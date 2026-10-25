package org.example.wedbanhoa.repository;

import org.example.wedbanhoa.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByEmailIgnoreCase(String email);

    @Query("SELECT a FROM Account a WHERE LOWER(TRIM(a.email)) = LOWER(TRIM(:identifier)) OR TRIM(a.phone) = TRIM(:identifier)")
    List<Account> findByEmailOrPhone(@Param("identifier") String identifier);

    List<Account> findByRoleIdOrderByUserIdDesc(Integer roleId);
    List<Account> findAllByOrderByUserIdDesc();
    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
}