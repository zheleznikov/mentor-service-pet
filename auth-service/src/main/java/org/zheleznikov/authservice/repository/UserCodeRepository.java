package org.zheleznikov.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zheleznikov.authservice.entity.UserCode;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {
}
