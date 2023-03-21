package ru.sevavto.stock.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.sevavto.stock.crm.exception.NotFoundException;
import ru.sevavto.stock.crm.exception.NotValidRefreshTokenException;
import ru.sevavto.stock.crm.model.dto.security.AuthenticationRequest;
import ru.sevavto.stock.crm.model.dto.security.AuthenticationResponse;
import ru.sevavto.stock.crm.model.dto.security.TokenRefreshRequest;
import ru.sevavto.stock.crm.model.entity.CrmUser;
import ru.sevavto.stock.crm.repository.CrmUserRepository;
import ru.sevavto.stock.crm.security.JwtUtils;


@Service
public class AuthenticationService {
    private final CrmUserRepository crmUserRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public AuthenticationService(CrmUserRepository crmUserRepository,
                                 JwtUtils jwtUtils,
                                 AuthenticationManager authenticationManager,
                                 StringRedisTemplate stringRedisTemplate) {
        this.crmUserRepository = crmUserRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        CrmUser crmUser = crmUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException(""));
        //todo большой вопрос в предназначении данного метода, т.к. в фильтре мы так же используем .setAuthentication с UsernamePasswordAuthenticationToken
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword(), crmUser.getAuthorities())); //в гайде использовался без authorities
        String accessToken = jwtUtils.generateAccessToken(crmUser);
        String refreshToken = jwtUtils.generateRefreshToken(crmUser);
        stringRedisTemplate.opsForValue().set(crmUser.getEmail(), refreshToken); //todo: может бросать исключение при недоступном редис
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(TokenRefreshRequest request) {
        String username = jwtUtils.getUsernameFromToken(request.getRefreshToken());
        String refreshTokenAtRequest = request.getRefreshToken();
        String refreshTokenAtDb = stringRedisTemplate.opsForValue().get(username);
        CrmUser crmUser = crmUserRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким логином не найден в БД"));
        if(refreshTokenAtDb != null
                && refreshTokenAtDb.equals(refreshTokenAtRequest)
                && jwtUtils.isRefreshTokenValid(refreshTokenAtRequest, crmUser)) {
            String accessToken = jwtUtils.generateAccessToken(crmUser);
            String refreshToken = jwtUtils.generateRefreshToken(crmUser);
            stringRedisTemplate.opsForValue().set(username, refreshToken);
            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            throw new NotValidRefreshTokenException("Передан невалидный refresh токен");
        }
    }
}
