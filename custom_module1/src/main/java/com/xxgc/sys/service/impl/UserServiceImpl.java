package com.xxgc.sys.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxgc.common.utils.JwtUtil;
import com.xxgc.sys.entity.User;
import com.xxgc.sys.mapper.UserMapper;
import com.xxgc.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-09-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> login(User user) {
        //根据用户名查询
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        //queryWrapper.eq(User::getPassword,user.getPassword());
        User loginUser=this.baseMapper.selectOne(queryWrapper);
        //2.结果不为空，则生成token，并将用户信息存入redis
        if (loginUser!=null && passwordEncoder.matches(user.getPassword(),loginUser.getPassword())){
            //清除密码
            loginUser.setPassword(null);
            String token=jwtUtil.createToken(loginUser);
            //返回数据
            Map<String,Object> data=new HashMap<>();
            data.put("token",token);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        User loginUser=null;
        try {
            loginUser=jwtUtil.parseToken(token,User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (loginUser != null) {
            Map<String,Object> data=new HashMap<>();
            data.put("name",loginUser.getUsername());
            data.put("avatar",loginUser.getAvatar());
            List<String> roleList = this.baseMapper.getRoleNameByUserId(loginUser.getId());
            data.put("role",roleList);
            return data;
        }
        return null;
    }



}
