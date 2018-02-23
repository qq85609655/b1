/** 
 * Project Name:gtacore 
 * File Name:SubsetServiceImpl.java 
 * Package Name:com.gtafe.data.center.meta.service.impl 
 * Date:2017年7月18日上午10:40:07 
 * Copyright (c) 2017, GTA All Rights Reserved. 
 * 
 */  
package com.gtafe.data.center.metadata.clazz.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.metadata.clazz.mapper.ClazzMapper;
import com.gtafe.data.center.metadata.clazz.service.IClazzService;
import com.gtafe.data.center.metadata.clazz.vo.ClazzBySubsetVO;
import com.gtafe.data.center.metadata.clazz.vo.ClazzVO;
import com.gtafe.framework.base.service.BaseService;

@Service
public class ClazzServiceImpl extends BaseService implements IClazzService {
	
	@Resource
	private ClazzMapper clazzMapper;

	
	/**
	 * 
	 * queryClassList:分页查询类信息(跟初始化类用同一个方法). <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode
	 *            子集编号
	 * @param classCodeOrclassName
	 *            类编号或类中文名
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            每一页数据数量
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public List<ClazzVO> queryClassList(int sourceId, String subsetCode,String classCodeOrclassName, int pageNum, int pageSize) {
		return this.clazzMapper.queryClassList(sourceId, subsetCode,classCodeOrclassName, pageNum, pageSize);
	}
	
	/**
	 * 
	 * classDelete:删除类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode
	 * @param classCode
	 * @return
	 * @since JDK 1.7
	 */
	
	@Override
	public boolean classDelete(String subsetCode, String classCode) {
		return this.clazzMapper.classDelete(subsetCode, classCode);
	}
	
	/**
	 * 
	 * updateClass:修改类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode
	 * @param classCode
	 * @param clazzVO
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public boolean updateClass(ClazzVO clazzVO) {
		ClazzVO s = this.queryByCode(clazzVO.getSubsetCode(), clazzVO.getClassCode());
		if(s == null){
			return this.addClass(clazzVO);
		}else{
			return this.clazzMapper.updateClass(clazzVO);
		}
	}
	 
	/**
	 * 
	 * addClass:新增类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param clazzVO
	 * @return
	 * @since JDK 1.7
	 */
	
	@Override
	public boolean addClass(ClazzVO clazzVO) {
		return this.clazzMapper.addClass(clazzVO);
	}
	
	/**
	 * 
	 * queryByCode:根据子集编号和类编号到类表查询是否存在该记录. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param classCode
	 * @return
	 * @since JDK 1.7
	 */
	
	@Override
	public ClazzVO queryByCode(String subsetCode, String classCode) {
		return this.clazzMapper.queryByCode(subsetCode, classCode);
	}
	
	/**
	 * 
	 * querySubClass:根据类编号到子类表查询是否有挂载子类. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param classCode
	 * @return
	 * @since JDK 1.7
	 */
	
	@Override
	public int  querySubClass(String classCode){
		return this.clazzMapper.querySubClass(classCode);
	}
	
	/**
	 * 
	 * addClassBatchBysubset:按子集编号批量新增类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode listClazzBySubsetVO
	 * @return
	 * @since JDK 1.7
	 */
	@Override
	public boolean addClassBatchBysubset(String subsetCode,List<ClazzBySubsetVO> listClazzBySubsetVO) {
		return this.clazzMapper.addClassBatchBysubset(subsetCode,listClazzBySubsetVO);
	}
	
}
