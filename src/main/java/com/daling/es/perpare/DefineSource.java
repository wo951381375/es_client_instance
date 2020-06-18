package com.daling.es.perpare;

/**
 * @Description:
 * @Author: ZhangTeng
 * @Date: 2020/4/9
 */
public interface DefineSource {

    /**
     * @Description 设置_source 展示数据
     * @Author ZhangTeng
     * @Date 2020/4/9
     */
    String[] getSourceIncludes(Object obj);

    /**
     * @Description 设置_source 非展示数据
     * @Author ZhangTeng
      * @Date 2020/4/9
     */
    String[] getSourceExcludes(Object obj);
}
