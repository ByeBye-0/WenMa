package com.smy.WenMa.Controller;

import Entity.Courses;
import com.smy.WenMa.Service.CoursesService;
import com.smy.WenMa.Tool.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CoursesController {
    @Autowired
    private CoursesService coursesService;
//获得所有课程
    @GetMapping("/all")
    public Result getAllCourses() {
        log.info("查询课程");
        List<Courses> list = coursesService.getAllCourses();
        return Result.success(200, "success", list);
    }
//返回多个课程，同名的情况下
    @GetMapping("/{coursesId}")
    public Result getCoursesById(@PathVariable("coursesId") String coursesId) {
        log.info("查询课程{}", coursesId);
//        通过id去查询只会返回一个
        List<Courses> courses = coursesService.getCourseById(coursesId);
        if (courses.isEmpty()) {
//            根据课程名字查询，返回多个同名
            courses = coursesService.getCourseByName(coursesId);
        }
        System.out.println(courses);
        return Result.success(200, "success", courses);
    }
//返回单个课程详情/在购买课程的页面中需要
    @GetMapping("/detail/{coursesId}")
    public Result getCoursesDetail(@PathVariable("coursesId") String coursesId) {
        log.info("查询课程{}", coursesId);
        Courses courses = coursesService.getCoursesDetailById(coursesId);
        if (courses==null) {
            return Result.error(500, "error", courses);
        }
        System.out.println(courses);
        return Result.success(200, "success", courses);
    }
//传输一个列表，返回一个列表/在学生的空间里面，需要查看个人课程表
    @GetMapping("/list")
    public Result getCoursesList(@RequestParam List <String> coursesIds) {
        log.info("查询课程{}", coursesIds);
        List<Courses> list=new ArrayList<>();
        for (String coursesId : coursesIds) {
            System.out.println(coursesId);
            Courses courses = coursesService.getCoursesDetailById(coursesId);
            list.add(courses);
        }
        return Result.success(200, "success", list);
    }

}
