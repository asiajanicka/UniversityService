package org.example.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortalAccount)) return false;
        PortalAccount account = (PortalAccount) o;
        return Objects.equals(login, account.login)
                && Objects.equals(password, account.password)
                && Objects.equals(issueDate, account.issueDate)
                && Objects.equals(expiryDate, account.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, issueDate, expiryDate);
    }

}
