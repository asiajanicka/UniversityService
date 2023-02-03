package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Grade {

    @JsonProperty
    private long id;

    @JsonProperty
    private int value;

    @JsonProperty
    private Subject subject;

    public Grade(int value) {
        this.value = value;
    }

    public Grade(int value, Subject subject) {
        this.value = value;
        this.subject = subject;
    }

}