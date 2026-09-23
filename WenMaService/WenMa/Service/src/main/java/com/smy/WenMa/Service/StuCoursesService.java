package com.smy.WenMa.Service;

import Entity.StuCourses;

import java.util.List;

public interface StuCoursesService {

//    增加课表
    int addStuCourses(StuCourses stuCourses);
    /*
    * 删除
    * */
    /*
    * 改
    * */

    /*
    * 查看
    * */
    List<StuCourses> getAllStuCourses(String StuId);


}
