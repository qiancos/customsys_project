package com.xxgc.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxgc.common.Result;
import com.xxgc.sys.entity.User;
import com.xxgc.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-09-07
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/all")
    public Result<List<User>>getAlluser(){
        List<User> list= userService.list();
        return Result.success("查询成功",list);
    }


    //登录
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user){
        Map<String,Object> data=userService.login(user);
        if (data!=null){
            return Result.success(data);
        }
        return Result.fail(20002,"用户名和密码错误");
    }

    //超时连接
    @GetMapping("/info")
    public Result<Map<String,Object>> getUserInfo(@RequestParam("token") String token){
        Map<String,Object> data=userService.getUserInfo(token);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20003,"登录信息无效，请重新登录");

    }

    //注销
    @PostMapping("/logout")
    public Result<?>logout(@RequestHeader("X-Token") String token){
        return Result.success();

    }

//   查询接口
    @GetMapping("/list")
    public Result<Map<String,Object>> getUserList(@RequestParam(value = "username",required = false) String username,String phone,
                                                  @RequestParam(value = "pageNumber") Long pageNumber,
                                                  @RequestParam(value = "pageSize") Long pageSize){

        //1.设置查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(username),User::getUsername,username);
        wrapper.like(StringUtils.hasLength(phone),User::getPhone,phone);

        //2.设置分页参数
        Page<User> page = new Page<>(pageNumber, pageSize);


        //执行查询
        //根据page中的分页参数
        userService.page(page,wrapper);


//        4.封装响应参数
        HashMap<String, Object> data = new HashMap<>();
        data.put("total",page.getTotal());
        data.put("row",page.getRecords());
        wrapper.orderByDesc(User::getId);
        return Result.success(data);
    }


    //新增·
//    @PostMapping()
    @PostMapping()
    public Result<?> addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return Result.success("新增用户成功");
    }

    //修改
    @PutMapping()
    public Result<?> updateUserById(User user){
        user.setPassword(null);
        userService.updateById(user);
        return Result.success("修改用户成功");
    }
    //根据用户查询id
    @GetMapping("/{id}")
    public Result<User>getUserById(@PathVariable("id") Integer id){
        User user = userService.getById(id);
        return Result.success(user);
    }
    //删除

    @DeleteMapping("/{id}")
    public Result<User>deleteUserById(@PathVariable("id") Integer id){
        userService.removeById(id);
        return Result.success("删除用户成功");
    }
}
