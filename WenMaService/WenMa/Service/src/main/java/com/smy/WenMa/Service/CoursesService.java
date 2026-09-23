package com.smy.WenMa.Service;

import Entity.Courses;
import com.smy.WenMa.Tool.Result;

import java.util.List;

public interface CoursesService {
    //    增加课程
    int addCourse(Courses course);

    //    删除课程
    Integer deleteCourseById(String courseId);

    //    查询所有课程
    List<Courses> getAllCourses();

    // 查询单个课程ID
    List<Courses> getCourseById(String courseId);

    //查询课程名称
    List<Courses> getCourseByName(String courseName);

//    返回课程详细信息
    Courses getCoursesDetailById(String courseId);
}
