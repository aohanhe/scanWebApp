package com.ao.scanWebApp.control;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.QueryDslRootPaths;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.AccountExpense;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanWebApp.Result;

import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.EntityPath;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
import ao.jpaQueryHelper.annotations.Like;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/expense")
@Api("消费记录API")
public class ExpenseControl {
	private Logger logger=LoggerFactory.getLogger(ExpenseControl.class);
	
	@Autowired
	private ExpensesService service;
	
	@ApiModel("查询数据对象")
	@JpaQueryBean(entityClass=AccountExpense.class,mainRootPath=QueryDslRootPaths.Root_Expense)
	public static class QueryData extends PageJpaQueryBean{
		@ApiModelProperty(name="用户ID",dataType="int")		
		private Integer userId=null;
		@EntityPath(rootPath=QueryDslRootPaths.Root_UserInfo)
		@Like
		@ApiModelProperty(name="用户名",dataType="string")	
		private String userName;
		
		@EntityPath(rootPath=QueryDslRootPaths.Root_UserInfo)
		@ApiModelProperty(name="电话",dataType="string")	
		@Like
		private String phone;
		
		@EntityPath(name="operatorid",rootPath=QueryDslRootPaths.Root_Station)
		@ApiModelProperty(name="运营商ID",dataType="int")	
		private Integer operatorId;
		
		@EntityPath(rootPath=QueryDslRootPaths.Root_Device)
		@ApiModelProperty(name="站场ID",dataType="int")
		private Integer stationId;
		
		@EntityPath(name="regioncode",rootPath=QueryDslRootPaths.Root_Station)
		@ApiModelProperty(name="地区编码",dataType="string")
		@Like(isStartWith=true)
		private String regionCode;
		
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public Integer getOperatorId() {
			return operatorId;
		}
		public void setOperatorId(Integer operatorId) {
			this.operatorId = operatorId;
		}
		public Integer getStationId() {
			return stationId;
		}
		public void setStationId(Integer stationId) {
			this.stationId = stationId;
		}
		public String getRegionCode() {
			return regionCode;
		}
		public void setRegionCode(String regionCode) {
			this.regionCode = regionCode;
		}
		
		
	};
	
	@PostMapping("list")	
	@ApiOperation(value="获取消费列表",httpMethod="POST", notes="获取消费列表")
	public Result<PagerResult<AccountExpense>> list(@ApiParam  @RequestBody  QueryData queryData) throws ScanElectricityException{
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("查询消费记录出错:"+e.getMessage(),e);
			return Result.fail("查询消费记录出错:"+e.getMessage());
		}	
	}
	
	/*
	 * 返回用户总的花费数据
	 */
	@RequestMapping("totalCost/{userId}")	
	public Result<HashMap<String, Object>> getTotalTimeAndMoney(@ApiParam @PathVariable int userId) {
		HashMap<String, Object> re=new HashMap<>();
		var data= service.getTotalTimeAndMoney(userId);
		re.put("cost",data.getFirst());
		re.put("time",data.getSecond());
		return Result.success(re);
	}

}
