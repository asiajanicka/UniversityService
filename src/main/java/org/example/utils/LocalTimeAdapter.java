package org.example.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");

    @Override
    public LocalTime unmarshal(String time) {
        return LocalTime.parse(time, formatter);
    }

    @Override
    public String marshal(LocalTime time) {
        return time.format(formatter);
    }

}
