package com.ao.scanWebApp.authen;

import java.util.List;

import com.ao.scanElectricityBis.auth.IScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 用户身份信息项
 * @author aohanhe
 *
 */
public class WebAppUserInfo extends ScanServerPrincipal{
	
	private String avatar="header.png";
	private String []roles= {"admin"};
	@JsonIgnore
	private List<Integer> rights;
	
	/**
	 * 创建用户信息对象
	 * @param principal
	 * @return
	 */
	public static WebAppUserInfo wrap(IScanServerPrincipal principal) {
		var item=new WebAppUserInfo();
		item.setUser(principal.getUser());
		item.setOpenId(principal.getOpenId());
		item.setOperatorId(principal.getOperatorId());
		item.setOperatorName(principal.getOperatorName());
		item.setName(principal.getName());
		item.setPhone(principal.getPhone());
		item.setAdminUser(principal.isAdminUser());
		item.setUserType(principal.getUserType());
		item.setRightAreaCode(principal.getRightAreaCode());
		
		return item;
	}
	
	
	public List<Integer> getRights() {
		return rights;
	}
	public void setRights(List<Integer> rights) {
		this.rights = rights;
	}
	public String[] getRoles() {
		return roles;
	}
	public void setRoles(String[] roles) {
		this.roles = roles;
	}
	
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	/**
	 * 检查是否拥有指定的权限
	 * @param role
	 * @return
	 */
	public boolean hasRole(String role) {
		for (String item : this.roles) {
			if(item.equals(role)) return true;
		}
		return false;
	}
	/**
	 * 是否拥有列表中的至少一个权限
	 * @param roles
	 * @return
	 */
	public boolean hasLastOneRole(String []roles) {
		//如果要检查的值为空，则直接返回真
		if(roles==null || roles.length==0) return true;
		for(String role:roles) {
			if(this.hasRole(role)) return true;
		}
		return false;
	}	

}
