package com.ao.scanWebApp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.QueryDslRootPaths;
import com.ao.scanElectricityBis.auth.ScanServerPrincipalManger;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.BaseAccount;
import com.ao.scanElectricityBis.entity.StationStationInfo;
import com.ao.scanElectricityBis.service.AccountService;
import com.ao.scanWebApp.BaseResult;
import com.ao.scanWebApp.Result;
import com.ao.scanWebApp.control.StationControl.QueryData;

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
@RequestMapping("/api/accounts")
@Api(value = "ϵͳ�ʺ�API")
public class AccountContol {
	private Logger logger = LoggerFactory.getLogger(BaseAccount.class);
	@Autowired
	private AccountService service;

	@Autowired
	private PasswordEncoder encoder;

	@ApiModel("��ѯ���ݶ���")
	@JpaQueryBean(entityClass = BaseAccount.class, mainRootPath = QueryDslRootPaths.Root_Account)
	public static class QueryData extends PageJpaQueryBean {
		@ApiModelProperty(name = "����", dataType = "string")
		@Like
		private String userName;
		@ApiModelProperty(name = "�绰", dataType = "string")
		@Like
		private String phone;
		@ApiModelProperty(name = "״̬", dataType = "boolean")
		private Boolean status;

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

		public Boolean getStatus() {
			return status;
		}

		public void setStatus(Boolean status) {
			this.status = status;
		}
	}

	@PostMapping("list")
	@ApiOperation(value = "��ȡ�ʻ��б�", httpMethod = "POST", notes = "��ȡ�ʻ��б�")
	Result<PagerResult<BaseAccount>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			//��ֹ�û�����й©
			list.getItems().forEach(item->{
				item.setPwd("");
			});
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("��ѯվ������:" + e.getMessage(), e);
			return Result.fail("��ѯվ������:" + e.getMessage());
		}
	}

	@PutMapping
	@ApiOperation(value = "�����µ��û��ʻ�", httpMethod = "PUT", notes = "�����µ��û��ʻ�")
	public Result<BaseAccount> saveNewItem(@RequestBody BaseAccount item) throws ScanElectricityException {

		try {
			// ���û�������м���
			item.setPwd(encoder.encode(item.getPwd()));

			return Result.success(service.saveNew(item));
		} catch (Exception e) {
			logger.error("�����µ��û��ʻ�ʧ��", e);
			return Result.fail(1, "�����µ��û��ʻ�ʧ��");
		}

	}

	@PostMapping
	@ApiOperation(value = "�����û��ʻ�", httpMethod = "PUT", notes = "�����û��ʻ�")
	public Result<BaseAccount> saveItem(@RequestBody BaseAccount item) throws ScanElectricityException {
		try {
			// ȡ��ԭʼ������
			var oldItem = service.findItemById(item.getId(), BaseAccount.class);
			item.setPwd(oldItem.block().getPwd());
			return Result.success(service.saveItem(item));
		} catch (ScanElectricityException e) {
			logger.error("�����û��ʻ�ʧ��", e);
			return Result.fail(1, "�����û��ʻ�ʧ��");
		}
	}

	@DeleteMapping("{id}")
	@ApiOperation(value = "ɾ���û��ʻ�", httpMethod = "POST", notes = "ɾ���û��ʻ�")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {

		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("ɾ���û��ʻ�ʧ��", e);
			return BaseResult.failResult(1, "ɾ���û��ʻ�ʧ��");
		}
	}

	@Autowired
	private ScanServerPrincipalManger userManger;

	@GetMapping("cur")
	@ApiOperation(value = "ȡ�õ�ǰ�û���Ϣ", httpMethod = "GET", notes = "ȡ�õ�ǰ�û���Ϣ")
	public Result<BaseAccount> getCurUserItem() throws ScanElectricityException {
		try {
			int userId = userManger.getCurrentUser().getUser();
			var res = service.findItemById(userId, BaseAccount.class);
			var item = res.block();
			item.setPwd("");

			return Result.success(item);
		} catch (ScanElectricityException e) {
			logger.error("ȡ�õ�ǰ�û���Ϣʧ��", e);
			return Result.fail(1, "ȡ�õ�ǰ�û���Ϣʧ��");
		}
	}
	
	@PostMapping("updateCurPwd/{phone}/{pwd}")
	@ApiOperation(value = "���µ�ǰ�û���Ϣ", httpMethod = "POST", notes = "���µ�ǰ�û���Ϣ")
	public BaseResult updateUserPwd(@PathVariable String phone,@PathVariable String pwd) throws ScanElectricityException {
		try {
			int userId = userManger.getCurrentUser().getUser();
			service.updateUserPwdAndPhone(userId, phone, encoder.encode(pwd));
			
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("ɾ���û��ʻ�ʧ��", e);
			return BaseResult.failResult(1, "ɾ���û��ʻ�ʧ��");
		}
	}
}
