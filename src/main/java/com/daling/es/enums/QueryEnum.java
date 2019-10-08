package com.daling.es.enums;

public enum QueryEnum {
        termsQuery(1,"termsQuery","精确查询"),
        matchQuery(2,"matchQuery","分词查询"),
        matchPhrasePrefixQuery(3,"matchPhrasePrefixQuery","前缀匹配查询"),
        matchPhraseQuery(4,"matchPhraseQuery","包含查询"),
        wildCard(5,"wildCard","正则匹配查询"),
        UnKnown(99, "unKnown", "未知");
        ;

        int id;
        String value;
        String desc;

        QueryEnum(int id, String value, String desc) {
        }

        QueryEnum() {
        }

        public static QueryEnum fromValue(String value){
                for (QueryEnum query : QueryEnum.values()){
                        if (query.getValue().equals(value)){
                              return query;
                        }
                }
                return UnKnown;
        }

        public int getId() {
                return id;
        }

        public String getValue() {
                return value;
        }

        public String getDesc() {
                return desc;
        }

}
