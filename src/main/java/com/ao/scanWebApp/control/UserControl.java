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
@Api(value = "�û�����API")
public class UserControl {
	private static Logger logger=LoggerFactory.getLogger(UserControl.class);
	
	@ApiModel("��ѯ���ݶ���")
	@JpaQueryBean(entityName="UserInfo", entityClass = UserInfo.class)
	public static class QueryData extends PageJpaQueryBean {
		
		@ApiModelProperty(name = "����", dataType = "string")
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
	 * ����ͨ����ѯbean�����û���Ϣ��ҳ��ѯ
	 * @param queryData
	 * @return
	 * @throws ScanElectricityException
	 */
	@PostMapping("list")
	@ApiOperation(value = "��ȡ�û���Ϣ�б�", httpMethod = "POST", notes = "��ȡ�û���Ϣ�б�")
	public Result<PagerResult<UserInfo>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException  {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("��ѯ�û�����:"+e.getMessage(),e);
			return Result.fail(1, "��ѯ�û�����:"+e.getMessage());
		}		
		
	}

}
