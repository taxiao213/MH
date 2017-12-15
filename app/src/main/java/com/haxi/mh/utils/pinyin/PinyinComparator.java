package com.haxi.mh.utils.pinyin;


import java.util.Comparator;


/**
 * 比较排序
 * Created by Han on 2017/12/11
 * Email:yin13753884368@163.com
 */
public class PinyinComparator implements Comparator {

    public static PinyinComparator instance = null;

    public static PinyinComparator getInstance() {
        if (instance == null) {
            synchronized (PinyinComparator.class) {
                if (instance == null) {
                    instance = new PinyinComparator();
                }
            }
        }
        return instance;
    }


    @Override
    public int compare(Object o, Object t1) {

        return 0;
    }
}
