package com.gtafe.data.center;

import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.dataetl.trans.EtlTrans;
import com.gtafe.framework.base.utils.MailSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
public class PrjTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    EtlMapper etlMapper;

    @Autowired
    EtlTrans etlTrans;

    @Autowired
    MailSender mailSender;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void t() throws Exception {
      //  etlTrans.setExcuteOnce(true);
       // etlTrans.setScheId(9);
      //  etlTrans.execute(null);
    }

    @Test
    public void t1() throws Exception {

       // boolean result=mailSender.sendEmail("gang.zhou@gtafe.com","测试","扫大街佛山都放松的哈佛三名防守都放假哦诶将沃尔沃二年为日哦as美分");
    }


}