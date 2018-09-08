package com.ao.scanWebApp.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal.UserType;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.service.AccountService;
import com.ao.scanElectricityBis.service.UsersService;

@Service
public class ScanServerUserDetailsService implements UserDetailsService {
	private static Logger logger = LoggerFactory.getLogger(ScanServerUserDetailsService.class);

	

	@Autowired
	private AccountService accountServer;

	@Autowired
	private UsersService userServer;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		var request = this.getRequest();

		String grantType = request.getParameter("grant_type");
		String scope = request.getParameter("scope");
		String secret = request.getParameter("client_secret");
		String clientId = request.getParameter("client_id");
		// String username=request.getParameter("username");

		if (Strings.isBlank(clientId))
			throw new BadCredentialsException("不允许没有clientid的应用用户登入");

		ScanServerUserDetails user = null;

		if (clientId.equals(ScanClients.MangerAppClient)) {
			user = this.loadMangerAppUser(username);

		}

		if (clientId.equals(ScanClients.WxMinAppClient)) {
			user = this.loadWxMinAppUser(username);

		}
		
		return user;
	}

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();
	}

	

	/**
	 * 加载微信小程序用户信息
	 * 
	 * @param userName
	 * @return
	 * @throws AuthenticationException
	 */
	public ScanServerUserDetails loadWxMinAppUser(String openId) throws AuthenticationException {

		if (Strings.isBlank(openId))
			throw new BadCredentialsException("用户名不能为空");
		try {

			var userItem = this.userServer.getItemByOpenId(openId);
			if (userItem == null)
				throw new ScanElectricityException("opendid 对应的用户不存在");

			ScanServerUserDetails details = new ScanServerUserDetails();
			details.setUsername(userItem.getUserName());
			details.setPassword("");

			
			details.setName(userItem.getUserName());
			details.setOperatorId(0);
			details.setOperatorName("");
			details.setUserType(UserType.WxMinAppUser);
			details.setUser(userItem.getId());
			details.setPhone(userItem.getPhone());
			details.setAdminUser(false);

		

			List<SimpleGrantedAuthority> authors = new ArrayList<>();

		} catch (ScanElectricityException e) {
			logger.error("取得用户信息失败:" + e.getMessage(), e);
			throw new UsernameNotFoundException("取得用户openID=" + openId + "信息失败:" + e.getMessage(), e);
		}
		throw new UsernameNotFoundException("取得用户openID=" + openId + "信息失败");
	}

	/**
	 * 加载管理后台的用户信息
	 * 
	 * @param userName
	 * @return
	 */
	public ScanServerUserDetails loadMangerAppUser(String userName) throws AuthenticationException {
		if (Strings.isBlank(userName))
			throw new BadCredentialsException("用户名不能为空");
		try {
			var accountItem = this.accountServer.getUserByUserName(userName);
			if (accountItem != null) {
				ScanServerUserDetails details = new ScanServerUserDetails();
				details.setUsername(accountItem.getUserName());
				details.setPassword(accountItem.getPwd());

				
				details.setName(accountItem.getUserName());
				details.setOperatorId(accountItem.getOperatorId());
				details.setOperatorName(accountItem.getOperatorName());
				details.setUserType(UserType.MangerUser);
				details.setUser(accountItem.getId());
				details.setAdminUser(accountItem.getOperatorId() > 0 ? false : true);
				details.setPhone(accountItem.getPhone());
				details.setRightAreaCode(accountItem.getRegionCode());

			

				List<SimpleGrantedAuthority> authors = new ArrayList<>();
				// 区分帐号的类型
				if (accountItem.getOperatorId() == 0) {
					authors.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				} else {
					authors.add(new SimpleGrantedAuthority("ROLE_OPERATOR"));
				}

				details.setAuthorities(authors);

				return details;
			}
		} catch (ScanElectricityException e) {
			logger.error("取得用户信息失败:" + e.getMessage(), e);
			throw new UsernameNotFoundException("取得用户" + userName + "信息失败:" + e.getMessage(), e);
		}
		throw new UsernameNotFoundException("取得用户" + userName + "信息失败");
	}

}
