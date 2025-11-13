package com.scoremanagement;

import com.scoremanagement.view.MenuView;

/**
 * 学生成绩管理系统主程序
 * 系统入口点
 */
public class Main {
    public static void main(String[] args) {
        try {
            // 创建菜单视图并启动系统
            MenuView menuView = new MenuView();
            menuView.start();
        } catch (Exception e) {
            System.err.println("系统启动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}