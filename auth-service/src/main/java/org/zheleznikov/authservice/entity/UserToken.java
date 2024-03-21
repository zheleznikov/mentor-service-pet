package org.zheleznikov.authservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("Token")
@Getter
@Setter
@Accessors(chain = true)
@Data
public class UserToken implements Serializable {

    @Indexed // надо проверить, надо ли
    private Long id;

    private String email;

    @Indexed
    private String accessToken;

    private String refreshToken;

    private LocalDateTime expired; // ?
}
