package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author fudada
 * @date 2019/5/14 - 12:52
 */
//保证序列化json的时候，如果是null对象，key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServiceResponse<T> implements Serializable {
    private int Status;
    private String msg;
    private T data;


    private ServiceResponse(int status) {
        this.Status = status;
    }

    public ServiceResponse(int status, T data) {
        Status = status;
        this.data = data;
    }

    public ServiceResponse(int status, String msg) {
        Status = status;
        this.msg = msg;
    }

    public ServiceResponse(int status, String msg, T data) {
        Status = status;
        this.msg = msg;
        this.data = data;
    }

    //使之不在序列化结果当中
    @JsonIgnore
    public boolean isSuccess() {
        return this.Status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return Status;
    }

    public T getData(){
        return data;
    }

    public String getMsg(){
        return msg;
    }

    public static <T> ServiceResponse<T> createBySuccess(){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServiceResponse<T> createBySuccessMessage(String msg){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServiceResponse<T> createBySuccess(T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServiceResponse<T> createBySuccess(String msg,T data){
        return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServiceResponse<T> createByError(){
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    public static <T> ServiceResponse<T> createByErrorMessage(String errorMessage) {
        return new ServiceResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServiceResponse<T> createByErrorCodeMessage(int errorcode,String errorMessage){
        return new ServiceResponse<T>(errorcode, errorMessage);
    }

}
