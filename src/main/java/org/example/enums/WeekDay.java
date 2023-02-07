package org.example.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlEnum;

@AllArgsConstructor(access= AccessLevel.PRIVATE)
@XmlEnum
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