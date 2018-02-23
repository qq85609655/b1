package com.gtafe.data.center.metadata.subclass.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.metadata.subclass.mapper.SubclassMapper;
import com.gtafe.data.center.metadata.subclass.service.ISubsclassService;
import com.gtafe.data.center.metadata.subclass.vo.SubclassByclassVO;
import com.gtafe.data.center.metadata.subclass.vo.SubclassVO;
import com.gtafe.framework.base.service.BaseService;

/**
 * 
 * ClassName: SubclassServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月21日 下午2:28:57 <br/>
 * 
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@Service
@SuppressWarnings("rawtypes")
public class SubclassServiceImpl extends BaseService implements
		ISubsclassService {
	@Resource
	private SubclassMapper subclassMapper;

	/**
	 * 
	 * TODO 新增数据子类信息（可选）.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#saveSubclassInfo(com.gtafe.data.center.metadata.subclass.vo.SubclassVO)
	 */
	public void saveSubclassInfo(SubclassVO subclassVO) {
		this.subclassMapper.saveSubclassInfo(subclassVO);
	}

	/**
	 * 
	 * TODO 修改子类信息（可选）.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#updateSubclassInfo(com.gtafe.data.center.metadata.subclass.vo.SubclassVO)
	 */
	public void updateSubclassInfo(SubclassVO subclassVO) {
		SubclassVO subclass = this.querybySubclassCode(
				subclassVO.getSubclassCode(), subclassVO.getClassCode());
		// 存在该子类进行修改，不存在进行新增操作
		if (subclass == null) {
			this.saveSubclassInfo(subclassVO);
		} else {
			subclassMapper.updateSubclassInfo(subclassVO);
			// 判断是否存在改表，如果存在：修改子类名字的同时，修改该表的注释
			if (this.isTableExists(subclassVO.getSubclassTableName()) > 0) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("subclassTableName",
						subclassVO.getSubclassTableName());
				params.put("subclassComment", subclassVO.getSubclassComment());
				this.updateComment(params);
			}
		}
	}

	/**
	 * 
	 * TODO 删除数据子类信息
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#deleteSubclassInfo(com.gtafe.data.center.metadata.subclass.vo.SubclassVO)
	 */
	public void deleteSubclassInfo(String subclassCode, String sclassCode) {
		this.subclassMapper.deleteSubclassInfo(subclassCode, sclassCode);
	}

	/**
	 * 
	 * TODO 分页查询子类数据.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#querySubclassPageInfo(java.lang.String,
	 *      java.lang.String, java.lang.String, int, int)
	 */
	public List<SubclassVO> querySubclassPageInfo(int sourceId,
			String subsetCode, String classCode, String codeOrName,
			String orderName, int pageNum, int pageSize) {
		return subclassMapper.querySubclassPageInfo(sourceId, subsetCode,
				classCode, codeOrName, orderName, pageNum, pageSize);
	}

	/**
	 * 
	 * TODO 根据code码查询数据子类.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#querybySubclassCode(com.gtafe.data.center.metadata.subclass.vo.SubclassVO)
	 */
	public SubclassVO querybySubclassCode(String subclassCode, String classCode) {
		return this.subclassMapper.querybySubclassCode(subclassCode, classCode);
	}

	/**
	 * 
	 * TODO 判断 表是否存在.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#isTableExists(java.lang.String)
	 */
	public int isTableExists(String subclassTableName) {
		return this.subclassMapper.isTableExists(subclassTableName);
	}

	/**
	 * 
	 * TODO 查看该表是否存在引用关系.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#citeExists(java.lang.String)
	 */
	public int isSubclassCiteRelation(String subclassCode) {
		return this.subclassMapper.isSubclassCiteRelation(subclassCode);
	}

	/**
	 * 
	 * TODO 新增多条子类数据.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#addSubclassBatch(java.util.List)
	 */
	public boolean addSubclassBatch(String classCode,
			List<SubclassByclassVO> listSubclassByClassVO) {
		return this.subclassMapper.addSubclassBatch(classCode,
				listSubclassByClassVO);
	}

	/**
	 * 
	 * TODO 修改表注释.
	 * 
	 * @see com.gtafe.data.center.metadata.subclass.service.ISubsclassService#updateComment(java.lang.String)
	 */
	public void updateComment(Map<String, Object> params) {
		this.subclassMapper.updateComment(params);
	}

}
