package com.gtafe.data.center.system.org.mapper;

import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrgMapper extends BaseMapper {
    /**
     * @param orgName
     * @param state
     * @param parentId
     * @param orgType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<OrgVo> queryList(@Param("orgName") String orgName, @Param("state") String state,
                          @Param("parentId") String parentId, @Param("orgType") String orgType,
                          @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);


    @Select("Select count(1) from sys_org where org_name=#{orgName} and parent_id=#{parentId} and node_type=#{nodeType}")
    int queryByOrgName(@Param("orgName") String orgName, @Param("parentId") String parentId, @Param("nodeType") int nodeType);


    @Select("Select count(1) from sys_org where org_no=#{orgNo}")
    int queryByOrgNo(@Param("orgNo") String orgNo);

    /**
     * @param vo
     * @return
     */
    boolean saveEntity(OrgVo vo);

    /**
     * @param ids
     * @return
     */
    boolean deleteEntity(@Param("ids") List<String> ids);

    /**
     * @param vo
     * @return
     */
    boolean updateEntity(OrgVo vo);

    /**
     * @param orgId
     * @return
     */
    OrgVo getEntityById(@Param("orgId") String orgId);

    /**
     * @param userId 小于0表示不过滤用户权限
     * @author 汪逢建
     * @date 2017年11月30日
     */
    List<OrgVo> getOrgVos(@Param("userId") String userId);

    @Insert("INSERT INTO  sys_org(org_name,org_address,org_no,org_type,node_type,state,sort,parent_id,creater,createtime,updater,updatetime,manager )  VALUES(#{orgName},#{orgAddress},#{orgNo},#{orgType},#{nodeType},#{state},#{sort},#{parentId},#{creater},#{createTime},#{updater},#{updateTime},#{manager})")
    @Options(useGeneratedKeys = true)
    int insertOrg(OrgVo org);

    @Update("update sys_org   set sort = #{sort} where id = #{id}")
    boolean updateSort(OrgVo org);

    @Delete("delete from sys_org where id = #{id}")
    boolean deleteById(String id);

    @Select("SELECT  COUNT(*)  from sys_org WHERE  parent_id = #{id}")
    boolean getSubOrgNum(String id);

    @Select("SELECT  org_no orgNo  from sys_org WHERE  parent_id = #{id}")
    List<OrgVo> getSubOrg(String id);

    @Select("SELECT id,parent_id parentId ,org_name orgName,org_no orgNo,org_address orgAddress,org_type orgType,state,node_type nodeType,manager  FROM  sys_org  WHERE  id = #{id}")
    OrgVo getOrgVoById(String id);

    @Update("update sys_org   set org_name = #{orgName}, org_type = #{orgType},manager = #{manager},org_address = #{orgAddress},updatetime = #{updateTime}, updater = #{updater} where id = #{id}")
    boolean updateOrg(OrgVo org);

    @Select("SELECT   id ,sort  from sys_org WHERE  parent_id = (SELECT parent_id FROM sys_org WHERE id = #{id}) order by sort")
    List<OrgVo> getSameOrg(String id);

    /**
     * @param userId 小于0表示不过滤用户权限
     * @author 周刚
     */
    List<OrgVo> getOrgVos4Import(@Param("userId") int userId);
}
