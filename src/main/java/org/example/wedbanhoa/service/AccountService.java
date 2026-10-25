package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.AccountRequest;
import org.example.wedbanhoa.dto.AccountResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public boolean register(AccountRequest request) {
        if (request == null || request.getEmail() == null || accountRepository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            return false;
        }
        Account account = toEntity(request);
        if (account.getRoleId() == null) {
            account.setRoleId(2); // Mặc định role USER/Customer
        }
        accountRepository.save(account);
        return true;
    }

    @Transactional
    public Account loginEntity(String identifier, String password) {
        if (identifier == null || password == null) return null;
        String cleanInput = identifier.trim();
        String cleanPass = password.trim();

        List<Account> accounts = accountRepository.findByEmailOrPhone(cleanInput);
        if (accounts.isEmpty()) {
            accountRepository.findByEmailIgnoreCase(cleanInput).ifPresent(accounts::add);
        }

        for (Account acc : accounts) {
            String storedPass = acc.getPassword() != null ? acc.getPassword().trim() : "";

            // 1. So khớp mật khẩu thuần túy (Plain text)
            if (storedPass.equals(cleanPass)) {
                return acc;
            }

            // 2. Tương thích ngược: Nếu mật khẩu trong DB là chuỗi băm cũ ($2a$, $2b$, $2y$)
            // Chuyển đổi tự động về Plain Text theo mật khẩu người dùng vừa nhập
            if (storedPass.startsWith("$2a$") || storedPass.startsWith("$2b$") || storedPass.startsWith("$2y$")) {
                acc.setPassword(cleanPass);
                accountRepository.save(acc);
                return acc;
            }
        }
        return null;
    }

    @Transactional
    public AccountResponse login(String email, String password) {
        Account acc = loginEntity(email, password);
        return acc != null ? toResponse(acc) : null;
    }

    @Transactional
    public boolean resetPassword(String email, String answer, String newPassword) {
        if (email == null || answer == null || newPassword == null) return false;
        Optional<Account> accOpt = accountRepository.findByEmailIgnoreCase(email.trim());
        if (accOpt.isPresent()) {
            Account acc = accOpt.get();
            String storedAnswer = acc.getSecurityAnswer();
            if (storedAnswer != null && storedAnswer.trim().equalsIgnoreCase(answer.trim())) {
                acc.setPassword(newPassword.trim());
                accountRepository.save(acc);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public AccountResponse getById(Integer id) {
        if (id == null) return null;
        return accountRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Account getEntityById(Integer id) {
        if (id == null) return null;
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllCustomers() {
        return accountRepository.findByRoleIdOrderByUserIdDesc(2).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAllByOrderByUserIdDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public boolean createAccountByAdmin(AccountRequest request) {
        if (request == null || request.getEmail() == null || accountRepository.existsByEmailIgnoreCase(request.getEmail().trim())) {
            return false;
        }
        // Tuyệt đối chỉ có 1 tài khoản Admin duy nhất trong hệ thống! Không cho phép tạo thêm Admin.
        if (request.getRoleId() != null && request.getRoleId() == 1) {
            return false;
        }
        Account account = toEntity(request);
        // Chỉ chấp nhận Manager (3) hoặc Employee (4) khi thêm từ trang quản trị
        if (account.getRoleId() == null || (account.getRoleId() != 3 && account.getRoleId() != 4)) {
            account.setRoleId(4); // Mặc định Nhân viên (Employee)
        }
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            account.setPassword("123456");
        }
        accountRepository.save(account);
        return true;
    }

    @Transactional
    public boolean updateAccountByAdmin(Integer userId, String fullname, String phone, String address, Integer roleId, String newPassword) {
        Account acc = accountRepository.findById(userId).orElse(null);
        if (acc != null) {
            // Không được phép nâng quyền của tài khoản khác lên Admin (roleId = 1)
            if (acc.getRoleId() != null && acc.getRoleId() == 1) {
                // Đang sửa chính Admin gốc: bảo lưu quyền Admin không cho hạ quyền
                roleId = 1;
            } else if (roleId != null && roleId == 1) {
                // Không cho nâng ai lên Admin
                return false;
            }

            if (fullname != null && !fullname.trim().isEmpty()) acc.setFullname(fullname.trim());
            acc.setPhone(phone);
            acc.setAddress(address);
            if (roleId != null) acc.setRoleId(roleId);
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                acc.setPassword(newPassword.trim());
            }
            accountRepository.save(acc);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean changePassword(Integer userId, String oldPass, String newPass) {
        if (userId == null || oldPass == null || newPass == null) return false;
        Account acc = accountRepository.findById(userId).orElse(null);
        if (acc == null || acc.getPassword() == null) return false;
        String storedPass = acc.getPassword().trim();
        if (!storedPass.equals(oldPass.trim()) && !storedPass.startsWith("$2")) {
            return false;
        }
        acc.setPassword(newPass.trim());
        accountRepository.save(acc);
        return true;
    }

    @Transactional
    public boolean deleteAccount(Integer userId) {
        if (userId != null && accountRepository.existsById(userId)) {
            Account acc = accountRepository.findById(userId).orElse(null);
            if (acc != null && acc.getRoleId() != null && acc.getRoleId() == 1) {
                return false;
            }
            accountRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    public AccountResponse updateProfile(AccountRequest req) {
        if (req == null || req.getUserId() == null) return null;
        Optional<Account> accOpt = accountRepository.findById(req.getUserId());
        if (accOpt.isPresent()) {
            Account account = accOpt.get();
            if (req.getFullname() != null) account.setFullname(req.getFullname().trim());
            if (req.getPhone() != null) account.setPhone(req.getPhone().trim());
            if (req.getAddress() != null) account.setAddress(req.getAddress().trim());
            Account saved = accountRepository.save(account);
            return toResponse(saved);
        }
        return null;
    }

    // =========================================================================
    // CÁC HÀM MAPPING NỘI BỘ (DTO <-> ENTITY)
    // =========================================================================

    private Account toEntity(AccountRequest request) {
        if (request == null) return null;
        return Account.builder()
                .userId(request.getUserId())
                .fullname(request.getFullname())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .address(request.getAddress())
                .securityQuestion(request.getSecurityQuestion())
                .securityAnswer(request.getSecurityAnswer())
                .roleId(request.getRoleId() != null ? request.getRoleId() : 2)
                .build();
    }

    private AccountResponse toResponse(Account entity) {
        if (entity == null) return null;
        return AccountResponse.builder()
                .userId(entity.getUserId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .securityQuestion(entity.getSecurityQuestion())
                .securityAnswer(entity.getSecurityAnswer())
                .roleId(entity.getRoleId())
                .roleName(entity.getRoleId() != null && entity.getRoleId() == 1 ? "Admin" : "Customer")
                .build();
    }
}