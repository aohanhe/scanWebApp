package com.ao.scanWebApp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.AccountExpense;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanWebApp.Result;

import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
import ao.jpaQueryHelper.annotations.Like;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/expense")
@Api("���Ѽ�¼API")
public class ExpenseControl {
	private Logger logger=LoggerFactory.getLogger(ExpenseControl.class);
	
	@Autowired
	private ExpensesService service;
	
	@ApiModel("��ѯ���ݶ���")
	@JpaQueryBean(entityClass=AccountExpense.class)
	public static class QueryData extends PageJpaQueryBean{
		@ApiModelProperty(name="�û�ID",dataType="int")	
		private Integer userId=null;
		@Like
		@ApiModelProperty(name="�û���",dataType="string")	
		private String userName;
		@ApiModelProperty(name="�绰",dataType="string")	
		@Like
		private String phone;
		@ApiModelProperty(name="��Ӫ��ID",dataType="int")	
		private Integer operatorId;
		@ApiModelProperty(name="վ��ID",dataType="int")
		private Integer stationId;
		@ApiModelProperty(name="��������",dataType="string")
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
	@ApiOperation(value="��ȡ�����б�",httpMethod="POST", notes="��ȡ�����б�")
	public Result<PagerResult<AccountExpense>> list(@ApiParam  @RequestBody  QueryData queryData) throws ScanElectricityException{
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("��ѯ���Ѽ�¼����:"+e.getMessage(),e);
			return Result.fail("��ѯ���Ѽ�¼����:"+e.getMessage());
		}	
	}

}