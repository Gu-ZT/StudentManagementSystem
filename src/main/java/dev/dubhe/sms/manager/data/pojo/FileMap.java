package dev.dubhe.sms.manager.data.pojo;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("file")
public class FileMap extends AbstractSqlBase {
    private String name;      // 文件名
    private String addr;      // 文件位置

    public FileMap(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }
}
