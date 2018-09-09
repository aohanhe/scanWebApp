package com.ao.scanWebApp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.scanElectricityBis.auth.ScanServerPrincipalManger;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.QUserInfo;
import com.ao.scanElectricityBis.entity.UserInfo;
import com.ao.scanElectricityBis.service.UsersService;
import com.ao.scanWebApp.BaseResult;
import com.ao.scanWebApp.Result;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.CanNull;
import ao.jpaQueryHelper.annotations.DslPredicateMehtod;
import ao.jpaQueryHelper.annotations.EntityPath;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
import ao.jpaQueryHelper.annotations.Like;
import ao.jpaQueryHelper.annotations.Or;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/user")
@Api(value = "用户管理API")
public class UserControl {
	private static Logger logger=LoggerFactory.getLogger(UserControl.class);
	
	@ApiModel("查询数据对象")
	@JpaQueryBean(entityClass = UserInfo.class)
	public static class QueryData extends PageJpaQueryBean {
		
		@ApiModelProperty(name = "名称", dataType = "string")
		@Like				
		private String name;
		@Like		
		private String phone;

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	};
	
	@Autowired
	private UsersService service;
	
	/**
	 * 根据通过查询bean进行用户信息分页查询
	 * @param queryData
	 * @return
	 * @throws ScanElectricityException
	 */
	@PostMapping("list")
	@ApiOperation(value = "获取用户信息列表", httpMethod = "POST", notes = "获取用户信息列表")
	public Result<PagerResult<UserInfo>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException  {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("查询用户出错:"+e.getMessage(),e);
			return Result.fail("查询用户出错:"+e.getMessage());
		}		
		
	}
	
	
	@PostMapping("item/{id}")
	@ApiOperation(value = "获取用户信息", httpMethod = "POST", notes = "获取用户信息")
	public Result<UserInfo> getItem(@ApiParam @PathVariable int id) throws ScanElectricityException{
		try {
			return Result.success(service.findItemById(id, UserInfo.class).block()) ;
		} catch (ScanElectricityException e) {
			logger.error("取得用户信息出错:"+e.getMessage(),e);
			return Result.fail("查询用户出错:"+e.getMessage());
		}
	}
	
	@Autowired
	private ScanServerPrincipalManger prManger;
	
	/**
	 * 开始充电指令
	 * @param plugCode 开始的插头编码
	 * @return
	 * @throws ScanElectricityException 
	 */
	@PostMapping("startCharger/{plugCode}")	
	@ApiOperation(value = "开始充电", httpMethod = "POST", notes = "开始充电")
	public BaseResult startCharger(@ApiParam @PathVariable String plugCode) throws ScanElectricityException {
		//首先检查当前用户是否有足够的钱		
		int userId = prManger.getCurrentUser().getUser();
		boolean hasMoney =service.hasEnoughMoney(userId);
		if(!hasMoney)
			return BaseResult.failResult(2, "帐户余额不足");
		return BaseResult.successResult();
	}

}
