package com.ao.scanWebApp.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal.UserType;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.service.AccountService;
import com.ao.scanElectricityBis.service.UsersService;

/**
 * 系统认证管理器
 * @author aohanhe
 *
 */
@Service
public class ScanServerAuthenticationProvider extends DaoAuthenticationProvider {
	private static Logger logger=LoggerFactory.getLogger(ScanServerAuthenticationProvider.class);
	
	@Value("${System.config.isCheckCode:true}")
	private boolean isCheckCode;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private ScanServerUserDetailsService detailServer;
	
	
	@PostConstruct
	public void init() {
		this.setUserDetailsService(this.detailServer);
		this.setPasswordEncoder(this.encoder);
	}
	
	/**
	 * 取得当前的request对象
	 * @return
	 */
	private HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		
		/**
		 * 检查用户的验证码
		 */
		this.checkVerifyCode();		
		
		super.additionalAuthenticationChecks(userDetails, authentication);
	}
	
	
	
	/**
	 * 检查验证码
	 */
	private void checkVerifyCode() throws AuthenticationException {
		if (!this.isCheckCode)
			return;

	}

}
