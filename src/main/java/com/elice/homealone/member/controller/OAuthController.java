package com.elice.homealone.member.controller;


import com.elice.homealone.member.dto.TokenDto;
import com.elice.homealone.member.dto.response.KakaoUserResponse;
import com.elice.homealone.member.entity.Member;
import com.elice.homealone.member.service.AuthService;
import com.elice.homealone.member.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "AuthController", description = "OAUTH2.0 인증 관리 API")
public class OAuthController {
    @Value("${naver.url}")
    private String NAVER_URL;
    @Value("${kakao.url}")
    private String KAKAO_URL;
    private final OAuthService oAuthService;
    private final AuthService authService;
    @Operation(summary = "네이버 로그인 페이지 이동")
    @GetMapping("/naver")
    public String naverLoginRedirect() {
        return NAVER_URL;
    }
    @Operation(summary = "네이버 로그인")
    @GetMapping("/naver/login")
    public ResponseEntity<TokenDto> naverLogin(@RequestParam(name = "code") String code
            , HttpServletResponse httpServletResponse) {
        Member member = oAuthService.toEntityUser(code);
        TokenDto tokenDto = oAuthService.signupOrLogin(member, httpServletResponse);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", tokenDto.getAccessToken());
        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }
    @Operation(summary = "카카오 로그인 페이지 이동")
    @GetMapping("/kakao")
    public String kakaoResponseUrl() {
        return KAKAO_URL;
    }
    @Operation(summary = "카카오 로그인")
    @PostMapping("/kakao/login")
    public ResponseEntity<TokenDto> kakaoLogin (@RequestBody Map<String, String> body, HttpServletResponse httpServletResponse) {
        KakaoUserResponse kakaoUserDto = oAuthService.getKakaoUserInfo(body.get("accessToken"));
        //자동 로그인
        TokenDto tokenDto = authService.login(kakaoUserDto.toLoginRequestDto(), httpServletResponse);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", tokenDto.getAccessToken());
        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }
}