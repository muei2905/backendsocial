package com.example.BackEndSocial.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtProvider {
    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities= auth.getAuthorities();
        String roles = populateAuthorities(authorities);
        String jwt= Jwts.builder().setIssuedAt(new Date())
                .setExpiration((new Date(new Date().getTime()+7 * 24 * 60 * 60 * 1000)))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key)
                .compact();
        return jwt;
    }

    public String getEmailFromRawJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // subject chứa email trong OTP token
    }


    public String getEmailFromJwtToken(String jwt){
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        // Trường hợp có "email" claim → dùng, không thì lấy subject
        String email = claims.get("email", String.class);
        if (email == null) {
            email = claims.getSubject();
        }

        return email;
    }

    private String populateAuthorities(Collection <? extends GrantedAuthority> authorities){
        Set<String> auths= new HashSet<>();
        for (GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    public String generateOtpToken(String email) {
        String otp = generateOtp(); // Tạo mã OTP
        return Jwts.builder()
                .setSubject(email)
                .claim("otp", otp)
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 5 phút
                .signWith(key)
                .compact();
    }
    public String extractOtpFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("otp", String.class);
    }
    private String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.valueOf(otp);
    }

    public boolean validateOtpToken(String token, String enteredOtp) {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            String otp = claims.get("otp", String.class); // Lấy OTP từ JWT
            return otp.equals(enteredOtp); // So sánh với OTP user nhập
    }

    public String generateTemporaryToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // Hết hạn sau 5 phút
                .signWith(key)
                .compact();
    }

}