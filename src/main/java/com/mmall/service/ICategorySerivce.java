package com.mmall.service;

import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author fudada
 * @date 2019/5/16 - 16:48
 */
public interface ICategorySerivce {

    ServiceResponse addCategory(String categoryName, Integer parentId);

    ServiceResponse updateCategoryName(Integer categoryId, String categoryName);

    ServiceResponse<List<Category>> getChildrenparallelCategory(Integer categoryId);

    ServiceResponse getCategoryAndChildrenById(Integer categoryId);

}
