package com.tegasus9.anytest.infrastructrue.dao;
import com.tegasus9.anytest.infrastructrue.po.TestUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestUserMapper {

    List<TestUser> selectAllByUserPhone(@Param("userPhone")String userPhone);

    int insertSelective(TestUser testUser);


}