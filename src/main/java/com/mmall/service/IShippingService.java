package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Shipping;

/**
 * @author fudada
 * @date 2019/5/24 - 9:58
 */
public interface IShippingService {

    ServiceResponse add(Integer userId, Shipping shipping);

    ServiceResponse del(Integer useId, Integer shippingId);

    ServiceResponse update(Integer userId, Shipping shipping);

    ServiceResponse<Shipping> select(Integer userId, Integer shippingId);

    ServiceResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
