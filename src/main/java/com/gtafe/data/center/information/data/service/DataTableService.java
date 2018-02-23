package com.gtafe.data.center.information.data.service;

import java.util.List;

public interface DataTableService {
    /**
     * 根据子类建表
     * @author 汪逢建
     * @date 2017年12月20日
     */
    public boolean createTable(String subclassCode);
    /**
     * 根据子类建表
     * @author 汪逢建
     * @date 2017年12月20日
     */
    public boolean createTables(List<String> subclassCodes);
}
