package org.zheleznikov.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.zheleznikov.generated.model.Role;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserEntity {
    /*
    Как вообще такая модель данных? Может быть нужно еше что-то?
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

    private LocalDateTime signupTimestamp;

    private LocalDateTime confirmTimestamp;

    private LocalDateTime lastLoginTimestamp;

    private Boolean isConfirmed;

    @OneToMany
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "userId")
    private List<UserCode> userCode;

}
