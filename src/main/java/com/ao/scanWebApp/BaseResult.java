package com.ao.scanWebApp;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import io.swagger.annotations.ApiModelProperty;

public class BaseResult {
	@ApiModelProperty(name="返回状态码",value="返回状态码,0 表示成功")
	private int code= 0;
	@ApiModelProperty(name="错误消息",value="返回状态消息")
	private String  message;
	
	
	public BaseResult(int code,String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 构造一个失败结果
	 * @param code
	 * @param message
	 * @return
	 * @throws ScanElectricityException 
	 */
	public static BaseResult failResult(int code,String message) throws ScanElectricityException{
		
		if(code==0) throw new ScanElectricityException("失败结果不能设置code 为0");
		
		return new BaseResult(code,message);
	}
	
	/**
	 * 构造一个成功结果
	 * @param data
	 * @return
	 */
	public static BaseResult successResult() {
		BaseResult re=new BaseResult(0, "成功");
		
		return re;
	}

}
