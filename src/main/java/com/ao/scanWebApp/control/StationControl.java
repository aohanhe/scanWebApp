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
@Api("վ��API")
public class StationControl {
	private Logger logger = LoggerFactory.getLogger(StationControl.class);
	
	@JpaQueryBean(entityClass = StationStationInfo.class,mainRootPath=QueryDslRootPaths.Root_Station)
	@ApiModel("��ѯ���ݶ���")
	public static class QueryData extends PageJpaQueryBean {
		@ApiModelProperty(name = "վ������", dataType = "String")
		private String name;		
		@ApiModelProperty(name = "��Ӫ��ID", dataType = "int")
		@EntityPath(name="operatorid",rootPath=QueryDslRootPaths.Root_Station)
		private Integer operatorId ;
		@ApiModelProperty(name = "��������", dataType = "String")
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
	@ApiOperation(value = "��ȡվ���б�", httpMethod = "POST", notes = "��ȡվ���б�")
	Result<PagerResult<StationStationInfo>> list(@ApiParam @RequestBody QueryData queryData) throws ScanElectricityException {
		try {
			var list = service.findAllByQueryBeanByPage(queryData);
			
			return Result.success(list);
		} catch (ScanElectricityException e) {
			logger.error("��ѯվ������:"+e.getMessage(),e);
			return Result.fail("��ѯվ������:"+e.getMessage());
		}	
	}
	

	
	@PutMapping	
	@ApiOperation(value = "�����µ�վ��", httpMethod = "PUT", notes = "�����µ�վ��")
	public Result<StationStationInfo> saveNewItem(@RequestBody StationStationInfo item)
			throws ScanElectricityException {

		try {
			return Result.success(service.saveNew(item));
		} catch (ScanElectricityException e) {
			logger.error("�����µ�վ��ʧ��", e);
			return Result.fail(1, "�����µ�վ��ʧ��");
		}

	}


	@PostMapping
	@ApiOperation(value = "����վ��", httpMethod = "PUT", notes = "����վ��")
	public Result<StationStationInfo> saveItem(@RequestBody StationStationInfo item)
			throws ScanElectricityException {
		try {

			return Result.success(service.saveItem(item));
		} catch (ScanElectricityException e) {
			logger.error("����վ��ʧ��", e);
			return Result.fail(1, "����վ��ʧ��");
		}
	}

	
	@DeleteMapping("{id}")
	@ApiOperation(value = "ɾ��վ��", httpMethod = "POST", notes = "ɾ��վ��")
	public BaseResult delete(@PathVariable("id") int id) throws ScanElectricityException {

		try {
			service.deleteItemById(id);
			return BaseResult.successResult();
		} catch (ScanElectricityException e) {
			logger.error("ɾ��վ��ʧ��", e);
			return BaseResult.failResult(1, "ɾ��վ��ʧ��");
		}
	}
	
	
	@PostMapping("listNear")	
	@ApiOperation(value = "��ȡ����վ���б�", httpMethod = "POST", notes = "��ȡ����վ���б�")
	Result<List<StationStationInfo>> list(@ApiParam @RequestBody StationMongoEntry.Pos pos) throws ScanElectricityException {
		
		try {
			if(pos.getLat()==0||pos.getLon()==0) throw new ScanElectricityException("���ṩҪ��ѯ������");
			
			return Result
					.success(service.listNearPosStations(pos.getLon(), pos.getLat()));
		} catch (Exception e) {
			logger.error("ȡ�ø���վ��ʧ��", e);
			return Result.fail(1, "ȡ�ø���վ��ʧ��");
		}
	}
	
	
	@PostMapping("freePlugerNmber/{stationId}")	
	@ApiOperation(value = "��ȡվ�����в�ͷ��", httpMethod = "POST", notes = "��ȡվ�����в�ͷ��")
	Result<Integer> StationFreePlugerNumber(@ApiParam @PathVariable int stationId) {
		return Result.success(service.FreePlugerNumber(stationId));
	}


}
