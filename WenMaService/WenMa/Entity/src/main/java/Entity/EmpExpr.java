package Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("emp_expr")
public class EmpExpr {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //关联emp的id（外键）
    private Integer empId;
    //公司名字
    private String company;
    //职位
    private String job;
    //    入职时间
    private LocalDate begin;
    //    离职时候
    private LocalDate end;

}
