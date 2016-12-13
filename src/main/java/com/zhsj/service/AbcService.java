package com.zhsj.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class AbcService {

    public String abc(){
        return "testHello";
    }

    public static void main(String[] args){
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);ids.add(4);
        ids.add(5);
        ids.add(6);
        ids.add(7);
        ids.add(8);

        int offset = 8;
        int size = 0;
        ids = ids.subList(offset,offset+size);
        System.out.println("===========================");
        System.out.println(ids);
        System.out.println("===========================");

    }
}
