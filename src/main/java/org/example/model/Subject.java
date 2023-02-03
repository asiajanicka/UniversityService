package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Subject {

    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    public Subject(String name) {
        this.name = name;
    }

}