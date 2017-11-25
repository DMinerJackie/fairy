package com.jackie.fairy.bean.impl;

import com.jackie.fairy.bean.FairyDao;

/**
 * Created by jackie on 17/11/19.
 */
public class FairyDaoImpl implements FairyDao {
    @Override
    public void greet() {
        System.out.println("Hi, I am fairy");
    }

    @Override
    public void fly() {
        System.out.println("Let's go to fly");
    }
}
