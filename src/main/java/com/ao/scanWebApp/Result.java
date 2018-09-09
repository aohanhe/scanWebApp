package com.ao.scanWebApp;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 返回结果结构
 * @author aohanhe
 *
 */
@ApiModel("返回结果集")
public class Result<T> extends BaseResult {
	

	@ApiModelProperty(name="负载数据")
	private T data = null;
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public Result(int code, String message) {
		super(code, message);		
	}
	
	/**
	 * 构造一个失败结果
	 * @param code
	 * @param message
	 * @return
	 * @throws ScanElectricityException 
	 */
	public static<T> Result fail(int code,String message) throws ScanElectricityException{
		
		if(code==0) throw new ScanElectricityException("失败结果不能设置code 为0");
		
		return new Result(code,message);
	}
	
	/**
	 * 构造一个标准错误
	 * @param message
	 * @return
	 * @throws ScanElectricityException
	 */
	public static<T> Result fail(String message) throws ScanElectricityException{
		return new Result(500,message);		
	}
	
	/**
	 * 构造一个成功结果
	 * @param data
	 * @return
	 */
	public static<T> Result success(T data) {
		Result<T> re=new Result<>(0, "成功");
		re.setData(data);
		return re;
	}
}
