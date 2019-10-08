package com.daling.es.perpare;

import com.daling.es.enums.QueryEnum;
import org.elasticsearch.index.query.BoolQueryBuilder;

public interface PrepareQuery {

        BoolQueryBuilder getBoolQueryBuilder(Object obj, QueryEnum queryEnum);

}
