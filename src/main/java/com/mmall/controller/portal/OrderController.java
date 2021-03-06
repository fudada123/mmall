package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServiceResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.Map;

/**
 * @author fudada
 * @date 2019/5/27 - 13:39
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private IOrderService iOrderService;



    @RequestMapping("create.do")
    @ResponseBody
    public ServiceResponse create(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }

    @RequestMapping("cancel.do")
    @ResponseBody
    public ServiceResponse cancel(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(),orderNo);
    }

    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServiceResponse getOrderCartProduct(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }


    @RequestMapping("detail.do")
    @ResponseBody
    public ServiceResponse detail(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }


    @RequestMapping("list.do")
    @ResponseBody
    public ServiceResponse list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1")int pageNum, @RequestParam(value = "pageSize",defaultValue ="10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }
























    @RequestMapping("pay.do")
    @ResponseBody
    public ServiceResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }


    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipaycallback(HttpServletRequest request){
        Map<String, String> params = Maps.newHashMap();

        Map requestParameter = request.getParameterMap();
        for (Iterator iterator = requestParameter.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParameter.get(name);
            String valueStr = "";
            for (int i = 0; i <values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]:valueStr + values[i]+",";
            }

            params.put(name, valueStr);

        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("tread_status"),params.toString());


        //非常重要，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServiceResponse.createByErrorMessage("非法请求，验证不通过，再恶意请求报警");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常",e);
        }

        //todo  验证各种数据


        ServiceResponse serviceResponse = iOrderService.aliCallback(params);
        if (serviceResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }

        return Const.AlipayCallback.RESPONSE_FAILED;
    }



    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServiceResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServiceResponse   serviceResponse= iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if (serviceResponse.isSuccess()) {
            return ServiceResponse.createBySuccess(true);
        }
        return ServiceResponse.createBySuccess(false);
    }


}
