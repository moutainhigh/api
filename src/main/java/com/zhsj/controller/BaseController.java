package com.zhsj.controller;

import com.zhsj.service.AbcService;
import com.zhsj.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class BaseController {

    @Autowired
    private AbcService service;

    @RequestMapping(value = "/")
    public String riderTrack(Model model, HttpServletRequest request) {
            return "index2";
    }


    @RequestMapping(value = "/getBusinessLine", method = RequestMethod.GET)
    @ResponseBody
    public Object getBusinessLine(Model model) {
        try {
            List<Map> busLines = new ArrayList<>();
                Map<Integer,String> map1 = new HashMap<>();
                map1.put(-1, "全部");
                busLines.add(map1);
                Map<Integer,String> map2 = new HashMap<>();
                map2.put(1, "自营");
                busLines.add(map2);
                Map<Integer,String> map3 = new HashMap<>();
                map3.put(2, "加盟");
                busLines.add(map3);
                Map<Integer,String> map4 = new HashMap<>();
                map4.put(4,"众包");
                busLines.add(map4);
            return CommonResult.build(0, "success", service.abc());
        } catch (Exception e) {
            return CommonResult.build(1, "后台异常");
        }
    }






}
