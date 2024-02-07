package com.movieworld.movieboard.Service;

import com.movieworld.movieboard.Repository.RefreshTokenRepository;
import com.movieworld.movieboard.domain.RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenRefreshService {
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenRefreshService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public boolean match(String token, String subject) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(token);

        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();
            return refreshToken.getNickname().equals(subject);
        } else {
            return false;
        }
    }

    public void updateRefreshToken(String prevRefreshToken, String newRefreshToken, String nickname){
        refreshTokenRepository.deleteById(prevRefreshToken);
        RefreshToken refreshToken=new RefreshToken(newRefreshToken,nickname);
        refreshTokenRepository.save(refreshToken);
    }

}
