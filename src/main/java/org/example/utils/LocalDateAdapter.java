package org.example.utils;

import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate unmarshal(String date) {
        return LocalDate.parse(date, formatter);
    }

    @Override
    public String marshal(LocalDate date) {
        return date.format(formatter);
    }

}