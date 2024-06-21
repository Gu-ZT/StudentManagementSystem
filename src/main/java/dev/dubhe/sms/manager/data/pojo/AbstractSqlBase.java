package dev.dubhe.sms.manager.data.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public abstract class AbstractSqlBase { // SQL 基类
    @TableId
    protected Long id; // ID
    @Version
    protected Integer version; // 版本
    @TableLogic
    protected Boolean delFlag; // 删除标记
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime; // 更新时间
}
