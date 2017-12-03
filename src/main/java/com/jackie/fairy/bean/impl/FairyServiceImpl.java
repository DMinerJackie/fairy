package com.jackie.fairy.bean.impl;

import com.jackie.fairy.annotation.JackieAutowired;
import com.jackie.fairy.bean.FairyDao;
import com.jackie.fairy.bean.FairyService;

/**
 * Created by jackie on 17/11/25.
 */
public class FairyServiceImpl implements FairyService {
    @JackieAutowired
    private FairyDao fairyDao;
    private String lightColor;

    public FairyDao getFairyDao() {
        System.out.println("===getFairyDao===: " + fairyDao.toString());
        return fairyDao;
    }

    public void setFairyDao(FairyDao fairyDao) {
        System.out.println("===setFairyDao===: " + fairyDao.toString());
        this.fairyDao = fairyDao;
    }

    public String getLightColor() {
        return lightColor;
    }

    public void setLightColor(String lightColor) {
        this.lightColor = lightColor;
    }

    @Override
    public void greet() {
        fairyDao.greet();
    }

    @Override
    public void fly() {
        fairyDao.fly();
    }

    @Override
    public void lighting() {
        System.out.println("----------Hi, I am light fairy. Exactly, " + lightColor + " color light fairy----------");
    }
}
