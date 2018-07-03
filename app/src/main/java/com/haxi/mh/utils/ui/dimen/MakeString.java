package com.haxi.mh.utils.ui.dimen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * 资源快速生成方法
 * Created by Han on 2018/7/3
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class MakeString {
    private final static String rootPath = "C:\\Users\\aaa\\Desktop\\bean\\";//存放路径桌面
    private final static String[] method = new String[]{"haha", "xixi"};//字段名
    private final static String[] name = new String[]{"我", "你"};//字段匹配的中文名称
    private final static String[] id = new String[]{"tv_name", "tv_code"};//资源id名

    public static void main(String[] args) {
        makeString("work");
    }

    public static void makeString(String bean) {

        StringBuffer sb = new StringBuffer();
        StringBuffer st = new StringBuffer();
        for (int i = 0; i < method.length; i++) {
            sb.append("String " + method[i] + " = " + bean + ".get" + method[i] + "();" + "\\" + "\\" + name[i] + "\n");
            st.append(method[i] + ".setCenterText(setText(" + id[i] + "));" + "\\" + "\\" + name[i] + "\n");
        }
        File rootFile = new File(rootPath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File getFile = new File(rootPath + "method.txt");
        File setFile = new File(rootPath + "methodSet.txt");
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(getFile));
            pw.print(sb.toString());
            pw.close();

            PrintWriter pwst = new PrintWriter(new FileOutputStream(setFile));
            pwst.print(st.toString());
            pwst.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
