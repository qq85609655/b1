package com.gtafe.data.center.codestandard.set.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.codestandard.set.vo.ByBatchSetVO;
import com.gtafe.data.center.codestandard.set.vo.SetVO;

/**
 * 
 * ClassName: SetMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:40:50 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
public interface SetMapper {
	/**
	 * 
	 * saveSetInfo:新增子集信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void saveSetInfo(SetVO setVO);

	/**
	 * 
	 * saveSetInfo:修改子集信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void updateSetInfo(SetVO setVO);

	/**
	 * 
	 * saveSetInfo:删除子集信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void deleteSetInfo(@Param("setId") int setId,
			@Param("code") String code);

	/**
	 * 
	 * saveSetInfo:根据sourceid获取所有子集信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public List<SetVO> querySetInfo(@Param("sourceid") int sourceid);

	/**
	 * 
	 * querySetById:根据id查询子集代码. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setId
	 * @return
	 * @since JDK 1.7
	 */
	public SetVO querySetByCode(@Param("sourceid") int sourceid,
			@Param("setId") int setId, @Param("code") String code);

	/**
	 * 
	 * isCiteRelation:是否存在引用关系. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public int isSetCiteRelation(@Param("setId") int setId);

	/**
	 * 
	 * insertSetBatch:多条子集新增. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public boolean saveSetBatch(@Param("sourceid") int sourceid,
			@Param("batchSetVOs") List<ByBatchSetVO> batchSetVOs);

    public void updateSetInfoByCode(SetVO setVO);

}
