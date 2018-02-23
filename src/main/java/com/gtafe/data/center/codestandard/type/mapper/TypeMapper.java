package com.gtafe.data.center.codestandard.type.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.codestandard.type.vo.ByBatchTypeVO;
import com.gtafe.data.center.codestandard.type.vo.TypeVO;

/**
 * 
 * ClassName: TypeMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:54:12 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
public interface TypeMapper {
	/**
	 * 
	 * saveSetInfo:新增代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void saveTypeInfo(TypeVO typeVO);

	/**
	 * 
	 * saveSetInfo:修改代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void updateTypeInfo(TypeVO typeVO);

	/**
	 * 
	 * saveSetInfo:删除代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void deleteTypeInfo(@Param("setId") int setId,
			@Param("typeId") int typeId);

	/**
	 * 
	 * saveSetInfo:根据setId获取所有代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public List<TypeVO> queryTypeInfo(@Param("sourceId") int sourceId,
			@Param("setId") int setId);

	/**
	 * 
	 * querySetById:获取代码类型信息. <br/>
	 * 
	 * @historys
	 * @author ken.zhang
	 * @param setId
	 * @return
	 * @since JDK 1.7
	 */
	public TypeVO queryTypeInfoByCode(@Param("setId") int setId,
			@Param("typeId") int typeId, @Param("code") String code);

	/**
	 * 
	 * isCiteRelation:是否存在引用关系. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public int isTypeCiteRelation(@Param("typeId") int typeId);

	/**
	 * 
	 * saveTypeBatch:批量新增代码类型. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setId
	 * @return
	 * @since JDK 1.7
	 */
	public boolean saveTypeBatch(@Param("setId") int setId,
			@Param("batchTypeVOs") List<ByBatchTypeVO> batchTypeVOs);
}
