package org.example.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class PortalAccount {

    private long id;
    private String login;
    private String password;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    public PortalAccount(String login, String password, LocalDate issueDate, LocalDate expiryDate) {
        this.login = login;
        this.password = password;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

}
