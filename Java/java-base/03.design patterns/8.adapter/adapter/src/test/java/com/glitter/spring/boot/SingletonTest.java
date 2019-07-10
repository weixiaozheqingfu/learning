package com.glitter.spring.boot;


import com.glitter.spring.boot.adapter.TypecAdapterService;
import com.glitter.spring.boot.adapter.impl.HdmiAdapterServiceImpl;
import com.glitter.spring.boot.adapter.impl.VgaAdapterServiceImpl;
import com.glitter.spring.boot.service.HdmiService;
import com.glitter.spring.boot.service.VgaService;
import com.glitter.spring.boot.service.impl.HdmiServiceImpl;
import com.glitter.spring.boot.service.impl.VgaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SingletonTest {

    @Test
    public void HdmiAdapterTest() {
        HdmiService hdmiService = new HdmiServiceImpl();
        TypecAdapterService typecService = new HdmiAdapterServiceImpl(hdmiService);
        typecService.input("hello world");
        typecService.input("haha");
    }

    @Test
    public void VgaAdapterTest() {
        VgaService vgaService = new VgaServiceImpl();
        TypecAdapterService typecService = new VgaAdapterServiceImpl(vgaService);
        typecService.input("hello world");
        typecService.input("haha");
    }

}
