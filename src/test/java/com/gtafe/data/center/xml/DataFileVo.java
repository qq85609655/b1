package com.gtafe.data.center.xml;

public class DataFileVo {
    public String id;
    public String name;
    public String start;
    public String tablePrefix;
    public DataFileVo(String id, String name, String start,String tablePrefix) {
        super();
        this.id = id;
        this.name = name;
        this.start = start;
        this.tablePrefix = tablePrefix;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStart() {
        return start;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public String getTablePrefix() {
        return tablePrefix;
    }
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
