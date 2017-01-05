package org.allen.imocker.dto;

public enum StatusEnum {

    NO,
    YES;

    public static final StatusEnum getStatusEnumByValue(int value) {
        StatusEnum se = null;
        for (StatusEnum e : StatusEnum.values()) {
            if (e.ordinal() == value) {
                se = e;
                break;
            }
        }
        return se;
    }

}
