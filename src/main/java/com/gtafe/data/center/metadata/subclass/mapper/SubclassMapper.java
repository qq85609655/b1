package com.gtafe.data.center.metadata.subclass.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.metadata.subclass.vo.SubclassByclassVO;
import com.gtafe.data.center.metadata.subclass.vo.SubclassVO;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: SubclassMapper <br/>
 */
public interface SubclassMapper extends BaseMapper {
    /**
     * querySubclassPageInfo:分页查询数据子类信息. <br/>
     *
     * @param subsetCode 子集码
     * @param classCode  类码
     * @param codeOrName
     * @param pageNum
     * @param pageSize
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public List<SubclassVO> querySubclassPageInfo(
            @Param("sourceId") int sourceId,
            @Param("subsetCode") String subsetCode,
            @Param("classCode") String classCode,
            @Param("codeOrName") String codeOrName,
            @Param("orderName") String orderName,
            @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    /**
     * saveSubclassInfo:新增子类信息. <br/>
     *
     * @param subclassVO
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public void saveSubclassInfo(SubclassVO subclassVO);

    /**
     * saveSubclassInfo:修改子类信息. <br/>
     *
     * @param subclassVO
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public void updateSubclassInfo(SubclassVO subclassVO);

    /**
     * saveSubclassInfo:删除子类信息. <br/>
     *
     * @param subclassCode
     * @param classCode
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public void deleteSubclassInfo(@Param("subclassCode") String subclassCode,
                                   @Param("classCode") String classCode);

    /**
     * saveSubclassInfo:根据子类码查询信息. <br/>
     *
     * @param subclassCode
     * @param classCode
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public SubclassVO querybySubclassCode(
            @Param("subclassCode") String subclassCode,
            @Param("classCode") String classCode);

    /**
     * citeExists:判断表是否存在. <br/>
     *
     * @param subclassTableName
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public int isTableExists(
            @Param("subclassTableName") String subclassTableName);

    /**
     * citeExists:查看该表是否存在引用关系. <br/>
     *
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public int isSubclassCiteRelation(@Param("subclassCode") String subclassCode);

    /**
     * addSubclassBatch:多条数据新增. <br/>
     *
     * @param classCode listSubclassByClassVO
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    public boolean addSubclassBatch(
            @Param("classCode") String classCode,
            @Param("listSubclassByClassVO") List<SubclassByclassVO> listSubclassByClassVO);

    /**
     * updateComment:修改表注释. <br/>
     *
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    public void updateComment(Map<String, Object> params);

    /**
     * 根据 参数获取表名
     *
     * @param itemCode
     * @param subclassCode
     * @return
     */
    @Select("SELECT subclass.subclass_tablename FROM info_metadata_subclass subclass,info_metadata_item item WHERE item.subclass_code=subclass.subclass_code and item.subclass_code = #{subclassCode} and item.item_code=#{itemCode}")
    String queryTableNameBy(@Param("itemCode") String itemCode, @Param("subclassCode") String subclassCode);
}
