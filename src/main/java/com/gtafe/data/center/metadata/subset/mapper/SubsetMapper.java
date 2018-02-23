package com.gtafe.data.center.metadata.subset.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.metadata.subset.vo.SubsetVO;
import com.gtafe.data.center.metadata.subset.vo.Subsets;
import com.gtafe.framework.base.mapper.BaseMapper;

public interface SubsetMapper extends BaseMapper {
	List<SubsetVO>  query(@Param("sourceId") String sourceId, @Param("codeOrName") String codeOrName, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);
	
	void insert(SubsetVO subsetVO);
	
	void insertBatch(@Param("subsets")Subsets subsets);
	
	void update(SubsetVO subsetVO);
	
	void delete(@Param("sourceid") int sourceid, @Param("code") String code);
	
	SubsetVO queryByCode(@Param("sourceid") int sourceid, @Param("code") String code);
	
	int queryClass(@Param("code") String code);
}
