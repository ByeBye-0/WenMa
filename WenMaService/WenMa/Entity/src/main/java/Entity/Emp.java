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
//'员工表'*/
public class Emp {
    //    id主键
    @TableId(type = IdType.AUTO)
    private Integer Id;
    //    '用户名'/账号
    private String username;
    //    密码/默认 123456
    private String password;
    //    姓名
    private String name;
    //    1 男 2女
    private Integer gender;
    //    手机号
    private String phone;
    //     '职位，1班主任，2讲师，3学生主管，4教研主管，5咨询师',
    private Integer job;
    //    '薪资',
    private Integer salary;
    //    入职日期
    private LocalDate entryDate;
    //    头像路径
    private String image;
    //    数据创建时间
    private LocalDateTime createTime;
    //     '修改时间',
    private LocalDateTime updateTime;
    //    部门id
    private Integer dept;

}
