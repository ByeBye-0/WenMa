package Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("emp_log")
//操作日志
public class EmpLog {
    @TableId(type = IdType.AUTO)
    private  Integer id;
//    操作时间
    private LocalDateTime operateTime;
//    日志信息
    private String info;
}


