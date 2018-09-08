package com.ao.scanWebApp.authen;

import java.util.List;

import com.ao.scanElectricityBis.auth.IScanServerPrincipal;
import com.ao.scanElectricityBis.auth.ScanServerPrincipal;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * �û������Ϣ��
 * @author aohanhe
 *
 */
public class WebAppUserInfo extends ScanServerPrincipal{
	
	private String avatar="header.png";
	private String []roles= {"admin"};
	@JsonIgnore
	private List<Integer> rights;
	
	/**
	 * �����û���Ϣ����
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
	 * ����Ƿ�ӵ��ָ����Ȩ��
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
	 * �Ƿ�ӵ���б��е�����һ��Ȩ��
	 * @param roles
	 * @return
	 */
	public boolean hasLastOneRole(String []roles) {
		//���Ҫ����ֵΪ�գ���ֱ�ӷ�����
		if(roles==null || roles.length==0) return true;
		for(String role:roles) {
			if(this.hasRole(role)) return true;
		}
		return false;
	}	

}
