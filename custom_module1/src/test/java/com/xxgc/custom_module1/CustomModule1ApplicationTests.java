package com.xxgc.custom_module1;

import com.xxgc.sys.entity.User;
import com.xxgc.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class CustomModule1ApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        List<User> userList=userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
