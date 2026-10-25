package org.example.wedbanhoa.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Integer userId;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String securityQuestion;
    private String securityAnswer;
    private Integer roleId;
    private String roleName;

    // Helper for template checks
    public boolean isAdmin() {
        return roleId != null && roleId == 1;
    }

    public boolean isManager() {
        return roleId != null && roleId == 3;
    }

    public boolean isEmployee() {
        return roleId != null && roleId == 4;
    }

    public boolean isCustomer() {
        return roleId != null && roleId == 2;
    }

    public String getRoleTitle() {
        if (roleId == null) return "Khách hàng";
        return switch (roleId) {
            case 1 -> "Quản trị viên (Admin)";
            case 3 -> "Quản lý (Manager)";
            case 4 -> "Nhân viên (Employee)";
            default -> "Khách hàng";
        };
    }
}
