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
import org.slf4j.LoggerFactory;
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
        //????????????id???????????????????????????
        Integer userId = UserUtils.getLoginUserId();
        List<CartVO> cartList = cartMapper.selectCartVOList(userId);
        if (CollectionUtils.isEmpty(cartList)) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        cartList = cartList.stream().filter(cart -> cart.getSelected().equals(OrangeMallConstant.SELECTED)).collect(Collectors.toList());
        // ?????????????????????????????????????????????
        validateCart(cartList);
        // ???????????????item????????????????????????
        List<OrderItem> orderItems = addOrderItem(cartList);
        // ????????????
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
        // ?????????????????????item???
        orderItems.forEach(orderItem -> {
            orderItem.setOrderNo(orderCode);
            orderItemMapper.insertSelective(orderItem);
        });
        return orderCode;
    }

    /**
     * ???????????????????????????????????????, ????????????
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
     * ??????????????????item????????????????????????
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
     * ??????????????????????????????????????????
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
     * ??????order????????????oderVo??????
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
     * ???????????????????????????
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
     * ???????????????????????????
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
     * ?????????????????????
     * @param orderNo
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public String qrCode(String orderNo) throws IOException, WriterException {
        // ????????????port???????????????url
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        int port = request.getLocalPort();
        try{
            // ?????????????????????ip
            // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
            String innerIp = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
        String payUrl = "http://" + serverIp + ":" + port + "/pay?orderNo=" + orderNo;
        String filePath = OrangeMallConstant.FILE_UPLOAD_DIR + orderNo + ".png";
        // ??????????????????????????????????????????????????????url
        QRCodeUtils.generateORCode(payUrl, 400, 400, filePath);
        return "http://" + serverIp + ":" +port + OrangeMallConstant.FILE_ACCESS + orderNo + ".png";
    }

    /**
     * ????????????
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
     * ????????????
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
     * ????????????
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

    // todo ????????????????????????
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
