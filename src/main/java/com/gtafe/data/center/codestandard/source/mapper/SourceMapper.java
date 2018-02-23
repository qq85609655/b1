package com.gtafe.data.center.codestandard.source.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.codestandard.source.vo.SourceVO;

/**
 * 
 * ClassName: SourceMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:51:59 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
public interface SourceMapper {
	/**
	 * 
	 * saveSourceInfo:新增代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void saveSourceInfo(SourceVO sourceVO);

	/**
	 * 
	 * updateSourceInfo:修改代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void updateSourceInfo(SourceVO sourceVO);

	/**
	 * 
	 * deleteSourceInfo:删除代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void deleteSourceInfo(@Param("sourceid") int sourceid,
			@Param("code") String code);

	/**
	 * 
	 * querySourceInfo:获取所有代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public List<SourceVO> querySourceInfoAll();

	/**
	 * 
	 * querySourceInfoByCode:获取代码来源信息. <br/>
	 * 
	 * @historys
	 * @author ken.zhang
	 * @param setId
	 * @return
	 * @since JDK 1.7
	 */
	public SourceVO querySourceInfoByCode(@Param("sourceid") int sourceid,
			@Param("code") String code);

	/**
	 * 
	 * isSourceCiteRelation:是否存在引用关系. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public int isSourceCiteRelation(@Param("sourceid") int sourceid);

	/**
	 * 
	 * queryTreeInfo:初始显示树形菜单. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public List<SourceVO> queryTreeInfo(@Param("sourceid") int sourceid);
}
