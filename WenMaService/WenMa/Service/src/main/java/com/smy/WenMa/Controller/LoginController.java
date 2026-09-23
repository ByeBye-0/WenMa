package com.smy.WenMa.Controller;

import com.smy.WenMa.Service.EmpService;
import com.smy.WenMa.Service.StuService;
import com.smy.WenMa.Tool.LoginInfo;
import com.smy.WenMa.Tool.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Autowired
    StuService stuServiceImp;
    @Autowired
    EmpService empServiceImp;

    @PostMapping("/{type}")
    public Result login(@RequestBody LoginInfo loginInfo) {
//        1 是学生 2是员工

        if (loginInfo.getUserType() == 1) {
            log.info("学生登录{}", loginInfo.getUsername());
            return stuServiceImp.Login(loginInfo.getUsername(), loginInfo.getPassword());
        } else if (loginInfo.getUserType() == 2) {
            log.info("员工登录{}", loginInfo.getUsername());
            return empServiceImp.Login(loginInfo.getUsername(), loginInfo.getPassword());
        }
        return Result.error(501, "未知错误", null);
    }
}
