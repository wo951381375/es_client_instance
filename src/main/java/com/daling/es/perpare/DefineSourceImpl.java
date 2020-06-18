package com.daling.es.perpare;

/**
 * @Description:
 * @Author: ZhangTeng
 * @Date: 2020/4/9
 */
public class DefineSourceImpl implements DefineSource{

    @Override
    public String[] getSourceIncludes(Object obj) {
        return new String[0];
    }

    @Override
    public String[] getSourceExcludes(Object obj) {
        return new String[0];
    }
}
