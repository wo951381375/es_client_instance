package es.perpare;

import es.enums.QueryEnum;
import org.elasticsearch.index.query.BoolQueryBuilder;

public interface PrepareQuery {

        BoolQueryBuilder getBoolQueryBuilder(Object obj, QueryEnum queryEnum);

}
