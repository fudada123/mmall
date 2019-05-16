package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategorySerivce;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author fudada
 * @date 2019/5/16 - 16:08
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategorySerivce iCategorySerivce;



    @RequestMapping("add_category.do")
    @ResponseBody
    public ServiceResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId",defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        //校验下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //增加我们的分类的逻辑
            return iCategorySerivce.addCategory(categoryName,parentId);
        } else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }


    }

    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServiceResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //更新categoryName
          return  iCategorySerivce.updateCategoryName(categoryId, categoryName);
        } else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping("get_category.do")
    @ResponseBody
    public ServiceResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //查询子节点的category信息，并且不递归，保持平级
          return   iCategorySerivce.getChildrenparallelCategory(categoryId);
        } else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }


    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServiceResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //查询当前节点的id和递归子节点的id
            return iCategorySerivce.getChildrenparallelCategory(categoryId);
        } else {
            return ServiceResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }


}
