package com.gtafe.data.center.codestandard.type.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.codestandard.set.mapper.SetMapper;
import com.gtafe.data.center.codestandard.set.vo.SetVO;
import com.gtafe.data.center.codestandard.type.mapper.TypeMapper;
import com.gtafe.data.center.codestandard.type.service.ITypeService;
import com.gtafe.data.center.codestandard.type.vo.ByBatchTypeVO;
import com.gtafe.data.center.codestandard.type.vo.TypeVO;

@Service
public class TypeServiceImpl implements ITypeService {
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private SetMapper setMapper;

	/**
	 * 
	 * TODO 新增代码类型信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#saveTypeInfo(com.gtafe.data.center.codestandard.type.vo.TypeVO)
	 */
	public void saveTypeInfo(TypeVO typeVO) {
		this.typeMapper.saveTypeInfo(typeVO);
	}

	/**
	 * 
	 * TODO 修改代码类型信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#updateTypeInfo(com.gtafe.data.center.codestandard.type.vo.TypeVO)
	 */
	public void updateTypeInfo(TypeVO typeVO) {
	    System.out.println(typeVO.toString());
	    if(typeVO.getTypeId()==0) {
	        //此時是需要修改set 數據
	        SetVO setVO=new SetVO();
	        setVO.setCode(typeVO.getCode());
	        setVO.setName(typeVO.getName());
	        this.setMapper.updateSetInfoByCode(setVO);
	    }else {
	//	TypeVO vo = this.queryTypeInfoByCode(typeVO.getSetId(),
	//			typeVO.getTypeId(), typeVO.getCode());
		
		// 不存在改类型进行新增操作
	//	if (vo == null) {
			//this.saveTypeInfo(typeVO);
	//	}
		this.typeMapper.updateTypeInfo(typeVO);
	    }
	}

	/**
	 * 
	 * TODO 删除代码类型信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#deleteTypeInfo(com.gtafe.data.center.codestandard.type.vo.TypeVO)
	 */
	public void deleteTypeInfo(int setId, int typeId) {
		this.typeMapper.deleteTypeInfo(setId, typeId);
	}

	/**
	 * 
	 * TODO 根据setId获取所有代码类型信息
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#queryTypeInfo(int)
	 */
	public List<TypeVO> queryTypeInfo(int sourceId, int setId) {
		return this.typeMapper.queryTypeInfo(sourceId, setId);
	}

	/**
	 * 
	 * TODO 获取代码类型信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#queryTypeInfoById(int,
	 *      int)
	 */
	public TypeVO queryTypeInfoByCode(int setId, int typeId, String code) {
		return this.typeMapper.queryTypeInfoByCode(setId, typeId, code);
	}

	/**
	 * 
	 * TODO 是否存在引用关系.
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#isCiteRelation(int)
	 */
	public int isTypeCiteRelation(int typeId) {
		return this.typeMapper.isTypeCiteRelation(typeId);
	}

	/**
	 * 
	 * TODO 批量新增代码类型信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.type.service.ITypeService#saveTypeBatch(int,
	 *      java.util.List)
	 */
	public boolean saveTypeBatch(int setId, List<ByBatchTypeVO> batchTypeVOs) {
		return this.typeMapper.saveTypeBatch(setId, batchTypeVOs);
	}

}
