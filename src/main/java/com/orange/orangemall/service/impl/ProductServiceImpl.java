package com.orange.orangemall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange.orangemall.common.OrangeMallConstant;
import com.orange.orangemall.exception.OrangeMallException;
import com.orange.orangemall.enums.OrangeMallExceptionEnum;
import com.orange.orangemall.model.dao.ProductMapper;
import com.orange.orangemall.model.pojo.Product;
import com.orange.orangemall.model.request.AddProductReq;
import com.orange.orangemall.model.request.ProductListReq;
import com.orange.orangemall.model.request.UpdateProductReq;
import com.orange.orangemall.model.vo.CategoryVO;
import com.orange.orangemall.query.ProductListQuery;
import com.orange.orangemall.service.CategoryService;
import com.orange.orangemall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Scheelite
 * @date 2021/10/16
 * @email jwei.gan@qq.com
 * @description
 **/
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        String name = addProductReq.getName();
        Product product = productMapper.selectByName(name);
        if (product != null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NAME_EXISTED);
        }
        Product productNew = new Product();
        BeanUtils.copyProperties(addProductReq, productNew);
        int count = productMapper.insertSelective(productNew);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.INSERT_FAIL);
        }
    }

    @Override
    public String uploadFile(HttpServletRequest request, MultipartFile file) throws IOException, URISyntaxException {
        // 文件重命名
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + suffixName;
//        System.out.println(OrangeMallConstant.FILE_UPLOAD_DIR);
        File fileDirectory = new File(OrangeMallConstant.FILE_UPLOAD_DIR);
        File filePath = new File(OrangeMallConstant.FILE_UPLOAD_DIR + newFileName);
        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new OrangeMallException(OrangeMallExceptionEnum.CREATE_FAIL);
            }
        }
        // 写入文件，返回url
        file.transferTo(filePath);
        return getHost(new URI(request.getRequestURL()+"")) + OrangeMallConstant.FILE_ACCESS + newFileName;
    }

    /**
     * 获取请求地址host
     *
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    private URI getHost(URI uri) throws URISyntaxException {
        return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
    }

    @Override
    public void update(UpdateProductReq updateProductReq) {
        Product product = productMapper.selectByPrimaryKey(updateProductReq.getId());
        if (product == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        if (updateProductReq.getName().equals(product.getName())) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NAME_EXISTED);
        }
        BeanUtils.copyProperties(updateProductReq, product);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.UPDATE_FAIL);
        }
    }

    @Override
    public void delete(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new OrangeMallException(OrangeMallExceptionEnum.DELETE_FAIL);
        }
    }

    @Override
    public void batchUpdateStatus(Integer[] ids, Integer status) {
        int count = productMapper.batchUpdateStatus(ids, status);
        // todo 更新失败抛出异常的代码

    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectAll();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new OrangeMallException(OrangeMallExceptionEnum.NOT_EXIST);
        }
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        ProductListQuery query = new ProductListQuery();
        // 处理关键词
        if (productListReq.getKeyword() != null) {
            StringBuilder builder = new StringBuilder();
            String keyword = builder.append("%").append(productListReq.getKeyword()).append("%").toString();
            query.setKeyword(keyword);
        }
        //获取目录列表
        List<Integer> categoryIds = new ArrayList<>();
        if (productListReq.getCategoryId() != null) {
            // 获取商品分类信息
            List<CategoryVO> categoryVOS = categoryService.categoryVOS(productListReq.getCategoryId());
            // 获取所有目录id（含子目录）
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOS, categoryIds);
            query.setIds(categoryIds);
        }
        PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(),
                OrangeMallConstant.ORDER_BY.contains(productListReq.getOrderBy()) ? productListReq.getOrderBy() : "");
        List<Product> productList = productMapper.selectSelected(query);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    /**
     * 递归从树状目录中获取目录id列表
     *
     * @param categoryVOS
     * @param ids
     * @return
     */
    private void getCategoryIds(List<CategoryVO> categoryVOS, List<Integer> ids) {
        if (!CollectionUtils.isEmpty(categoryVOS)) {
            categoryVOS.forEach(
                    categoryVO -> {
                        ids.add(categoryVO.getId());
                        List<CategoryVO> childCategories = categoryVO.getChildCategories();
                        getCategoryIds(childCategories, ids);
                    }
            );
        }
    }
}
