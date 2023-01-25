package org.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum WeekDay {

    @JsonProperty
    MONDAY,
    @JsonProperty
    TUESDAY,
    @JsonProperty
    WEDNESDAY,
    @JsonProperty
    THURSDAY,
    @JsonProperty
    FRIDAY,
    @JsonProperty
    SATURDAY,
    @JsonProperty
    SUNDAY

}