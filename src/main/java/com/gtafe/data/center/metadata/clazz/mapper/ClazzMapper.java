package com.gtafe.data.center.metadata.clazz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.metadata.clazz.vo.ClazzBySubsetVO;
import com.gtafe.data.center.metadata.clazz.vo.ClazzVO;
import com.gtafe.framework.base.mapper.BaseMapper;

public interface ClazzMapper extends BaseMapper {
	List<ClazzVO>  queryClassList(@Param("sourceId")int sourceId, @Param("subsetCode") String subsetCode,@Param("classCodeOrclassName") String classCodeOrclassName, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);
	boolean classDelete(@Param("subsetCode") String subsetCode, @Param("classCode") String classCode);
	
	boolean addClass(ClazzVO clazzVO);
	
	boolean updateClass(ClazzVO clazzVO);
	 
	ClazzVO queryByCode(@Param("subsetCode") String subsetCode, @Param("classCode") String classCode);
	
	int querySubClass(@Param("classCode") String classCode);
	
	boolean addClassBatchBysubset(@Param("subsetCode") String subsetCode,@Param("listClazzBySubsetVO") List<ClazzBySubsetVO> listClazzBySubsetVO);
}
