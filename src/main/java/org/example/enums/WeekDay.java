package org.example.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.xml.bind.annotation.XmlEnum;

@AllArgsConstructor(access= AccessLevel.PRIVATE)
@XmlEnum
public enum WeekDay {

    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

}
