package com.smy.WenMa.Service.Impl;

import Entity.Courses;
import Mapper.CoursesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smy.WenMa.Service.CoursesService;
import com.smy.WenMa.Tool.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoursesServiceImp implements CoursesService {
    @Autowired
    private CoursesMapper coursesMapper;

    //    添加课程
    @Override
    public int addCourse(Courses course) {
        int res = 0;
        res = coursesMapper.insert(course);
        return res;
    }

    /*
     * 删除课程，这里是指删除当前时间在售的课程，对于已经开班了的课程不做处理
     * 不必添加事务管理
     * */
    @Override
    public Integer deleteCourseById(String courseId) {
        LambdaQueryWrapper<Courses> coursesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        coursesLambdaQueryWrapper.eq(Courses::getCoursesId, courseId);

        int result = 0;
        result = coursesMapper.delete(coursesLambdaQueryWrapper);
        return result;
    }

    @Override
    public List<Courses> getAllCourses() {
        List<Courses> list = coursesMapper.selectList(null);
        return list;
    }

    @Override
    public List<Courses> getCourseById(String courseId) {
        LambdaQueryWrapper<Courses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Courses::getCoursesId, courseId);
        List<Courses> courses = coursesMapper.selectList(queryWrapper);
        System.out.println(courses);
        if (courses == null) {
            return null;
        }
        return courses;
    }

    @Override
    public List<Courses> getCourseByName(String courseName) {
        LambdaQueryWrapper<Courses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Courses::getCoursesName, courseName);
        List<Courses> res= coursesMapper.selectList(queryWrapper);
        return res;
    }

    @Override
    public Courses getCoursesDetailById(String courseId) {
        LambdaQueryWrapper<Courses> coursesLambdaQueryWrapper = new LambdaQueryWrapper<>();
        coursesLambdaQueryWrapper.eq(Courses::getCoursesId, courseId);
        Courses courses = coursesMapper.selectOne(coursesLambdaQueryWrapper);
        return courses;
    }
}
