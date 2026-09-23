package com.smy.WenMa.Service;

import Entity.Emp;
import com.smy.WenMa.Tool.Result;

public interface EmpService {
    Result Login(String username,String password);
}
