package org.example.model;

import lombok.*;
import org.example.utils.LocalDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@XmlRootElement(name = "portalAccount")
@XmlAccessorType(XmlAccessType.FIELD)
public class PortalAccount {

    @XmlAttribute
    private long id;
    @XmlElement
    private String login;
    @XmlElement
    private String password;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate issueDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate expiryDate;

    public PortalAccount(String login, String password, LocalDate issueDate, LocalDate expiryDate) {
        this.login = login;
        this.password = password;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

}
