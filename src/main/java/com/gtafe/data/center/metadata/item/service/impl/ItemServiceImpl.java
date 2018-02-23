/**
 * Project Name:gtacore
 * File Name:SubsetServiceImpl.java
 * Package Name:com.gtafe.data.center.meta.service.impl
 * Date:2017年7月18日上午10:40:07
 * Copyright (c) 2017, GTA All Rights Reserved.
 */
package com.gtafe.data.center.metadata.item.service.impl;

import com.gtafe.data.center.metadata.item.mapper.ItemMapper;
import com.gtafe.data.center.metadata.item.service.IItemService;
import com.gtafe.data.center.metadata.item.vo.ItemVO;
import com.gtafe.data.center.metadata.item.vo.SubsetTreeVO;
import com.gtafe.framework.base.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemServiceImpl extends BaseService implements IItemService {
    @Resource
    private ItemMapper itemMapper;


    @Override
    public List<ItemVO> queryItemList(int sourceId, String itemCodeOritemName, int pageNum, int pageSize) {
        return this.itemMapper.queryItemList(sourceId, itemCodeOritemName, pageNum, pageSize);
    }

    @Override
    public boolean itemDelete(String subclassCode, String itemCode) {
        return this.itemMapper.itemDelete(subclassCode, itemCode);
    }


    @Override
    public boolean itemUpdate(ItemVO itemVO) {
        ItemVO s = this.queryByCode(itemVO.getSubclassCode(), itemVO.getItemCode());
        if (s == null) {
            return this.itemAdd(itemVO);
        } else
        return this.itemMapper.itemUpdate(itemVO);
    }

    @Override
    public boolean itemAdd(ItemVO itemVO) {
        return this.itemMapper.itemAdd(itemVO);
    }

    @Override
    public ItemVO queryByCode(String subclassCode, String itemCode) {
        return this.itemMapper.queryByCode(subclassCode, itemCode);
    }

    @Override
    public List<SubsetTreeVO> queryMetaDataTreeInfo(int sourceId) {
        return this.itemMapper.queryMetaDataTreeInfo(sourceId);
    }

    @Override
    public List<ItemVO> queryItemListBysubclass(String subclassCode, String itemCodeOrItemName, int pageNum, int pageSize) {
        return this.itemMapper.queryItemListBysubclass(subclassCode, itemCodeOrItemName, pageNum, pageSize);
    }

    @Override
    public String queryTableName(String subclassCode) {
        return this.itemMapper.queryTableName(subclassCode);
    }

    @Override
    public int existTable(String subclassTableName) {
        return this.itemMapper.existTable(subclassTableName);
    }

}
