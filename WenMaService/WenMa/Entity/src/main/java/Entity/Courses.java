package Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courses {
    //    '课程序号' 自增
    @TableId(type = IdType.AUTO)
    private Integer id;
    //        课程代码，唯一',
    private String coursesId;
    //        '课程名字',
    private String coursesName;
    //        '课程分类',
    private String coategory;
    //        '课程描述',
    private String description;
    //         '课程时长',
    private Integer totalHours;
    //          '课程价格',
    private double price;


}
