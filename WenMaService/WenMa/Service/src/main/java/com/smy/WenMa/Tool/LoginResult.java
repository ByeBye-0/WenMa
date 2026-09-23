package com.smy.WenMa.Tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    String userId;
    String name;
    String token;
// 1是学生   2是员工
    String userType;
}
