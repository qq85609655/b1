package com.gtafe.data.center.dataetl.kqsj.mapper;

import org.apache.ibatis.annotations.Insert;

public interface KqxxMapper {

    @Insert("insert into t_client_baseInfo() values()")
    void saveEntity(Object o);
}
