package com.flintzy.socialtool.repository;

import com.flintzy.socialtool.model.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    
    Optional<SocialAccount> findByPageId(String pageId);
    
    List<SocialAccount> findByUserEmail(String email);
}
