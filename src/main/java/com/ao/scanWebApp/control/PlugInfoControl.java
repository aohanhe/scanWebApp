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
	@ApiOperation(value = "��ȡվ���豸�б�", httpMethod = "POST", notes = "��ȡվ���豸�б�")
	public Result<List<StationPlugInfo>> list(@ApiParam @PathVariable int deviceId)
			throws ScanElectricityException {
		try {
			var pluginfo=QStationPlugInfo.stationPlugInfo;
			var list = service.findAllItems(query->{
				return query.where(pluginfo.deviceid.eq(deviceId));
			}).collect(Collectors.toList()).block();

			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("��ȡվ���豸�б�:" + e.getMessage(), e);
			return Result.fail("��ȡվ���豸�б�:" + e.getMessage());
		}
	}
	
	
	@PutMapping
	@ApiOperation(value = "�����µĲ�ͷ", httpMethod = "PUT", notes = "�����µĲ�ͷ")
	public Result<StationPlugInfo> saveNewItem(@RequestBody StationPlugInfo item) throws ScanElectricityException {
		try {
			return  Result.success(service.saveNew(item));
			
		} catch (ScanElectricityException e) {
			logger.error("�����µ��豸ʧ��", e);
			return Result.fail(1, "�����µ��豸ʧ��");
		}
	}
	
	@PostMapping
	public Result<StationPlugInfo> saveItem(@RequestBody StationPlugInfo item) throws ScanElectricityException { 
		try {			
			
			return Result.success(service.saveItem(item));
		} catch (ScanElectricityException e) {
			logger.error("�����豸ʧ��",e);
			return Result.fail(1, "�����豸ʧ��");
		}
	}
	
	
	@DeleteMapping("{id}")		
	@ApiOperation(value="ɾ���豸",httpMethod="POST", notes="ɾ���豸")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {
		
		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("ɾ���豸ʧ��",e);
			return BaseResult.failResult(1, "ɾ���豸ʧ��");
		}
	}
	

}
