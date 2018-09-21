package com.ao.scanWebApp.control;

import java.util.List;
import java.util.stream.Collectors;

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

import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.QStationPlugInfo;

import com.ao.scanElectricityBis.entity.StationPlugInfo;
import com.ao.scanElectricityBis.service.PlugInfoService;
import com.ao.scanWebApp.BaseResult;
import com.ao.scanWebApp.Result;



import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/pluginfo")
public class PlugInfoControl {
	private static Logger logger=LoggerFactory.getLogger(PlugInfoControl.class);
	
	@Autowired
	private PlugInfoService service;
	
	
	@PostMapping("list/{deviceId}")
	@ApiOperation(value = "获取站场设备列表", httpMethod = "POST", notes = "获取站场设备列表")
	public Result<List<StationPlugInfo>> list(@ApiParam @PathVariable int deviceId)
			throws ScanElectricityException {
		try {
			var pluginfo=QStationPlugInfo.stationPlugInfo;
			var list = service.findAllItems(query->{
				return query.where(pluginfo.deviceid.eq(deviceId));
			}).collect(Collectors.toList()).block();

			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("获取站场设备列表:" + e.getMessage(), e);
			return Result.fail("获取站场设备列表:" + e.getMessage());
		}
	}
	
	
	@PutMapping
	@ApiOperation(value = "保存新的插头", httpMethod = "PUT", notes = "保存新的插头")
	public Result<StationPlugInfo> saveNewItem(@RequestBody StationPlugInfo item) throws ScanElectricityException {
		try {
			return  Result.success(service.saveNew(item));
			
		} catch (ScanElectricityException e) {
			logger.error("保存新的设备失败", e);
			return Result.fail(1, "保存新的设备失败");
		}
	}
	
	@PostMapping
	public Result<StationPlugInfo> saveItem(@RequestBody StationPlugInfo item) throws ScanElectricityException { 
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
