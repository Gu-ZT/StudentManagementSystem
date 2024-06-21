package dev.dubhe.sms.manager.service.impl;

import cn.hutool.core.util.IdUtil;
import dev.dubhe.sms.manager.data.dao.IUserDao;
import dev.dubhe.sms.manager.data.pojo.User;
import dev.dubhe.sms.manager.exception.CustomException;
import dev.dubhe.sms.manager.redis.RedisCache;
import dev.dubhe.sms.manager.service.ITokenService;
import dev.dubhe.sms.manager.util.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService implements ITokenService {
    @Value("${token.header}")
    private String header;
    @Value("${token.secret}")
    private String secret;
    private final RedisCache redisCache;
    private final IUserDao userDao;

    public String createToken(@Nonnull User user) {
        Date now = DateUtil.now();
        Date expiration = DateUtil.afterDays(now, 1);
        String token = IdUtil.fastSimpleUUID();
        this.redisCache.setCacheObject("TOKEN-" + token, user.getId(), 1, TimeUnit.DAYS);
        return Jwts.builder()
                .issuer("Gugle")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .claim("token", token)
                .compact();
    }

    public User parserToken(String token) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String uuid = payload.get("token", String.class);
            Long uid = this.redisCache.getCacheObject("TOKEN-" + uuid);
            if (uid == null) throw CustomException.unauthorized();
            return this.userDao.getById(uid);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("token解析失败: {}", token);
            return null;
        }
    }

    @Override
    public String getToken(@Nonnull HttpServletRequest request) {
        return request.getHeader(this.header);
    }
}
