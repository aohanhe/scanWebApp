package com.ao.scanWebApp;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ������ѯ����
 * @author aohanhe
 *
 */
@ApiModel("������ѯ����")
public class BaseQueryData {
	@ApiModelProperty(name="ҳ���",value="ҳ��ţ���1��ʼ")
	private int page=1;
	@ApiModelProperty(name="���ؼ�¼��",value="���ؼ�¼��")
	private int limit=20;
	@ApiModelProperty(name="��������",value="�������� ��+id,+��ʾ���� - ��ʾ����")
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
				throw new ScanElectricityException("��ѯ�������Ϸ�,ÿ����ѯ�ֶ�һ����+/-�ֶ����Ľṹ:"+ar);
			}
			//�ֽ��һ����ĸ
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
	 * ��ѯ�����������
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
	 * �����������
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
