package org.example.wedbanhoa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(100)")
    private String fullname;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "security_question", columnDefinition = "NVARCHAR(255)")
    private String securityQuestion;

    @Column(name = "security_answer", columnDefinition = "NVARCHAR(255)")
    private String securityAnswer;

    @Column(name = "role_id")
    private Integer roleId;
}
