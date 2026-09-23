package com.smy.WenMa.Service.Impl;

import Entity.StuCourses;
import Mapper.StuCoursesMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smy.WenMa.Service.StuCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StuCoursesServiceImpl implements StuCoursesService {

    @Autowired
    StuCoursesMapper stuCoursesMapper;

    @Override
    public int addStuCourses(StuCourses stuCourses) {
        int res = 0;
        res = stuCoursesMapper.insert(stuCourses);
        return res;
    }

    @Override
    public List<StuCourses> getAllStuCourses(String stuId) {
        LambdaQueryWrapper<StuCourses> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StuCourses::getStuId, stuId);
        List<StuCourses> list = stuCoursesMapper.selectList(queryWrapper);
        return list;
    }
}
