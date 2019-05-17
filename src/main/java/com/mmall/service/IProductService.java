package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Product;

/**
 * @author fudada
 * @date 2019/5/17 - 16:14
 */
public interface IProductService {

    ServiceResponse saveOrUpdateProduct(Product product);

    ServiceResponse<String> setSaleStatus(Integer productId, Integer status);
}
