package com.smy.WenMa.Controller;


import Entity.StuCourses;
import com.smy.WenMa.Service.StuCoursesService;
import com.smy.WenMa.Tool.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/stucourses")
public class StuCoursesController {
    @Autowired
    private StuCoursesService stuCoursesService;

    @PostMapping("/add")
    public Result stuCourses(@RequestBody StuCourses stuCourses) {
        log.info("<UNK>:{}",stuCourses);
        int res = stuCoursesService.addStuCourses(stuCourses);
        return Result.success(200, "success", res);
    }
//根据学生id获取课程表信息
    @GetMapping("/ids/{StuId}")
    public Result GetstuCourses(@PathVariable("StuId") String StuId) {
        List<StuCourses> res = stuCoursesService.getAllStuCourses(StuId);
        return Result.success(200, "success", res);
    }
}
