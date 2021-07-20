package com.glitter;


import com.glitter.util.EmailUtil;
import com.glitter.util.MailUtil;
import lombok.extern.log4j.Log4j2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@SpringBootTest(classes = {Application.class})
@Log4j2
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class EmailUtilTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    public void send() throws UnsupportedEncodingException, MessagingException {
        mailUtil.simpleMailSend("limengjun1@huimin100.cn","测试111","测试能不能发邮件！！！");
//        mailUtil.attachedSend("limengjun1@huimin100.cn","测试111","测试能不能发邮件！！！", null);
    }

}
