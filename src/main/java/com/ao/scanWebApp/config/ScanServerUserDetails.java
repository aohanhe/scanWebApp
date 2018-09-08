package com.ao.scanWebApp.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ao.scanElectricityBis.auth.ScanServerPrincipal;

/**
 * 用户信息类
 * @author aohanhe
 *
 */
public class ScanServerUserDetails extends ScanServerPrincipal implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 133357192012402549L;
	private String username;
	private String password;
	private List<? extends GrantedAuthority> authorities;
	
	
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
		
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
		
	}

	public void setPassword(String password) {
		this.password = password;
		
	}

	public void setAuthorities(List<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
		
	}

	

}
