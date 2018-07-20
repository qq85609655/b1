package com.gtafe.data.center.system.user.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserAuthMapper {

    /**
     * 获取用户权限码
     *
     * @param userId
     * @return
     */
    List<String> getUserAllAuthcodes(@Param("userId") String userId);
}
