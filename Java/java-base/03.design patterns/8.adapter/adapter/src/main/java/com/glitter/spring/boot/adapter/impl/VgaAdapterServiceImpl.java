package com.glitter.spring.boot.adapter.impl;

import com.glitter.spring.boot.adapter.TypecAdapterService;
import com.glitter.spring.boot.service.VgaService;
import org.springframework.stereotype.Service;


/**
 * typec接口的vga转换器,用于对接外部设备接口是vga接口的外部设备
 */
@Service
public class VgaAdapterServiceImpl implements TypecAdapterService {

    VgaService vgaService;

    public VgaAdapterServiceImpl(VgaService vgaService) {
        this.vgaService = vgaService;
    }

    /**
     * 可以将外设的数据传输给电脑
     *
     * @param msg
     */
    @Override
    public void input(String msg) {
        vgaService.vgaInput(msg);
    }

    /**
     * 可以电脑数据传输给外设
     *
     * @param msg
     */
    @Override
    public void output(String msg) {
        vgaService.vgaOutput(msg);
    }

}
