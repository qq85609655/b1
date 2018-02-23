/** 
 * Project Name:gtacore 
 * File Name:SubsetServiceImpl.java 
 * Package Name:com.gtafe.data.center.meta.service.impl 
 * Date:2017年7月18日上午10:40:07 
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */
package com.gtafe.data.center.metadata.subset.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.metadata.subset.mapper.SubsetMapper;
import com.gtafe.data.center.metadata.subset.service.ISubsetService;
import com.gtafe.data.center.metadata.subset.vo.SubsetVO;
import com.gtafe.data.center.metadata.subset.vo.Subsets;
import com.gtafe.framework.base.service.BaseService;

@Service
public class SubsetServiceImpl extends BaseService implements ISubsetService {

	@Resource
	private SubsetMapper subsetMapper;

	@Override
	public List<SubsetVO> query(String sourceId, String codeOrName, int pageNum, int pageSize) {
		return this.subsetMapper.query(sourceId, codeOrName, pageNum, pageSize);
	}

	@Override
	public SubsetVO queryByCode(int sourceid, String code) {
		return this.subsetMapper.queryByCode(sourceid, code);
	}

	@Override
	public void insert(SubsetVO subsetVO) {
		this.subsetMapper.insert(subsetVO);
	}

	@Override
	public void insertBatch(Subsets subsets) {
		this.subsetMapper.insertBatch(subsets);
	}
	
	@Override
	public void update(SubsetVO subsetVO) {
		SubsetVO s = this.queryByCode(subsetVO.getSourceid(), subsetVO.getCode());
		if(s == null){
			this.insert(subsetVO);
		}else{
			// TODO 缺少引用的判断逻辑
			this.subsetMapper.update(subsetVO);
		}
	}

	@Override
	public void delete(int sourceid, String code) {
		this.subsetMapper.delete(sourceid, code);
	}
	
	@Override
	public int queryClass(String code){
		return this.subsetMapper.queryClass(code);
	}

}
