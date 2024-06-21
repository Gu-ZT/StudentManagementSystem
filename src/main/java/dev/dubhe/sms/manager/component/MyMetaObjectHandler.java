package dev.dubhe.sms.manager.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import dev.dubhe.sms.manager.util.DateUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", DateUtil.now(), metaObject);
        this.setFieldValByName("updateTime", DateUtil.now(), metaObject);
        this.setFieldValByName("time", DateUtil.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", DateUtil.now(), metaObject);
    }
}
