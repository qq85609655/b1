package com.gtafe.data.center.codestandard.code.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.codestandard.code.vo.ByBatchCodeVO;
import com.gtafe.data.center.codestandard.code.vo.CodeVO;

/**
 * 
 * ClassName: CodeMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:38:52 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
public interface CodeMapper {
	/**
	 * 
	 * saveCodeInfo:新增代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void saveCodeInfo(CodeVO codeVO);

	/**
	 * 
	 * updateCodeInfo:修改代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void updateCodeInfo(CodeVO codeVO);

	/**
	 * 
	 * deleteCodeInfo:删除代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @since JDK 1.7
	 */
	public void deleteCodeInfo(@Param("typeId") int typeId,
			@Param("codeId") int codeId);

	/**
	 * 
	 * queryCodeByCode:根据typeId、code查询代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setId
	 * @return
	 * @since JDK 1.7
	 */
	public CodeVO queryCodeInfoByCode(@Param("typeId") int typeId,
			@Param("code") String code);

	/**
	 * 
	 * querySubclassPageInfo:分页查询代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param codeOrName名字或者编码
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @since JDK 1.7
	 */
	public List<CodeVO> queryCodePageInfo(
			@Param("codeOrName") String codeOrName,
			@Param("sourceid") int sourceid, @Param("setId") int setId,
			@Param("typeId") int typeId, @Param("orderName") String orderName,
			@Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

	/**
	 * 
	 * insertSetBatch:批量新增代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public boolean saveCodeBatch(@Param("typeId") int typeId,
			@Param("batchCodeVOs") List<ByBatchCodeVO> batchCodeVOs);

}
