package org.allen.imocker.dto;

/**
 *
 */
public enum RegexEnum {

    INT("{int}", "\\{int\\}", "[0-9]\\\\d*"),
    STRING("{string}", "\\{string\\}", "");


    private String name;

    private String regex;

    private String replacement;

    RegexEnum(String name, String regex, String replacement) {
        this.name = name;
        this.regex = regex;
        this.replacement = replacement;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public String getReplacement() {
        return replacement;
    }
}
