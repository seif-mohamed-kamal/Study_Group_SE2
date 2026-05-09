package com.studygroup.studygroup.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    STUDENT,
    CREATOR,
    ADMIN;

    @JsonCreator
    public static Role from(String value) {
        if (value == null) return null;
        return Role.valueOf(value.toUpperCase());
    }
}