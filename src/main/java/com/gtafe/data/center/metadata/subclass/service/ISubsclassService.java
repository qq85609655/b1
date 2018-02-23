package com.gtafe.data.center.metadata.subclass.service;

import java.util.List;
import java.util.Map;

import com.gtafe.data.center.metadata.subclass.vo.SubclassByclassVO;
import com.gtafe.data.center.metadata.subclass.vo.SubclassVO;

/***
 * 
 * ClassName: 数据子类业务层 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月18日 下午5:16:57 <br/>
 * 
 * @author ken.zhang
 * @version
 */
@SuppressWarnings("rawtypes")
public interface ISubsclassService {
	/**
	 * 
	 * saveSubclassInfo:新增子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @since JDK 1.7
	 */
	public void saveSubclassInfo(SubclassVO subclassVO);

	/**
	 * 
	 * saveSubclassInfo:修改子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @since JDK 1.7
	 */
	public void updateSubclassInfo(SubclassVO subclassVO);

	/**
	 * 
	 * saveSubclassInfo:删除子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @since JDK 1.7
	 */
	public void deleteSubclassInfo(String subclassCode, String classCode);

	/**
	 * 
	 * querySubclassPageInfo:分页查询数据子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subsetCode
	 *            子集码
	 * @param classCode
	 *            类码
	 * @param codeOrName
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @since JDK 1.7
	 */
	public List<SubclassVO> querySubclassPageInfo(int sourceId,
			String subsetCode, String classCode, String codeOrName,
			String orderName, int pageNum, int pageSize);

	/**
	 * 
	 * saveSubclassInfo:根据子类码查询信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @since JDK 1.7
	 */
	public SubclassVO querybySubclassCode(String subclassCode, String classCode);

	/**
	 * 
	 * citeExists:查看该表是否存在. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassTableName
	 * @return
	 * @since JDK 1.7
	 */
	public int isTableExists(String subclassTableName);

	/**
	 * 
	 * citeExists:查看该表是否存在引用关系. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassTableName
	 * @return
	 * @since JDK 1.7
	 */
	public int isSubclassCiteRelation(String subclassCode);

	/**
	 * 
	 * addSubclassBatch:新增多条数据子类. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param classCode
	 *            listSubclassVO
	 * @return
	 * @since JDK 1.7
	 */
	boolean addSubclassBatch(String classCode,
			List<SubclassByclassVO> listSubclassByClassVO);

	/**
	 * 
	 * updateComment:修改表注释. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @since JDK 1.7
	 */
	public void updateComment(Map<String, Object> params);
}
