package com.ao.scanWebApp.control;

import java.util.List;

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
import com.ao.scanElectricityBis.entity.StationMongoEntry;
import com.ao.scanElectricityBis.entity.StationStationInfo;
import com.ao.scanElectricityBis.service.StationService;
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
@RequestMapping("/api/station")
@Api("站场API")
public class StationControl {
	private Logger logger = LoggerFactory.getLogger(StationControl.class);
	
	@JpaQueryBean(entityClass = StationStationInfo.class,mainRootPath=QueryDslRootPaths.Root_Station)
	@ApiModel("查询数据对象")
	public static class QueryData extends PageJpaQueryBean {
		@ApiModelProperty(name = "站场名称", dataType = "String")
		private String name;		
		@ApiModelProperty(name = "运营商ID", dataType = "int")
		@EntityPath(name="operatorid",rootPath=QueryDslRootPaths.Root_Station)
		private Integer operatorId ;
		@ApiModelProperty(name = "地区编码", dataType = "String")
		@EntityPath(name="regioncode",rootPath=QueryDslRootPaths.Root_Station)
		private String regionCode;
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getOperatorId() {
			return operatorId;
		}
		public void setOperatorId(Integer operatorId) {
			this.operatorId = operatorId;
		}
		public String getRegionCode() {
			return regionCode;
		}
		public void setRegionCode(String regionCode) {
			this.regionCode = regionCode;
		}
	}
	
	@Autowired
	private StationService service;
	
	@PostMapping("list")	
	@ApiOperation(value = "获取站场列表", httpMethod = "POST", notes = "获取站场列表")
	Result<PagerResult<StationStationInfo>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("查询站场出错:"+e.getMessage(),e);
			return Result.fail("查询站场出错:"+e.getMessage());
		}	
	}
	

	
	@PutMapping	
	@ApiOperation(value = "保存新的站场", httpMethod = "PUT", notes = "保存新的站场")
	public Result<StationStationInfo> saveNewItem(@RequestBody StationStationInfo item)
			throws ScanElectricityException {

		try {
			return Result.success(service.saveNew(item));
		} catch (ScanElectricityException e) {
			logger.error("保存新的站场失败", e);
			return Result.fail(1, "保存新的站场失败");
		}

	}


	@PostMapping
	@ApiOperation(value = "保存站场", httpMethod = "PUT", notes = "保存站场")
	public Result<StationStationInfo> saveItem(@RequestBody StationStationInfo item)
			throws ScanElectricityException {
		try {

			return Result.success(service.saveItem(item));
		} catch (ScanElectricityException e) {
			logger.error("保存站场失败", e);
			return Result.fail(1, "保存站场失败");
		}
	}

	
	@DeleteMapping("{id}")
	@ApiOperation(value = "删除站场", httpMethod = "POST", notes = "删除站场")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {

		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("删除站场失败", e);
			return BaseResult.failResult(1, "删除站场失败");
		}
	}
	
	
	@PostMapping("listNear")	
	@ApiOperation(value = "获取附近站场列表", httpMethod = "POST", notes = "获取附近站场列表")
	Result<List<StationStationInfo>> list(@ApiParam @RequestBody StationMongoEntry.Pos pos) throws ScanElectricityException {
		
		try {
			if(pos.getLat()==0||pos.getLon()==0) throw new ScanElectricityException("请提供要查询的坐标");
			
			return Result
					.success(service.listNearPosStations(pos.getLon(), pos.getLat()));
		} catch (Exception e) {
			logger.error("取得附近站场失败", e);
			return Result.fail(1, "取得附近站场失败");
		}
	}
	
	
	@PostMapping("freePlugerNmber/{stationId}")	
	@ApiOperation(value = "获取站场空闲插头数", httpMethod = "POST", notes = "获取站场空闲插头数")
	Result<Integer> StationFreePlugerNumber(@ApiParam @PathVariable int stationId) {
		return Result.success(service.FreePlugerNumber(stationId));
	}


}
