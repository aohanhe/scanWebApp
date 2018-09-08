package com.ao.scanWebApp;

import com.ao.scanElectricityBis.base.ScanElectricityException;

/**
 * ��ѯ������
 * @author aohanhe
 *
 */
public enum QuerySortType {
	Desc("+"),Asc("-");
	
	private String value;
	QuerySortType(String thValue) {
		this.value = thValue;
	}
	
	
	
	@Override
	public String toString() {
		
		return this.value;
	}
	
	/**
	 * ���ִ�ת����QuerySortType
	 * @param value
	 * @return
	 * @throws ScanElectricityException
	 */
	public static QuerySortType fromString(String value) throws ScanElectricityException {
		if(value.equals("+")) return QuerySortType.Desc;
		if(value.equals("-")) return QuerySortType.Asc;
		throw new ScanElectricityException("���ִ�ת��QuerySortTypeʧ��:"+value);
	}
}
