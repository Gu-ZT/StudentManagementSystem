package dev.dubhe.sms.manager.data.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import dev.dubhe.sms.manager.data.pojo.User;

public interface IUserDao extends IService<User> {
    User getByUserName(String username);
}
