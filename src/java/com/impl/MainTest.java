package com.impl;

/**
 * @author 咕噜科
 * ClassName: MainTest
 * date: 2021/8/26 21:57
 * Description:
 * version 1.0
 */
public class MainTest {

    public static void main(String[] args) {
        System.out.println("=========宠物商店启动=======");
        PetManage pm = new PetManage();
        //显示所有宠物
        pm.showAll();
    }

}
