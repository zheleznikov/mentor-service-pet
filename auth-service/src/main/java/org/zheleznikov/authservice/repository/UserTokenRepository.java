package org.zheleznikov.authservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.zheleznikov.authservice.entity.UserToken;

public interface UserTokenRepository extends CrudRepository<UserToken, Long> {

    UserToken findByAccessToken(String accessToken);


}
