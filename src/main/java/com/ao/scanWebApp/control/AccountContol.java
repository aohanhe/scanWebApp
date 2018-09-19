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
@Api(value = "系统帐号API")
public class AccountContol {
	private Logger logger = LoggerFactory.getLogger(BaseAccount.class);
	@Autowired
	private AccountService service;

	@Autowired
	private PasswordEncoder encoder;

	@ApiModel("查询数据对象")
	@JpaQueryBean(entityClass = BaseAccount.class, mainRootPath = QueryDslRootPaths.Root_Account)
	public static class QueryData extends PageJpaQueryBean {
		@ApiModelProperty(name = "名称", dataType = "string")
		@Like
		private String userName;
		@ApiModelProperty(name = "电话", dataType = "string")
		@Like
		private String phone;
		@ApiModelProperty(name = "状态", dataType = "boolean")
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
	@ApiOperation(value = "获取帐户列表", httpMethod = "POST", notes = "获取帐户列表")
	Result<PagerResult<BaseAccount>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			//防止用户密码泄漏
			list.getItems().forEach(item->{
				item.setPwd("");
			});
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("查询站场出错:" + e.getMessage(), e);
			return Result.fail("查询站场出错:" + e.getMessage());
		}
	}

	@PutMapping
	@ApiOperation(value = "保存新的用户帐户", httpMethod = "PUT", notes = "保存新的用户帐户")
	public Result<BaseAccount> saveNewItem(@RequestBody BaseAccount item) throws ScanElectricityException {

		try {
			// 把用户密码进行加密
			item.setPwd(encoder.encode(item.getPwd()));

			return Result.success(service.saveNew(item));
		} catch (Exception e) {
			logger.error("保存新的用户帐户失败", e);
			return Result.fail(1, "保存新的用户帐户失败");
		}

	}

	@PostMapping
	@ApiOperation(value = "保存用户帐户", httpMethod = "PUT", notes = "保存用户帐户")
	public Result<BaseAccount> saveItem(@RequestBody BaseAccount item) throws ScanElectricityException {
		try {
			// 取回原始的密码
			var oldItem = service.findItemById(item.getId(), BaseAccount.class);
			item.setPwd(oldItem.block().getPwd());
			return Result.success(service.saveItem(item));
		} catch (ScanElectricityException e) {
			logger.error("保存用户帐户失败", e);
			return Result.fail(1, "保存用户帐户失败");
		}
	}

	@DeleteMapping("{id}")
	@ApiOperation(value = "删除用户帐户", httpMethod = "POST", notes = "删除用户帐户")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {

		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("删除用户帐户失败", e);
			return BaseResult.failResult(1, "删除用户帐户失败");
		}
	}

	@Autowired
	private ScanServerPrincipalManger userManger;

	@GetMapping("cur")
	@ApiOperation(value = "取得当前用户信息", httpMethod = "GET", notes = "取得当前用户信息")
	public Result<BaseAccount> getCurUserItem() throws ScanElectricityException {
		try {
			int userId = userManger.getCurrentUser().getUser();
			var res = service.findItemById(userId, BaseAccount.class);
			var item = res.block();
			item.setPwd("");

			return Result.success(item);
		} catch (ScanElectricityException e) {
			logger.error("取得当前用户信息失败", e);
			return Result.fail(1, "取得当前用户信息失败");
		}
	}
	
	@PostMapping("updateCurPwd/{phone}/{pwd}")
	@ApiOperation(value = "更新当前用户信息", httpMethod = "POST", notes = "更新当前用户信息")
	public BaseResult updateUserPwd(@PathVariable String phone,@PathVariable String pwd) throws ScanElectricityException {
		try {
			int userId = userManger.getCurrentUser().getUser();
			service.updateUserPwdAndPhone(userId, phone, encoder.encode(pwd));
			
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("删除用户帐户失败", e);
			return BaseResult.failResult(1, "删除用户帐户失败");
		}
	}
}
