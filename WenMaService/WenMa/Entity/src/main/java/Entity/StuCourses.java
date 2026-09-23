package Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 多对多关系，学生课程表
 * 关联学生表和课程表
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stu_courses")
public class StuCourses {
    @TableId(type = IdType.AUTO)
//    数据库唯一标识'
    private Integer id;
    //    '关联stu表',长度为10
    private String stuId;
    //    '课程代码，唯一，关联courses表 长度为5
    @TableField("courses_id")
    private String coursesId;
    //    成绩，默认为0
    private Integer score;
//学习进度
    private Integer progress;
}
