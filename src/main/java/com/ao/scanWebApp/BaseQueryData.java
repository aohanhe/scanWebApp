package com.ao.scanWebApp;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 基础查询条件
 * @author aohanhe
 *
 */
@ApiModel("基础查询数据")
public class BaseQueryData {
	@ApiModelProperty(name="页码号",value="页码号，从1开始")
	private int page=1;
	@ApiModelProperty(name="返回记录数",value="返回记录数")
	private int limit=20;
	@ApiModelProperty(name="排序条件",value="排序条件 例+id,+表示升序 - 表示降序")
	private String sort;
	private HashMap<String, QuerySortType> sortMap=new HashMap<>();
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}	
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) throws ScanElectricityException {
		this.sort = sort;
		String[] arry = sort.split(";");
		for (String ar : arry) {
			if(ar.length()<=1) {
				throw new ScanElectricityException("查询参数不合法,每个查询字段一定是+/-字段名的结构:"+ar);
			}
			//分解第一个字母
			String sortType = ar.substring(0, 1);
			String field=ar.substring(1);
			
			sortMap.put(field, QuerySortType.fromString(sortType));
		}
	}
			
	public HashMap<String, QuerySortType> getSortMap() {
		return sortMap;
	}
	@Override
	public String toString() {		
		return String.format("page=%d limit=%d", this.page,this.limit);
	}
	
	/**
	 * 查询条件进行组合
	 * @param pre
	 * @param next
	 * @return
	 */
	public static<T> Predicate<T> andFilter(Predicate<T> pre,Predicate<T> next){
		if(pre!=null)
			return pre.and(next);
		return next;
	}
	
	/**
	 * 排序条件组合
	 * @param preOrder
	 * @param nextOrder
	 * @return
	 */
	public static<T> Comparator<T> andOrder(Comparator<T> preOrder,Comparator<T> nextOrder,QuerySortType sortType){
		
		if(sortType==QuerySortType.Desc)
			nextOrder = nextOrder.reversed();
		
		if(preOrder!=null)
			return preOrder.thenComparing(nextOrder);
		else
			return nextOrder;
	}

}
