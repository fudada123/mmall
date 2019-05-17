package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fudada
 * @date 2019/5/17 - 16:14
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    public ServiceResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(product);
                if (rowCount > 0) {
                    return ServiceResponse.createBySuccess("更新产品成功");
                }
                return ServiceResponse.createByErrorMessage("产品更新失败");
            } else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                  return  ServiceResponse.createBySuccess("新增产品成功");
                }
                return ServiceResponse.createByErrorMessage("新增产品失败");
            }

        }
        return ServiceResponse.createByErrorMessage("新增或者更新产品参数不正确");
    }


    public ServiceResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("修改产品销售状态");
        }
        return ServiceResponse.createByErrorMessage("修改产品销售状态失败");
    }

    public ServiceResponse<Object> manageProductDetail(Integer productId){
        if (productId == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServiceResponse.createByErrorMessage("商品已下架或者删除");
        }
        //vo对象 - value object
        //pojo->bo(business object)->vo(view object)
        ProductDetailVo productDetailVo=

    }

    private assembleProductDetailVo(Product product) {

    }
}
