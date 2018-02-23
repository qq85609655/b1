package com.gtafe.data.center.metadata.item.service;

import java.util.List;

import com.gtafe.data.center.metadata.item.vo.ItemVO;
import com.gtafe.data.center.metadata.item.vo.SubsetTreeVO;

public interface IItemService {
	
List<ItemVO> queryItemList(int sourceId, String itemCodeOritemName, int pageNum, int pageSize);
	
boolean itemDelete(String subclassCode, String itemCode);
	
boolean itemUpdate(ItemVO itemVO);
	
boolean itemAdd(ItemVO itemVO);
	
ItemVO queryByCode(String subclassCode, String itemCode);

public List<SubsetTreeVO> queryMetaDataTreeInfo(int sourceId);

List<ItemVO> queryItemListBysubclass(String subclassCode, String itemCodeOrItemName, int pageNum, int pageSize);

String queryTableName(String subclassCode);

int existTable(String subclassTableName);

}
