package com.gtafe.data.center.codestandard.set.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.codestandard.set.mapper.SetMapper;
import com.gtafe.data.center.codestandard.set.service.ISetService;
import com.gtafe.data.center.codestandard.set.vo.ByBatchSetVO;
import com.gtafe.data.center.codestandard.set.vo.SetVO;
import com.gtafe.framework.base.exception.OrdinaryException;

@Service
public class SetServiceImpl implements ISetService {
	@Resource
	private SetMapper setMapper;

	/**
	 * 
	 * TODO 新增子集信息（可选）.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#saveSetInfo(com.gtafe.data.center.codestandard.set.vo.SetVO)
	 */
	public void saveSetInfo(SetVO setVO) {
		this.setMapper.saveSetInfo(setVO);
	}

	/**
	 * 
	 * TODO 修改子集信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#updateSetInfo(com.gtafe.data.center.codestandard.set.vo.SetVO)
	 */
	public void updateSetInfo(SetVO setVO) {
		SetVO vo = this.querySetByCode(setVO.getSourceid(), setVO.getSetId(),
				setVO.getCode());
		// 不存在该子集执行新增操作
		if (vo == null) {
			this.saveSetInfo(setVO);
		}
		this.setMapper.updateSetInfo(setVO);
	}

	/**
	 * 
	 * TODO 删除子集信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#deleteSetInfo(com.gtafe.data.center.codestandard.set.vo.SetVO)
	 */
	public void deleteSetInfo(int setId, String code) {
		this.setMapper.deleteSetInfo(setId, code);

	}

	/**
	 * 
	 * TODO 根据sourceid获取所有子集信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#querySetInfo(java.lang.String)
	 */
	public List<SetVO> querySetInfo(int sourceid) {
		return this.setMapper.querySetInfo(sourceid);
	}

	/**
	 * 
	 * TODO 根据id查询子集代码信息.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#querySetById(int)
	 */
	public SetVO querySetByCode(int sourceid, int setId, String code) {
		return this.setMapper.querySetByCode(sourceid, setId, code);
	}

	/**
	 * 
	 * TODO 是否存在引用关系.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#isSetCiteRelation(int)
	 */
	public int isSetCiteRelation(int setId) {
		return this.setMapper.isSetCiteRelation(setId);
	}

	/**
	 * 
	 * TODO 批量新增代码集.
	 * 
	 * @see com.gtafe.data.center.codestandard.set.service.ISetService#insertSetBatch(int,
	 *      java.util.List)
	 */
	public boolean saveSetBatch(int sourceid, List<ByBatchSetVO> batchSetVOs) {
	    if(batchSetVOs.size()==0) {
	        throw new OrdinaryException("请输入代码类 和 名称 ");
	    }
		return this.setMapper.saveSetBatch(sourceid, batchSetVOs);
	}
}
