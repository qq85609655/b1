package com.gtafe.data.center.codestandard.source.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.codestandard.source.mapper.SourceMapper;
import com.gtafe.data.center.codestandard.source.service.ISourceService;
import com.gtafe.data.center.codestandard.source.vo.SourceVO;

/**
 * 
 * ClassName: SourceServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月26日 下午7:14:52 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@Service
public class SourceServiceImpl implements ISourceService {
	@Resource
	private SourceMapper sourceMapper;

	/**
	 * 
	 * TODO 新增代码来源信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#saveSourceInfo(com.gtafe.data.center.codestandard.source.vo.SourceVO)
	 */
	public void saveSourceInfo(SourceVO sourceVO) {
		this.sourceMapper.saveSourceInfo(sourceVO);
	}

	/**
	 * 
	 * TODO 修改代码来源信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#updateSourceInfo(com.gtafe.data.center.codestandard.source.vo.SourceVO)
	 */
	public void updateSourceInfo(SourceVO sourceVO) {
		SourceVO vo = this.querySourceInfoByCode(sourceVO.getSourceid(),
				sourceVO.getCode());
		// 改代码来源不存在执行新增操作
		if (vo == null) {
			this.saveSourceInfo(sourceVO);
		}
		this.sourceMapper.updateSourceInfo(sourceVO);
	}

	/**
	 * 
	 * TODO 删除代码来源信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#deleteSourceInfo(com.gtafe.data.center.codestandard.source.vo.SourceVO)
	 */
	public void deleteSourceInfo(int sourceid, String code) {
		this.sourceMapper.deleteSourceInfo(sourceid, code);
	}

	/**
	 * 
	 * TODO 获取所有代码来源信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#querySourceInfoAll()
	 */
	public List<SourceVO> querySourceInfoAll() {
		return this.sourceMapper.querySourceInfoAll();
	}

	/**
	 * 
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#querySourceInfoByCode(int,
	 *      int, java.lang.String)
	 */
	public SourceVO querySourceInfoByCode(int sourceid, String code) {
		return this.sourceMapper.querySourceInfoByCode(sourceid, code);
	}

	/**
	 * 
	 * TODO 查看改来源是否存在引用关系.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#isSourceCiteRelation(int)
	 */
	public int isSourceCiteRelation(int sourceid) {
		return this.sourceMapper.isSourceCiteRelation(sourceid);
	}

	/**
	 * 
	 * TODO 初始显示树形结构.
	 * 
	 * @see com.gtafe.data.center.codestandard.source.service.ISourceService#queryTreeInfo()
	 */
	public List<SourceVO> queryTreeInfo(int sourceid) {
		return this.sourceMapper.queryTreeInfo(sourceid);
	}
}
