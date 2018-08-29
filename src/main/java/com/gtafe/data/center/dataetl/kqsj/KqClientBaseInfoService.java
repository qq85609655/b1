package com.gtafe.data.center.dataetl.kqsj;

import com.alibaba.fastjson.JSONObject;

public interface KqClientBaseInfoService {

    void saveIntoDb(JSONObject jsonObject);
}
