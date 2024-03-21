package org.zheleznikov.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zheleznikov.authservice.entity.UserEntity;


public interface UserRepository  extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
