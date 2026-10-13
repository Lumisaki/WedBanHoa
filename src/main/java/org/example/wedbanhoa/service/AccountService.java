package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.AccountRequest;
import org.example.wedbanhoa.dto.AccountResponse;
import org.example.wedbanhoa.entity.Account;
import org.example.wedbanhoa.repository.AccountRepository;
import org.example.wedbanhoa.utils.BCrypt;
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
        if (accountRepository.existsByEmail(request.getEmail())) {
            return false;
        }
        Account account = toEntity(request); // Gọi hàm mapping trực tiếp ở đây
        if (account.getRoleId() == null) {
            account.setRoleId(2); // Mặc định role USER/Customer
        }
        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
        }
        if (account.getSecurityAnswer() != null && !account.getSecurityAnswer().isEmpty()) {
            account.setSecurityAnswer(BCrypt.hashpw(account.getSecurityAnswer(), BCrypt.gensalt()));
        }
        accountRepository.save(account);
        return true;
    }

    @Transactional
    public boolean register(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            return false;
        }
        if (account.getRoleId() == null) {
            account.setRoleId(2);
        }
        if (account.getPassword() != null) {
            account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
        }
        if (account.getSecurityAnswer() != null) {
            account.setSecurityAnswer(BCrypt.hashpw(account.getSecurityAnswer(), BCrypt.gensalt()));
        }
        accountRepository.save(account);
        return true;
    }

    @Transactional
    public Account loginEntity(String email, String password) {
        Optional<Account> accOpt = accountRepository.findByEmail(email);
        if (accOpt.isPresent()) {
            Account acc = accOpt.get();
            String storedHash = acc.getPassword();
            boolean matches = false;

            if (storedHash != null) {
                if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
                    try {
                        matches = BCrypt.checkpw(password, storedHash);
                    } catch (Exception e) {
                        matches = false;
                    }
                } else {
                    matches = storedHash.equals(password);
                    if (matches) {
                        acc.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
                        accountRepository.save(acc);
                    }
                }
            }

            if (matches) {
                return acc;
            }
        }
        return null;
    }

    @Transactional
    public AccountResponse login(String email, String password) {
        Account acc = loginEntity(email, password);
        return acc != null ? toResponse(acc) : null; // Gọi hàm mapping trực tiếp
    }

    @Transactional
    public boolean resetPassword(String email, String answer, String newPassword) {
        Optional<Account> accOpt = accountRepository.findByEmail(email);
        if (accOpt.isPresent()) {
            Account acc = accOpt.get();
            String storedAnswerHash = acc.getSecurityAnswer();
            boolean answerMatches = false;

            if (storedAnswerHash != null) {
                if (storedAnswerHash.startsWith("$2a$") || storedAnswerHash.startsWith("$2b$") || storedAnswerHash.startsWith("$2y$")) {
                    try {
                        answerMatches = BCrypt.checkpw(answer, storedAnswerHash);
                    } catch (Exception e) {
                        answerMatches = false;
                    }
                } else {
                    answerMatches = storedAnswerHash.equalsIgnoreCase(answer);
                }
            }

            if (answerMatches) {
                acc.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                accountRepository.save(acc);
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public AccountResponse getById(Integer id) {
        return accountRepository.findById(id)
                .map(this::toResponse) // Gọi hàm mapping trực tiếp qua method reference
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Account getEntityById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllCustomers() {
        return accountRepository.findByRoleIdOrderByUserIdDesc(2).stream()
                .map(this::toResponse) // Dùng Stream API kết hợp hàm mapping nội bộ
                .toList();
    }

    @Transactional
    public AccountResponse updateProfile(AccountRequest request) {
        Account acc = accountRepository.findById(request.getUserId()).orElse(null);
        if (acc != null) {
            acc.setFullname(request.getFullname());
            acc.setPhone(request.getPhone());
            acc.setAddress(request.getAddress());
            Account saved = accountRepository.save(acc);
            return toResponse(saved); // Gọi hàm mapping trực tiếp
        }
        return null;
    }

    @Transactional
    public Account updateProfile(Account account) {
        return accountRepository.save(account);
    }

    // =========================================================================
    // CÁC HÀM MAPPING ĐƯỢC CHUYỂN VÀO ĐÂY (ĐÃ BỎ @Autowired AccountMapper)
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
                .roleId(entity.getRoleId())
                .roleName(entity.getRoleId() != null && entity.getRoleId() == 1 ? "Admin" : "Customer")
                .build();
    }

    private AccountRequest toRequest(Account entity) {
        if (entity == null) return null;
        return AccountRequest.builder()
                .userId(entity.getUserId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .securityQuestion(entity.getSecurityQuestion())
                .roleId(entity.getRoleId())
                .build();
    }
}