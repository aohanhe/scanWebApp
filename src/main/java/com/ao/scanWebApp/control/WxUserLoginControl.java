package com.ao.scanWebApp.control;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.scanElectricityBis.auth.ScanServerPrincipalManger;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.BaseAccount;
import com.ao.scanElectricityBis.entity.UserInfo;
import com.ao.scanElectricityBis.service.UsersService;
import com.ao.scanWebApp.BaseResult;
import com.ao.scanWebApp.Result;

import cn.binarywang.wx.miniapp.api.WxMaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 微信用户登入控制
 * 
 * @author aohanhe
 *
 */
@RestController
@RequestMapping("/api/wx")
public class WxUserLoginControl {
	private Logger logger=LoggerFactory.getLogger(WxUserLoginControl.class); 
	@Autowired
	private UsersService service;
	@Autowired
	private WxMaService wxService;

	@PostMapping("loginByPhone")
	@ApiOperation(value = "电话登入", httpMethod = "POST", notes = "电话登入")
	public Result<String> loginByPhone(@ApiParam @RequestBody HashMap<String, String> data)
			throws ScanElectricityException {

		try {

			HashMap<String, String> re = new HashMap<>();
			String auCode = data.get("code");
			String phone = data.get("phone");
			String code = data.get("checkpwd");
			
			var wxUserInfo=wxService.getUserService().getSessionInfo(auCode);
			

			re.put("openid", wxUserInfo.getOpenid());

			// 检查手机号对应的验证码是否正确
			if (!code.equals("1234"))
				throw new ScanElectricityException("验证码错误");

			var userInfo = service.getOrCreateItemByPhone(phone, wxUserInfo.getOpenid());

			return Result.success(wxUserInfo.getOpenid());
		} catch (Exception ex) {
			logger.error("验证错误:" + ex.getMessage(), ex);
			return Result.fail("验证错误:" + ex.getMessage());
		}

	}
	
	@Autowired
	private ScanServerPrincipalManger userManger;
	
	@GetMapping("cur")
	@ApiOperation(value = "取得当前用户信息", httpMethod = "GET", notes = "取得当前用户信息")
	public Result<UserInfo> getCurUserItem() throws ScanElectricityException {
		try {
			int userId = userManger.getCurrentUser().getUser();
			var res = service.findItemById(userId, UserInfo.class);
			var item = res.block();
			item.setPwd("");

			return Result.success(item);
		} catch (ScanElectricityException e) {
			logger.error("取得当前用户信息失败", e);
			return Result.fail(1, "取得当前用户信息失败");
		}
	}

}
