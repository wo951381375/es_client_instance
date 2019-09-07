package es.perpare;

import es.enums.QueryEnum;
import es.utils.JsonNoNullUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PrepareQueryImpl implements PrepareQuery {

        private static final String KEYWORD = ".keyword";

        private static final String MUSTNOT = "un_";

        @Override
        public BoolQueryBuilder getBoolQueryBuilder(Object obj, QueryEnum queryEnum) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                Map<String, Object> map = JsonNoNullUtil.transFormationMapNoNullVal(obj,Map.class);
                if (Objects.isNull(map) || map.isEmpty()){
                        return boolQuery;
                }
                Set<Map.Entry<String, Object>> entries = map.entrySet();
                for (Map.Entry<String, Object> entry : entries){
                        String field = entry.getKey();
                        Object value = entry.getValue();
                        //过滤查询统一使用精确查询
                        boolean mustNotFlag =  this.checkMustNotField(field);
                        //value非String类型查询使用 精确查询 不需要加 keyWord标识
                        boolean valFlag =  this.checkValType(value);
                        if (mustNotFlag){
                                field = field.substring(MUSTNOT.length())+KEYWORD;
                                if (value instanceof List){
                                        boolQuery.mustNot(QueryBuilders.termsQuery(field, (List)value));
                                }else {
                                        boolQuery.mustNot(QueryBuilders.termsQuery(field, value));
                                }
                        }else if (valFlag){
                                // 使用自定义查询方式, 默认使用精确查询
                                QueryBuilder builder = this.getStringQueryBuilder(field,value, queryEnum);
                                boolQuery.must(builder);
                        }else {
                                if (value instanceof List){
                                        boolQuery.must(QueryBuilders.termsQuery(field, (List)value));
                                }else {
                                        boolQuery.must(QueryBuilders.termsQuery(field, value));
                                }
                        }
                }
                return boolQuery;
        }

        /**
         * 校验使用自定义查询方式
         * 非String类型使用 精确查询
         * */
        private boolean checkValType(Object value) {
                if (value.getClass() == String.class){
                        return true;
                }
                return false;
        }

        /**
         * 过滤查询规则, 前缀为 un_
         * */
        private boolean checkMustNotField(String field) {
                String substring = field.length()<MUSTNOT.length()?field:field.substring(0, MUSTNOT.length());
                return MUSTNOT.equals(substring);
        }


        /**
         * 根据查询条件拼接 查询数据
         * */
        public static QueryBuilder getStringQueryBuilder(String field, Object value, QueryEnum queryEnum) {
                QueryBuilder builder = null;
                switch (queryEnum){
                        case matchQuery:
                                builder = QueryBuilders.matchQuery(field, value);
                                break;
                        case matchPhraseQuery:
                                builder = QueryBuilders.matchPhraseQuery(field, value);
                                break;
                        case matchPhrasePrefixQuery:
                                builder = QueryBuilders.matchPhrasePrefixQuery(field, value);
                                break;
                        case wildCard:
                                builder = QueryBuilders.wildcardQuery(field+KEYWORD,"*"+value.toString()+"*");
                                break;
                        default:
                                //String类型做精确查询需要使用keyword标识
                                if (value instanceof List){
                                        builder = QueryBuilders.termsQuery(field+KEYWORD, (List)value);
                                }else {
                                        builder = QueryBuilders.termsQuery(field+KEYWORD, value);
                                }
                                break;
                }
                return builder;
        }

}
