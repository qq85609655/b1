package com.gtafe.data.center.system.menu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.system.menu.mapper.MenuMapper;
import com.gtafe.data.center.system.menu.service.MenuService;
import com.gtafe.data.center.system.menu.vo.MenuAuthCodeInfo;
import com.gtafe.data.center.system.menu.vo.MenuInfo;
import com.gtafe.data.center.system.menu.vo.MenuVo;
import com.gtafe.framework.base.controller.BaseController;

@Service
public class MenuServiceImpl extends BaseController implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            MenuServiceImpl.class);

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<MenuVo> queryList(String menuName,  int pageNum, int pageSize) {
        return menuMapper.queryList(menuName,pageNum,pageSize);
    }

    @Override
    public boolean updateEntity(MenuVo vo, int id) {
        return menuMapper.updateEntity(vo,id);
    }

    @Override
    public MenuInfo getMenuTree() {
        List<MenuInfo> menuVos = menuMapper.getMenuInfos();
        MenuInfo rootMenu = null;
        for (MenuInfo menuVo : menuVos) {
            menuVo.setUrl(menuVo.getUrl().trim());
            for (MenuInfo menuVo2 : menuVos) {
                if (menuVo.getMenuId() == menuVo2.getParentId()) {
                    menuVo.getChildren().add(menuVo2);
                }
            }
            if (menuVo.getParentId() == 0) {
                rootMenu=menuVo;
            }
        }
        return rootMenu;
    }
    
    @Override
    public List<MenuInfo> getMenuInfos() {
        MenuInfo rootMenu = this.getMenuTree();
        List<String> authList = this.getUserInfo().getAuthList();
        if(!this.isAdmin()) {
            this.removeNoAuthMenu(rootMenu, authList);
        }
        rootMenu.removeEmptys();
        return rootMenu.getChildren();
    }
    
    private void removeNoAuthMenu(MenuInfo menu, List<String> authList) {
        if(EmptyUtil.isEmpty(menu.getChildren())) {
            return;
        }
        Iterator<MenuInfo> it = menu.getChildren().iterator();
        while(it.hasNext()) {
            MenuInfo child = it.next();
            if(child.gtLeaf()) {
                if(authList.contains(child.getAuthCode())) {
                    continue;
                }else {
                    it.remove();
                }
            }else {
                this.removeNoAuthMenu(child, authList);
            }
        }
    }


    @Override
    public MenuVo getEntityById(int moduleId) {
        return menuMapper.getEntityById(moduleId);
    }

    @Override
    public MenuAuthCodeInfo queryMenuAuthCodes() {
        List<MenuAuthCodeInfo> list = menuMapper.queryMenuAuthCodes();
        Map<String,MenuAuthCodeInfo> map = new HashMap<String,MenuAuthCodeInfo>();
        for(MenuAuthCodeInfo auth : list) {
            map.put(auth.getAuthId(), auth);
        }
        MenuAuthCodeInfo root = null;
        for(MenuAuthCodeInfo auth : list) {
            if(auth.getParentId().equals("0")) {
                root = auth;
            }
            MenuAuthCodeInfo parent = map.get(auth.getParentId());
            if(parent != null) {
                if(parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<MenuAuthCodeInfo>()); 
                }
                parent.getChildren().add(auth);
            }
        }
        if(root!=null) {
            root.removeEmptys();
        }
        return root;
    }
    
}
