package org.example.wedbanhoa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankSetting {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "bank_name", columnDefinition = "NVARCHAR(150)")
    private String bankName;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "account_holder", columnDefinition = "NVARCHAR(150)")
    private String accountHolder;

    @Column(name = "qr_image", length = 255)
    private String qrImage;
}
