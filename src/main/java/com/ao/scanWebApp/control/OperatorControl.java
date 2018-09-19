package com.ao.scanWebApp.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ao.QueryDslRootPaths;
import com.ao.scanElectricityBis.base.ScanElectricityException;

import com.ao.scanElectricityBis.entity.BaseOperator;
import com.ao.scanElectricityBis.service.OperatorsService;
import com.ao.scanWebApp.BaseResult;
import com.ao.scanWebApp.Result;


import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
import ao.jpaQueryHelper.annotations.Like;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/operator")
@Api(value="运营商API")
public class OperatorControl {
	private Logger logger=LoggerFactory.getLogger(OperatorControl.class);

	@ApiModel("查询数据对象")
	@JpaQueryBean(entityClass=BaseOperator.class,mainRootPath=QueryDslRootPaths.Root_Operator)
	public static class QueryData extends PageJpaQueryBean{
		@ApiModelProperty(name="名称",dataType="string")	
		@Like
		private String name;
		
		private Boolean status;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Boolean getStatus() {
			return status;
		}

		public void setStatus(Boolean status) {
			this.status = status;
		}

		
		
	};
	
	@Autowired
	private OperatorsService service;
	
	@PostMapping("list")	
	@ApiOperation(value="获取运营商列表",httpMethod="POST", notes="获取运营商列表")
	public Result<PagerResult<BaseOperator>> list(@ApiParam  @RequestBody  QueryData queryData) throws ScanElectricityException{
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("查询运营商记录出错:"+e.getMessage(),e);
			return Result.fail("查询运营商记录出错:"+e.getMessage());
		}	
	}
	
	
	
	@PutMapping	
	@ApiOperation(value="保存新的运营商",httpMethod="PUT", notes="保存新的运营商")
	public Result<BaseOperator> saveNewItem(@RequestBody BaseOperator item) throws ScanElectricityException {
		
		try {			
			return Result.success(service.saveNew(item));
		}
		catch (ScanElectricityException e) {
			logger.error("保存新的运营商失败:"+e.getMessage(),e);
			return Result.fail(1, "保存新的运营商失败:"+e.getMessage());
		}
		
	}
	
	
	@PostMapping	
	@ApiOperation(value="保存运营商",httpMethod="PUT", notes="保存运营商")
	public Result<BaseOperator> saveItem(@RequestBody BaseOperator item) throws ScanElectricityException { 
		try {			
			
			return Result.success(service.saveItem(item));
		}
		catch (ScanElectricityException e) {
			logger.error("保存运营商失败:"+e.getMessage(),e);
			return Result.fail(1, "保存运营商失败:"+e.getMessage());
		}
	}
	
	
	@DeleteMapping("{id}")		
	@ApiOperation(value="删除运营商",httpMethod="POST", notes="删除运营商")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {
		
		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("删除供应商失败:"+e.getMessage(),e);
			return BaseResult.failResult(1, "删除供应商失败:"+e.getMessage());
		}
	}
	
}
