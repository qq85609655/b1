package com.gtafe.data.center.system.menu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gtafe.data.center.system.menu.vo.MenuAuthCodeInfo;
import com.gtafe.data.center.system.menu.vo.MenuInfo;
import com.gtafe.data.center.system.menu.vo.MenuVo;
import com.gtafe.framework.base.mapper.BaseMapper;

public interface MenuMapper extends BaseMapper {
    /**
     * @param menuName
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<MenuVo> queryList(@Param("menuName") String menuName, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    /**
     * @param vo
     * @param id
     * @return
     */
    boolean updateEntity(MenuVo vo, @Param("id") int id);

    /**
     * @return
     */
    @Select("SELECT menu_id menuId,parent_id parentId ,menu_name menuName,url url,auth_code authCode,node_type nodeType FROM sys_menu where state = 1 order by sort asc")
    List<MenuInfo> getMenuInfos();


    /**
     * 获取权限码列表
     *
     * @author 汪逢建
     * @date 2017年11月1日
     */
    List<MenuAuthCodeInfo> queryMenuAuthCodes();


/*    List<MenuAuthCodeInfo> queryMenuAuthCodes2();*/

    @Select("SELECT menu_id menuId,parent_id parentId ,menu_name menuName,url url,auth_code authCode FROM sys_menu where menu_id =#{moduleId}")
    MenuVo getEntityById(@Param("moduleId") int moduleId);
}
