package com.example.BackEndSocial.controller;

import com.example.BackEndSocial.DTO.OtpDTO;
import com.example.BackEndSocial.config.JwtProvider;
import com.example.BackEndSocial.model.USER_ROLE;
import com.example.BackEndSocial.model.User;
import com.example.BackEndSocial.repository.UserRepository;
import com.example.BackEndSocial.request.ForgotPasswordRequest;
import com.example.BackEndSocial.request.LoginRequest;
import com.example.BackEndSocial.request.ResetPassRequest;
import com.example.BackEndSocial.response.AuthResponse;
import com.example.BackEndSocial.response.PasswordResponse;
import com.example.BackEndSocial.service.EmailService;
import com.example.BackEndSocial.service.ProUserDetailsService;
import com.example.BackEndSocial.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ProUserDetailsService proUserDetailsService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception{
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist!=null){
            throw new Exception("Email is already used with another account");
        }
        User createdUser= new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register success");
        authResponse.setRole(saveUser.getRole());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) {
        String username = req.getEmail();
        String password = req.getPassword();
        Authentication authentication = authenticate(username, password);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(USER_ROLE.valueOf(role));
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails= proUserDetailsService.loadUserByUsername(username);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid username...");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) throws Exception {
        User user = userService.findUserByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Email không tồn tại trong hệ thống");
        }
        String otpToken = jwtProvider.generateOtpToken(request.getEmail());
        String otpCode = jwtProvider.extractOtpFromToken(otpToken); // Lấy mã OTP từ token

        emailService.sendEmail(request.getEmail(), "Mã OTP đặt lại mật khẩu", otpCode);

        return ResponseEntity.ok(otpToken);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<PasswordResponse> verifyOtp(@RequestBody OtpDTO request) {
        String token = request.getToken();
        String otp = request.getOtp();
        if (token == null || otp == null) {
            return ResponseEntity.badRequest().body(new PasswordResponse(null, "Token hoặc OTP không được để trống."));
        }

        boolean isValid = jwtProvider.validateOtpToken(token, otp);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PasswordResponse(null, "OTP không hợp lệ hoặc đã hết hạn."));
        }

        String email = jwtProvider.getEmailFromJwtToken(token);
        String tempJwt = jwtProvider.generateTemporaryToken(email);
        System.out.println("Email: " + email);
        return ResponseEntity.ok(new PasswordResponse(tempJwt, "OTP hợp lệ. Bạn có thể đặt lại mật khẩu."));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestHeader("Authorization") String token,
                                                @RequestBody ResetPassRequest request) {
        String email = jwtProvider.getEmailFromJwtToken(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token không hợp lệ hoặc đã hết hạn.");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("Không tìm thấy tài khoản với email: " + email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy tài khoản.");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
    }


}
