package com.movieworld.movieboard.domain;

//import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken",timeToLive = 1200)
public class RefreshToken {
    @Id
    public String refreshToken;
    private String nickname;

    public RefreshToken(final String refreshToken, final String nickname){
        this.refreshToken=refreshToken;
        this.nickname=nickname;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public String getNickname(){
        return nickname;
    }
}
