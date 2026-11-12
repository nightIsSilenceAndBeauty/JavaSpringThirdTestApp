package ru.trints.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessages {
    EMPTY(""),
    VALIDATION("Ошибка валидации"),
    UNSUPPORTED("uid 123 не поддерживается нашей системой"),
    UNKNOWN("Произошла непредвиденная ошибка");

    private final String value;

    ErrorMessages(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
