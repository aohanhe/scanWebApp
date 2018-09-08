package com.ao.scanWebApp;

import com.ao.scanElectricityBis.base.ScanElectricityException;

/**
 * 查询排序方向
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
	 * 从字串转换到QuerySortType
	 * @param value
	 * @return
	 * @throws ScanElectricityException
	 */
	public static QuerySortType fromString(String value) throws ScanElectricityException {
		if(value.equals("+")) return QuerySortType.Desc;
		if(value.equals("-")) return QuerySortType.Asc;
		throw new ScanElectricityException("从字串转换QuerySortType失败:"+value);
	}
}
