package com.gtafe.data.center.codestandard.code.service;

import java.util.List;

import com.gtafe.data.center.codestandard.code.vo.ByBatchCodeVO;
import com.gtafe.data.center.codestandard.code.vo.CodeVO;

/**
 * 
 * ClassName: ICodeService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:40:24 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */

public interface ICodeService {
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
	public void deleteCodeInfo(int typeId, int codeId);

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
	public CodeVO queryCodeInfoByCode(int typeId, String code);

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
	public List<CodeVO> queryCodePageInfo(String codeOrName, int sourceid,
			int setId, int typeId, String orderName, int pageNum, int pageSize);

	/**
	 * 
	 * insertSetBatch:批量新增代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	public boolean saveCodeBatch(int typeId, List<ByBatchCodeVO> batchCodeVOs);
}
