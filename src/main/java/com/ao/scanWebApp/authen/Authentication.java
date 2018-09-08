package com.ao.scanWebApp.authen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipalManger;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanWebApp.Result;

import io.swagger.annotations.ApiOperation;


@RestController()
@RequestMapping("/api/auth")
public class Authentication {
	@Autowired
	private ScanServerPrincipalManger userManger;
	

	@PostMapping("userinfo")
	@RequestRoles({ "admin", "oper", "customer" })
	@ApiOperation(value = "用户信息取得", httpMethod = "POST", notes = "用户信息取得")
	public Result<WebAppUserInfo> userInfo() throws ScanElectricityException {
		
		
		var userInfo = userManger.getCurrentUser();		
		
		if(userInfo==null) 
			Result.fail(401, "当前用户消息缺失");
		
		return Result.success(WebAppUserInfo.wrap(userInfo));
	}
}
