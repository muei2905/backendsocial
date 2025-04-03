package com.example.BackEndSocial.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail); // Dùng email từ application.properties
        helper.setTo(to);
        helper.setSubject(subject);

        // Nội dung HTML cải tiến
        String htmlContent = "<html><body>" +
                "<h2>Xin chào,</h2>" +
                "<p>Mã OTP của bạn để đặt lại mật khẩu là: <strong>" + text + "</strong></p>" +
                "<p>Mã này có hiệu lực trong 10 phút.</p>" +
                "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>" +
                "<p>Trân trọng,<br>Đội ngũ hỗ trợ</p>" +
                "</body></html>";
        helper.setText(htmlContent, true); // true để hỗ trợ HTML

        mailSender.send(message);
        System.out.println("Email đã được gửi thành công đến: " + to);
    }
}
