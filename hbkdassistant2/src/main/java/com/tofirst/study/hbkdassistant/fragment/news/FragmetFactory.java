package com.tofirst.study.hbkdassistant.fragment.news;

import java.util.HashMap;

/**
 * =========================================================
 *
 *  版权: 个人开发Mr.Jalen  版权所有(c) ${YEAR}

 *  作者:${USER}

 *  版本: 1.0
 *
 *
 *  创建日期 : ${DATE}  ${HOUR}:${MINUTE}
 *
 *
 *  邮箱：Studylifetime@sina.com
 *
 *  描述:
 *
 *
 *  修订历史:
 *
 * =========================================================
 */
public class FragmetFactory {
    private static HashMap<Integer, BaseNewsFragment> mFragments = new HashMap<>();

    public static BaseNewsFragment createFragment(int position) {
        //从集合中获得
        BaseNewsFragment fragment = mFragments.get(position);
        //如果为空才开始从Fragment创建新的
        if (null == fragment) {

            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            mFragments.put(position, fragment);
        }
        return fragment;
    }
}
