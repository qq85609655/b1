package com.gtafe.data.center.information.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.information.data.mapper.DataStandardItemMapper;
import com.gtafe.data.center.information.data.mapper.DataStandardMapper;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.StringUtil;

@Service
public class DataStandardServiceImpl extends BaseController implements DataStandardService{
    @Resource
    private DataStandardMapper dataStandardMapper;
    @Resource
    private DataStandardItemMapper dataStandardItemMapper;
    @Resource
    private DataTaskMapper dataTaskMapper;
    @Resource
    private LogService logServiceImpl;
    @Override
    public List<DataStandardVo> queryDataOrgList(String keyWord,int sourceId,
                                                     String subsetCode, String classCode,int nodeType, 
                                                     int pageNum,int pageSize) {
        return dataStandardMapper.queryDataOrgList(keyWord, sourceId, 
            subsetCode, classCode, nodeType, pageNum, pageSize);
    }

    @Override
    public List<DataStandardVo> queryDataOrgListAll(int sourceId,
                                                    String subsetCode,
                                                    String classCode,
                                                    int nodeType) {
        return dataStandardMapper.queryDataOrgListAll(sourceId, subsetCode, classCode, nodeType);
    }
    
    @Override
    public DataStandardVo queryDataOrgTree(int sourceId) {
        List<DataStandardVo> list = this.dataStandardMapper.queryDataOrgTreeVos(sourceId);
        Map<String,DataStandardVo> map = new HashMap<String,DataStandardVo>();
        for(DataStandardVo vo : list) {
            map.put(vo.getCode(), vo);
        }
        DataStandardVo root = null;
        for(DataStandardVo vo : list) {
            if(vo.getParentCode().equals("")) {
                root = vo;
            }
            DataStandardVo parent = map.get(vo.getParentCode());
            if(parent != null) {
                if(parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<DataStandardVo>()); 
                }
                parent.getChildren().add(vo);
            }
        }
        /*if(root!=null) {
            root.removeEmptys();
        }*/
        return root;
    }

    @Override
    public DataStandardVo getDataStandardVo(String code) {
        return dataStandardMapper.getDataStandardVo(code);
    }

    @Override
    public boolean addDataStandardVos(int sourceId, String parentCode, List<DataStandardVo> voList,
                                      int nodeType) {
        if(voList==null || voList.isEmpty()) {
            return true;
        }
        DataStandardVo parent = this.dataStandardMapper.getDataStandardVo(parentCode);
        if(parent == null) {
            throw new OrdinaryException("父节点不存在或已被删除！");
        }
        List<String> codeList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        List<String> tablenameList = new ArrayList<String>();
        nodeType = parent.getNodeType() + 1;
        List<String> codenameList = new ArrayList<String>();
        for(DataStandardVo vo : voList) {
            codenameList.add(vo.getCode()+"/"+vo.getName());
            codeList.add(vo.getCode());
            nameList.add(vo.getName());
            if(nodeType == 3) {
                vo.setTableName(vo.getTableName().toUpperCase());
                tablenameList.add(vo.getTableName());
            }else {
                vo.setTableName("");
            }
            vo.setParentCode(parentCode);
            vo.setNodeType(nodeType);
            vo.setDescription(vo.getDescription()==null ? "" : vo.getDescription());
        }
        //检查数据准确性
        codeList = this.dataStandardMapper.checkDataCodeRepeat(codeList);
        if(codeList!=null && codeList.size()>0) {
            throw new OrdinaryException("部分编号已存在"+ codeList.toString() +"！");
        }
        /*nameList = this.dataStandardMapper.checkDataNameRepeat(nameList, parentCode, null);
        if(nameList!=null && nameList.size()>0) {
            throw new OrdinaryException("部分名称已存在"+ nameList.toString() +"！");
        }*/
        if(nodeType == 3) {
            tablenameList = this.dataStandardMapper.checkDataTablenameRepeat(tablenameList);
            if(tablenameList!=null && tablenameList.size()>0) {
                throw new OrdinaryException("部分英文名称已存在"+ tablenameList.toString() +"！");
            }
        }
        this.dataStandardMapper.insertDataStandardVos(voList, sourceId, this.getUserId());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(this.getMoudleId(nodeType));
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增"+ this.getTypeName(nodeType)  +":"+ StringUtil.join(codenameList));
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }
    
    @Override
    public boolean updateDataStandardVo(int sourceId, DataStandardVo dataVo,
                                        int nodeType) {
        DataStandardVo dbVo = this.dataStandardMapper.getDataStandardVo(dataVo.getCode());
        if(dbVo == null) {
            throw new OrdinaryException("节点不存在或已被删除！");
        }
        /*List<String> nameList = this.dataStandardMapper.checkDataNameRepeat(
            Arrays.asList(new String[] {dataVo.getName()}), dataVo.getParentCode(), dataVo.getCode());
        if(nameList!=null && nameList.size()>0) {
            throw new OrdinaryException("名称已存在！");
        }*/
        nodeType = dbVo.getNodeType();
        dataVo.setNodeType(dbVo.getNodeType());
        if(dataVo.getNodeType() == 3) {
            this.checkTaskUsing(dataVo.getCode(), sourceId);
            //禁止修改表名，不再重建表
        }
        this.dataStandardMapper.updateDataStandardVo(dataVo, sourceId, this.getUserId());
        
        String content = dataVo.getCode()+"/"+(!dbVo.getName().equals(dataVo.getName()) ? dbVo.getName()+"->":"") + dataVo.getName();
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(this.getMoudleId(nodeType));
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改"+ this.getTypeName(nodeType)  +":"+ content);
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public List<DataStandardVo> queryNodesByParentId(int pid) {
        return null;
    }

    @Override
    public boolean deleteDataStandardVo(int sourceId, String code,
                                        int nodeType) {
        DataStandardVo dbVo = this.dataStandardMapper.getDataStandardVo(code);
        if(dbVo == null) {
            return true;
        }
        if(dbVo.getNodeType() < 3) {
            List<DataStandardVo> list = this.dataStandardMapper.getChildDataStandardVos(code, sourceId);
            if(list!=null && list.size() > 0) {
                throw new OrdinaryException("当前节点下存在下级节点，不能删除！");
            }
        }else if(dbVo.getNodeType() == 3) {
            List<DataStandardItemVo> list = this.dataStandardItemMapper.querySubclassItemList(sourceId, code, 1, 1);
            if(list!=null && list.size() > 0) {
                throw new OrdinaryException("当前子类下存在下级元数据，不能删除！");
            }
            this.checkTaskUsing(code, sourceId);
        }
        nodeType = dbVo.getNodeType();
        this.dataStandardMapper.deleteDataStandardVo(code, sourceId);
        
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(this.getMoudleId(nodeType));
        logInfo.setOperType("删除");
        logInfo.setOperContent("删除"+ this.getTypeName(nodeType)  +":"+ dbVo.getCode()+"/"+dbVo.getName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }
    
    
    /**
     * 检查是否存在资源任务使用数据表
     * @author 汪逢建
     * @date 2017年12月12日
     */
    private void checkTaskUsing(String subclassCode,int sourceId){
        List<DataTaskVo> dataTaskVoList = this.dataTaskMapper.findTasksBySubclass(subclassCode);
        if(!dataTaskVoList.isEmpty()) {
            StringBuffer fbtask = new StringBuffer("");
            StringBuffer dytask = new StringBuffer("");
            for (DataTaskVo vo : dataTaskVoList) {
                if (vo.getBusinessType() == 1) {
                    fbtask.append(vo.getTaskName()).append(",");
                } else {
                    dytask.append(vo.getTaskName()).append(",");
                }
            }
            StringBuffer error = new StringBuffer();
            if(fbtask.length()>0) {
                error.append("发布任务【").append(fbtask.substring(0,fbtask.length()-1)).append("】");
            }
            if(dytask.length()>0) {
                if(error.length()>0) {
                    dytask.append("，");
                }
                error.append("订阅任务【").append(dytask.substring(0,dytask.length()-1)).append("】");
            }
            String msg = "当前元数据相关的数据表被" + error.toString() + "引用，请先删除数据资源任务!";
            throw new OrdinaryException(msg);
        }
    }
    
    private String getTypeName(int nodeType) {
        if(nodeType==1) {
            return "数据子集";
        }else if(nodeType==2) {
            return "数据类";
        }else {
            return "数据子类";
        }
    }
    private int getMoudleId(int nodeType) {
        if(nodeType==1) {
            return LogConstant.Module_Standard_Subset;
        }else if(nodeType==2) {
            return LogConstant.Module_Standard_Class;
        }else {
            return LogConstant.Module_Standard_Subclass;
        }
    }
}
