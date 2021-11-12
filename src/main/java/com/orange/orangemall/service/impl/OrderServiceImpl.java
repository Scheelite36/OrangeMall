package com.orange.orangemall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.enums.OrderStatusEnum;
import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.model.dao.CartMapper;
import com.orange.orangemall.model.dao.OrderItemMapper;
import com.orange.orangemall.model.dao.OrderMapper;
import com.orange.orangemall.model.dao.ProductMapper;
import com.orange.orangemall.model.pojo.*;
import com.orange.orangemall.model.request.AddOrderRequest;
import com.orange.orangemall.model.vo.CartVO;
import com.orange.orangemall.model.vo.OrderItemVO;
import com.orange.orangemall.model.vo.OrderVO;
import com.orange.orangemall.service.CartService;
import com.orange.orangemall.service.OrderService;
import com.orange.orangemall.utils.OrderCodeFactory;
import com.orange.orangemall.utils.QRCodeUtils;
import com.orange.orangemall.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.RequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Scheelite
 * @date 2021/11/3
 * @email jwei.gan@qq.com
 * @description
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Value("${file.upload.ip}")
    String serverIp;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String add(AddOrderRequest addOrderRequest) {
        //通过用户id获取已经勾选购物车
        Integer userId = UserUtils.getLoginUserId();
        List<CartVO> cartList = cartMapper.selectCartVOList(userId);
        if (CollectionUtils.isEmpty(cartList)) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        cartList = cartList.stream().filter(cart -> cart.getSelected().equals(OrangeMallConstant.SELECTED)).collect(Collectors.toList());
        // 判断商品存在库存状态，并扣库存
        validateCart(cartList);
        // 转化为订单item对象并删除购物车
        List<OrderItem> orderItems = addOrderItem(cartList);
        // 生成订单
        Order order = new Order();
        String orderCode = OrderCodeFactory.getOrderCode(userId);
        order.setOrderNo(orderCode);
        order.setOrderStatus(OrderStatusEnum.UNPAID.getCode());
        order.setPaymentType(addOrderRequest.getPaymentType());
        order.setPostage(addOrderRequest.getPostage());
        order.setReceiverAddress(addOrderRequest.getReceiverAddress());
        order.setReceiverMobile(addOrderRequest.getReceiverMobile());
        order.setReceiverName(addOrderRequest.getReceiverName());
        order.setUserId(userId);
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        order.setTotalPrice(totalPrice);
        orderMapper.insertSelective(order);
        // 保存每个商品到item表
        orderItems.forEach(orderItem -> {
            orderItem.setOrderNo(orderCode);
            orderItemMapper.insertSelective(orderItem);
        });
        return orderCode;
    }

    /**
     * 校验商品是否上架状态及库存, 并扣库存
     *
     * @param cartVOS
     */
    private void validateCart(List<CartVO> cartVOS) {
        for (CartVO cartVO : cartVOS) {
            Product product = productMapper.selectByPrimaryKey(cartVO.productId);
            if (product == null || product.getStatus().equals(OrangeMallConstant.OFF_SALE)) {
                throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
            }
            if (cartVO.getQuantity() > product.getStock()) {
                throw new OrangeMallException(OrangeMallExceptionEnum.NOT_ENOUGH);
            }
            Product newProduct = new Product();
            newProduct.setStock(product.getStock() - cartVO.getQuantity());
            newProduct.setId(product.getId());
            int count = productMapper.updateByPrimaryKeySelective(newProduct);
            if (count == 0) {
                throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
            }
        }
    }

    /**
     * 用于返回订单item对象并删除购物车
     *
     * @param cartVOS
     * @return
     */
    private List<OrderItem> addOrderItem(List<CartVO> cartVOS) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartVO cartVO : cartVOS) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
            orderItems.add(orderItem);
            int count = cartMapper.deleteByPrimaryKey(cartVO.getId());
            if (count == 0) {
                throw new OrangeMallException(OrangeMallExceptionEnum.DELETE_FAIL);
            }
        }
        return orderItems;
    }

    @Override
    public OrderVO detail(String orderNo) {
        Integer userId = UserUtils.getLoginUserId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        validOrderBelong(userId, order);
        return getOrderVO(order);
    }

    /**
     * 校验订单是否存在并属于该用户
     *
     * @param userId
     * @param order
     */
    private void validOrderBelong(Integer userId, Order order) {
        if (order == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        if (!userId.equals(order.getUserId())) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UNMATCHED_ORDER);
        }
    }

    /**
     * 根据order对象放回oderVo对象
     *
     * @param order
     * @return
     */
    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderStatusName(OrderStatusEnum.getOrderStatusInfo(orderVO.getOrderStatus()));
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOS = new ArrayList<>();
        orderItems.forEach(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOS.add(orderItemVO);
        });
        orderVO.setOrderItemVOList(orderItemVOS);
        return orderVO;
    }

    /**
     * 返回用户的所有订单
     *
     * @param pageNumm
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo list(Integer pageNumm, Integer pageSize) {
        Integer userId = UserUtils.getLoginUserId();
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVO> orderVOS = new ArrayList<>();
        for (Order order : orderList) {
            orderVOS.add(getOrderVO(order));
        }
        PageHelper.startPage(pageNumm, pageSize);
        return new PageInfo(orderVOS);
    }

    /**
     * 未支付状态取消订单
     *
     * @param orderNo
     */
    @Override
    public void cancel(String orderNo) {
        Integer userId = UserUtils.getLoginUserId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (!order.getOrderStatus().equals(OrderStatusEnum.UNPAID.getCode())) {
            throw new OrangeMallException(OrangeMallExceptionEnum.STATUS_ERROR);
        }
        validOrderBelong(userId, order);
        Order newOder = new Order();
        newOder.setId(order.getId());
        newOder.setOrderStatus(OrderStatusEnum.CANCLED.getCode());
        newOder.setEndTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(newOder);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    /**
     * 生成支付二维码
     * @param orderNo
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public String qrCode(String orderNo) throws IOException, WriterException {
        // 获取本机port，拼接支付url
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        int port = request.getLocalPort();
        try{
            // 获取当前局域网ip
            // 存在隐患：当局域网络比较复杂（虚拟网卡、多网口同时使用等）会获取到错误地址
            String innerIp = InetAddress.getLocalHost().getHostAddress();
        }
        String payUrl = "http://" + serverIp + ":" + port + "/pay?orderNo=" + orderNo;
        String filePath = OrangeMallConstant.FILE_UPLOAD_DIR + orderNo + ".png";
        // 调用工具生成二维码图片，返回支付图片url
        QRCodeUtils.generateORCode(payUrl, 400, 400, filePath);
        return "http://" + serverIp + ":" +port + OrangeMallConstant.FILE_ACCESS + orderNo + ".png";
    }

    /**
     * 支付订单
     * @param orderNo
     */
    @Override
    public void pay(String orderNo){
        Integer userId = UserUtils.getLoginUserId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        validOrderBelong(userId,order);
        if (!order.getOrderStatus().equals(OrderStatusEnum.UNPAID.getCode())) {
            throw new OrangeMallException(OrangeMallExceptionEnum.STATUS_ERROR);
        }
        Order newOrder = new Order();
        newOrder.setId(order.getId());
        newOrder.setPayTime(new Date());
        newOrder.setOrderStatus(OrderStatusEnum.PAID.getCode());
        int count = orderMapper.updateByPrimaryKeySelective(newOrder);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    /**
     * 完成订单
     * @param orderNo
     */
    @Override
    public void finish(String orderNo){
        Integer userId = UserUtils.getLoginUserId();
        User user = UserUtils.getLoginUser();
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (user.getRole().equals(OrangeMallConstant.CUSTOM_ROLE)) {
            validOrderBelong(userId,order);
        }
        if (order == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        if (!order.getOrderStatus().equals(OrderStatusEnum.DELIVERED.getCode())) {
            throw new OrangeMallException(OrangeMallExceptionEnum.STATUS_ERROR);
        }
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        newOrder.setId(order.getId());
        newOrder.setEndTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(newOrder);
        if (count==0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    /**
     * 订单发货
     * @param orderNo
     */
    @Override
    public void delivery(String orderNo){
        Integer userId = UserUtils.getLoginUserId();
        Order order = orderMapper.selectByOrderNo(orderNo);
        validOrderBelong(userId,order);
        if (!order.getOrderStatus().equals(OrderStatusEnum.PAID.getCode())) {
            throw new OrangeMallException(OrangeMallExceptionEnum.STATUS_ERROR);
        }
        Order newOrder = new Order();
        newOrder.setId(order.getId());
        newOrder.setDeliveryTime(new Date());
        newOrder.setOrderStatus(OrderStatusEnum.DELIVERED.getCode());
        int count = orderMapper.updateByPrimaryKeySelective(newOrder);
        if (count==0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    // todo 传入可选排序内容
    @Override
    public PageInfo listForAdmin(Integer pageNo,Integer pageSize){
        PageHelper.startPage(pageNo,pageSize);
        List<Order> orderList = orderMapper.selectAll();
        List<OrderVO> orderVOS = new ArrayList<>();
        for (Order order : orderList) {
            orderVOS.add(getOrderVO(order));
        }
        return new PageInfo(orderVOS);
    }
}
