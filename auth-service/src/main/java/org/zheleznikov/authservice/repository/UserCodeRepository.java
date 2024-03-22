package org.zheleznikov.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zheleznikov.authservice.entity.UserCode;

// CRUD vsJPA ? не успел посмотреть
public interface UserCodeRepository extends JpaRepository<UserCode, Long> {
}
