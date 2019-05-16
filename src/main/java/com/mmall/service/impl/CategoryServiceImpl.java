package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServiceResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategorySerivce;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author fudada
 * @date 2019/5/16 - 16:49
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategorySerivce {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;



    public ServiceResponse addCategory(String categoryName,Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServiceResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可以用的

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("添加商品成功");
        }

        return ServiceResponse.createByErrorMessage("添加品类失败");
    }


    public ServiceResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == 0 || StringUtils.isBlank(categoryName)) {
            return ServiceResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServiceResponse.createBySuccess("更改商品名称成功");
        }
        return ServiceResponse.createByErrorMessage("更改商品名称失败");
    }

    public ServiceResponse<List<Category>> getChildrenparallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParntId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子类");
        }
        return ServiceResponse.createBySuccess(categoryList);
    }

    public ServiceResponse getCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);
        List<Integer> categoryList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryList.add(categoryItem.getId());
            }
        }
        return ServiceResponse.createBySuccess(categoryList);
    }

    //递归算法，算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {

        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找子节点，递归算法一定要有一个推出条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParntId(categoryId);
        for (Category categoryItem:categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
