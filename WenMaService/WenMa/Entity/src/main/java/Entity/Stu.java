package Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stu {
    //    学生在数据库唯一的唯一标识
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    //    '学生学号，可作为账号登录' 长度为10
    private String StuId;
    //    '学生密码，默认123456',
    private String password;
    //    姓名
    private String name;
    //    '数据创建时间,yyyy-mm-dd hh:mm:ss',
    private LocalDateTime createTime;
    //    头像
    private String image;
    //入学时间
    private LocalDate entryTime;
    //修改时间
    private LocalDateTime updateTime;

}
