package dev.dubhe.sms.manager.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dev.dubhe.sms.manager.data.dao.IUserDao;
import dev.dubhe.sms.manager.data.mapper.UserMapper;
import dev.dubhe.sms.manager.data.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserDao extends ServiceImpl<UserMapper, User> implements IUserDao {
    @Override
    public User getByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).last("limit 1");
        return this.getOne(queryWrapper);
    }
}
