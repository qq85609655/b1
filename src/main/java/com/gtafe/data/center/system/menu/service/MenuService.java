package com.gtafe.data.center.system.menu.service;

import com.gtafe.data.center.system.menu.vo.MenuAuthCodeInfo;
import com.gtafe.data.center.system.menu.vo.MenuInfo;
import com.gtafe.data.center.system.menu.vo.MenuVo;

import java.util.List;

public interface MenuService {
    /**
     * 查询菜单列表
     *
     * @param menuName
     * @param pageNum
     * @param pageSize
     * @return
     */

    List<MenuVo> queryList(String menuName,   int pageNum, int pageSize);

    boolean updateEntity(MenuVo vo, int id);

    MenuInfo getMenuTree();
    
    List<MenuInfo> getMenuInfos();
    /**
     * 获取权限码列表
     * @author 汪逢建
     * @date 2017年11月1日
     */
    public MenuAuthCodeInfo queryMenuAuthCodes();


    MenuVo getEntityById(int moduleId);
}
