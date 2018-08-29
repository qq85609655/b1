package com.gtafe.data.center.dataetl.kqsj.impl;

import com.alibaba.fastjson.JSONObject;
import com.gtafe.data.center.dataetl.kqsj.KqClientBaseInfoService;
import com.gtafe.data.center.dataetl.kqsj.mapper.KqxxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KqClientBaseInfoServiceImpl implements KqClientBaseInfoService {


    @Autowired
    private KqxxMapper kqxxMapper;

    @Override
    public void saveIntoDb(JSONObject jsonObject) {
        //解析
        List<Object> list = fromJson(jsonObject);

        //循环入库
        for (Object o : list) {
            this.kqxxMapper.saveEntity(o);
        }
    }

    private List<Object> fromJson(JSONObject jsonObject) {

        return new ArrayList<Object>();
    }
}
