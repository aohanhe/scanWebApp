package com.ao.scanWebApp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.QueryDslRootPaths;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.AccountRecharge;
import com.ao.scanElectricityBis.service.AccountService;
import com.ao.scanElectricityBis.service.RechargesService;
import com.ao.scanWebApp.Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;

import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.EntityPath;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
import cn.binarywang.wx.miniapp.api.WxMaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/recharge")
@Api("��ֵ��¼API")
public class RechargeControl {
	
	private Logger logger = LoggerFactory.getLogger(RechargeControl.class);
	@Autowired
	private RechargesService service;

	@ApiModel("��ѯ���ݶ���")
	@JpaQueryBean(entityClass=AccountRecharge.class,mainRootPath=QueryDslRootPaths.Root_Recharge)
	public static class QueryData extends PageJpaQueryBean {
		@ApiModelProperty(name = "�û�ID", dataType = "int")
		@EntityPath(name="userid")
		private Integer userId ;
		@ApiModelProperty(name = "����", dataType = "string")
		private String name;
		@ApiModelProperty(name = "�绰", dataType = "string")
		private String phone;
		@ApiModelProperty(name = "״̬", dataType = "int")
		private Integer status ;
		
		public Integer getUserId() {
			return userId;
		}
		public void setUserId(Integer userId) {
			this.userId = userId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
	}
	
	@PostMapping("list")	
	@ApiOperation(value = "��ȡ�û���ֵ�б�", httpMethod = "POST", notes = "��ȡ�û���ֵ�б�")
	public Result<PagerResult<AccountRecharge>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("��ȡ�û���ֵ�б����:"+e.getMessage(),e);
			return Result.fail("��ȡ�û���ֵ�б����:"+e.getMessage());
		}		
	}
	
	@Autowired
	private WxPayService wxService;
	
	@PostMapping("rechargeMoney")	
	@ApiOperation(value = "�û���ֵ", httpMethod = "POST", notes = "�û���ֵ")
	public Result<WxPayUnifiedOrderResult> RechargeMoney(@ApiParam @RequestBody  WxPayUnifiedOrderRequest request)
			throws WxPayException, ScanElectricityException {
		try {
						
			var re = wxService.unifiedOrder(request);
			return Result.success(re);
		} catch (Exception ex) {
			logger.error("����ͳһ�µ�����ʧ��:" + ex.getMessage(), ex);
			return Result.fail(2, "���ɶ���ʧ��");
		}
	}

}
