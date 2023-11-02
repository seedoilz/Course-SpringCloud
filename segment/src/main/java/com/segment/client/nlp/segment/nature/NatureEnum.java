package com.segment.client.nlp.segment.nature;

public enum NatureEnum {
    /**
     * 词性
     */

    AdvAndDisadv("优势劣势", "#优势劣势"),

    Use("使用场景", "#使用场景"),

    Entity("实体", "#实体"),

    Sentiment("情绪", "#情绪"),

    Other("其它", "#其它#");

    private String name;

    private String tag;


    @Override
    public String toString() {
        return "NatureEnum{" +
                "name='" + name + '\'' +
                '}';
    }

    NatureEnum(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public static NatureEnum parser(String name) {
        for (NatureEnum type : NatureEnum.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return Other;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }
}
