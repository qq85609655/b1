package com.gtafe.data.center.codestandard.code.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.codestandard.code.mapper.CodeMapper;
import com.gtafe.data.center.codestandard.code.service.ICodeService;
import com.gtafe.data.center.codestandard.code.vo.ByBatchCodeVO;
import com.gtafe.data.center.codestandard.code.vo.CodeVO;

/**
 * 
 * ClassName: CodeServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月26日 下午5:12:56 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@Service
public class CodeServiceImpl implements ICodeService {
	@Resource
	private CodeMapper codeMapper;

	/**
	 * 
	 * TODO 新增代码信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.code.service.ICodeService#saveCodeInfo(com.gtafe.data.center.codestandard.code.vo.CodeVO)
	 */
	public void saveCodeInfo(CodeVO codeVO) {
		this.codeMapper.saveCodeInfo(codeVO);
	}

	/**
	 * 
	 * TODO 修改代码信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.code.service.ICodeService#updateCodeInfo(com.gtafe.data.center.codestandard.code.vo.CodeVO)
	 */
	public void updateCodeInfo(CodeVO codeVO) {
		CodeVO vo = this.queryCodeInfoByCode(codeVO.getTypeId(),
				codeVO.getCode());
		// 如果不存在改代码信息执行新增操作
		if (vo == null) {
			this.saveCodeInfo(codeVO);
		}
		this.codeMapper.updateCodeInfo(codeVO);
	}

	/**
	 * 
	 * TODO 删除代码信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.code.service.ICodeService#deleteCodeInfo(com.gtafe.data.center.codestandard.code.vo.CodeVO)
	 */
	public void deleteCodeInfo(int typeId, int codeId) {
		this.codeMapper.deleteCodeInfo(typeId, codeId);
	}

	/**
	 * 
	 * TODO 根据typeId、code查询代码信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.code.service.ICodeService#queryCodeByCode(int,
	 *      java.lang.String)
	 */
	public CodeVO queryCodeInfoByCode(int typeId, String code) {
		return this.codeMapper.queryCodeInfoByCode(typeId, code);
	}

	/**
	 * 
	 * TODO 分页查询代码信息（可选）.
	 * 
	 * @see com.gtafe.data.center.codestandard.code.service.ICodeService#queryCodePageInfo(java.lang.String,
	 *      int, int)
	 */
	public List<CodeVO> queryCodePageInfo(String codeOrName, int sourceid,
			int setId, int typeId, String orderName, int pageNum, int pageSize) {
		return this.codeMapper.queryCodePageInfo(codeOrName, sourceid, setId,
				typeId, orderName, pageNum, pageSize);
	}

	/**
	 * 
	 * TODO 批量新增代码信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.code.service.ICodeService#saveCodeBatch(int,
	 *      java.util.List)
	 */
	public boolean saveCodeBatch(int typeId, List<ByBatchCodeVO> batchCodeVOs) {
		return this.codeMapper.saveCodeBatch(typeId, batchCodeVOs);
	}

}
