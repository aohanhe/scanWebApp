package com.ao.scanWebApp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.QUserInfo;
import com.ao.scanElectricityBis.entity.UserInfo;
import com.ao.scanElectricityBis.service.UsersService;
import com.ao.scanWebApp.Result;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.DslPredicateMehtod;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
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
	@JpaQueryBean(entityName="UserInfo", entityClass = UserInfo.class)
	public static class QueryData extends PageJpaQueryBean {
		
		@ApiModelProperty(name = "名称", dataType = "string")
		@DslPredicateMehtod(value="queryName", orderMehtod = "orderyName")
		private String name;
		
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
		
		
		public BooleanExpression queryName() {
			return QUserInfo.userInfo.name.like("%"+this.name+"%");
		}
		
		public Path orderyName() {
			return QUserInfo.userInfo.name;
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
			return Result.fail(1, "查询用户出错:"+e.getMessage());
		}		
		
	}

}
