package com.test.twincarbonboot.mapper;

import com.test.twincarbonboot.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    SysUser selectByUsername(@Param("username") String username);
}
