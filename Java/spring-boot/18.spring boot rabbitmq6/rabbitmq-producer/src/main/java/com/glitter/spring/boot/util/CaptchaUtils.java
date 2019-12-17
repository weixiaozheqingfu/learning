package com.glitter.spring.boot.util;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {

    private static final Random RAN = new Random();
    private static CaptchaUtils captcha;
    private int width;
    private int height;
    private int num;
    private String code;

    private CaptchaUtils() {
        code = "0123456789";
        num = 4;
    }

    public static CaptchaUtils getInstance() {
        if (captcha == null) {
            captcha = new CaptchaUtils();
        }
        return captcha;
    }

    public void set(int width, int height, int num, String code) {
        this.width = width;
        this.height = height;
        this.setNum(num);
        this.setCode(code);
    }

    public void set(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String generateCaptchaCode() {
        StringBuffer cc = new StringBuffer();
        for (int i = 0; i < num; i++) {
            cc.append(code.charAt(RAN.nextInt(code.length())));
        }
        return cc.toString();
    }

    public BufferedImage generateCaptchaImg(String checkcode) {
        // 创建一个图片对象
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图片对象的画笔
        Graphics2D graphic = img.createGraphics();
        graphic.setColor(Color.WHITE);
        graphic.fillRect(0, 0, width, height);
        graphic.setColor(Color.BLACK);
        graphic.drawRect(0, 0, width - 1, height - 1);
        Font font = new Font("宋体", Font.BOLD + Font.ITALIC, (int) (height * 0.8));
        graphic.setFont(font);
        for (int i = 0; i < num; i++) {
            graphic.setColor(new Color(RAN.nextInt(180), RAN.nextInt(180), RAN.nextInt(180)));
            graphic.drawString(String.valueOf(checkcode.charAt(i)), i * (width / num) + 4, (int) (height * 0.8));
        }

        // 加一些点
        for (int i = 0; i < (width + height); i++) {
            graphic.setColor(new Color(RAN.nextInt(255), RAN.nextInt(255), RAN.nextInt(255)));
            graphic.drawOval(RAN.nextInt(width), RAN.nextInt(height), 1, 1);
        }

        // 加一些线
        for (int i = 0; i < 4; i++) {
            graphic.setColor(new Color(RAN.nextInt(255), RAN.nextInt(255), RAN.nextInt(255)));
            graphic.drawLine(0, RAN.nextInt(height), width, RAN.nextInt(height));
        }
        return img;
    }

}
