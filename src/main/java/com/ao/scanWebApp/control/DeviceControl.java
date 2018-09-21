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

import com.ao.scanElectricityBis.entity.StationDevice;
import com.ao.scanElectricityBis.service.DeviceService;
import com.ao.scanWebApp.BaseResult;
import com.ao.scanWebApp.Result;


import ao.jpaQueryHelper.PageJpaQueryBean;
import ao.jpaQueryHelper.PagerResult;
import ao.jpaQueryHelper.annotations.EntityPath;
import ao.jpaQueryHelper.annotations.JpaQueryBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/device")
@Api("设备API")
public class DeviceControl {

	private Logger logger = LoggerFactory.getLogger(DeviceControl.class);
	@Autowired
	private DeviceService service;

	@ApiModel("查询数据对象")
	@JpaQueryBean(entityClass = StationDevice.class, mainRootPath = QueryDslRootPaths.Root_Device)
	public static class QueryData extends PageJpaQueryBean {
		@ApiModelProperty(name = "站场ID", dataType = "int")
		private Integer stationId;

		@ApiModelProperty(name = "运营商ID", dataType = "int")
		@EntityPath(name = "operatorid", rootPath = QueryDslRootPaths.Root_Station)
		private Integer operatorId;

		@EntityPath(rootPath = QueryDslRootPaths.Root_Station)
		@ApiModelProperty(name = "地区code", dataType = "String")
		private String regionCode;
		@ApiModelProperty(name = "状态", dataType = "int")
		private Integer status = null;

		public String getRegionCode() {
			return regionCode;
		}

		public void setRegionCode(String regionCode) {
			this.regionCode = regionCode;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Integer getStationId() {
			return stationId;
		}

		public void setStationId(Integer stationId) {
			this.stationId = stationId;
		}

		public Integer getOperatorId() {
			return operatorId;
		}

		public void setOperatorId(Integer operatorId) {
			this.operatorId = operatorId;
		}
	}

	@PostMapping("list")
	@ApiOperation(value = "获取站场设备列表", httpMethod = "POST", notes = "获取站场设备列表")
	public Result<PagerResult<StationDevice>> list(@ApiParam @RequestBody QueryData queryData)
			throws ScanElectricityException {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);

			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("获取站场设备列表:" + e.getMessage(), e);
			return Result.fail("获取站场设备列表:" + e.getMessage());
		}
	}

	@PutMapping
	@ApiOperation(value = "保存新的设备", httpMethod = "PUT", notes = "保存新的设备")
	public Result<StationDevice> saveNewItem(@RequestBody StationDevice item) throws ScanElectricityException {
		try {
			return Result.success(service.saveNew(item));
		} catch (ScanElectricityException e) {
			logger.error("保存新的设备失败", e);
			return Result.fail(1, "保存新的设备失败");
		}
	}
	
	@PostMapping
	public Result<StationDevice> saveItem(@RequestBody StationDevice item) throws ScanElectricityException { 
		try {			
			
			return Result.success(service.saveItem(item));
		} catch (ScanElectricityException e) {
			logger.error("保存设备失败",e);
			return Result.fail(1, "保存设备失败");
		}
	}
	
	
	@DeleteMapping("{id}")		
	@ApiOperation(value="删除设备",httpMethod="POST", notes="删除设备")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {
		
		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("删除设备失败",e);
			return BaseResult.failResult(1, "删除设备失败");
		}
	}

}
