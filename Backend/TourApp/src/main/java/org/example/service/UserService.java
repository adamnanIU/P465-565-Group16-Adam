package org.example.service;

import net.bytebuddy.utility.RandomString;
import org.example.entity.User;
import org.example.entity.VerificationToken;
import org.example.exception.EmailVerificationException;
import org.example.exception.GeneralException;
import org.example.exception.UserAlreadyExistsException;
import org.example.model.Login;
import org.example.repo.UserRepository;
import org.example.repo.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpService otpService;

    @Autowired
    private Environment environment;

    @Autowired
    private Encryption encryption;

    public UserService(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository,
                       JavaMailSender mailSender, OtpService otpService,
                       Environment environment, Encryption encryption) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailSender = mailSender;
        this.otpService = otpService;
        this.environment = environment;
        this.encryption = encryption;
    }

    public User add(User user, String siteUrl) throws Exception{
        System.out.println("service user value is : " + user);
        User user1 = userRepository.findByEmail(user.getEmail());
        if (user1 == null){
            user.setEmailEnabled(false);
            user.setPhoneEnabled(false);
            String password = user.getPassword();
            user.setPassword(encryption.encrypt(password));
            userRepository.save(user);
            sendEmailVerification(user, siteUrl);
            if (user.getContactNo() != null) {
                otpService.generateOtp(user);
            }
            return user;
        } else {
            throw new UserAlreadyExistsException("User already exists with the same email address: " + user.getEmail());
        }

    }

    public String emailReVerification(String email, String siteUrl) throws Exception {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new GeneralException("No user found with email " + email);
        }
        if (user.isEmailEnabled()) {
            throw new GeneralException("Email is already verified");
        }
        sendEmailVerification(user, siteUrl);
        return "success";
    }

    private void sendEmailVerification(User user, String siteUrl) throws Exception {
        User user1 = userRepository.findByEmail(user.getEmail());
        String randomCode = RandomString.make(64);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user1);
        verificationToken.setToken(randomCode);
        verificationToken.setExpiryDate(calculateExpiryDate());
        verificationTokenRepository.save(verificationToken);
        String toAddress = user.getEmail();
        String fromAddress = "adarsha.reddy98@gmail.com";
        String senderName = "National Camping Services";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "[[companyName]].";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);
        content = content.replace("[[name]]", user.getName());
        content = content.replace("[[companyName]]", senderName);
        String verifyUrl = siteUrl + "/user/verifyEmail?code=" + randomCode;
        content = content.replace("[[URL]]", verifyUrl);
        mimeMessageHelper.setText(content, true);
        mailSender.send(message);
    }

    @Transactional
    public String verifyEmailCode(String code) throws EmailVerificationException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(code);
        if (verificationToken == null) {
            throw new EmailVerificationException("Email has already been verified or the link has expired");
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime today = LocalDateTime.now();
        Date now = new Date(dateTimeFormatter.format(today));
        if (verificationToken.getExpiryDate().compareTo(now) >= 0) {
            User user = userRepository.getOne(verificationToken.getUser().getId());
            user.setEmailEnabled(true);
            userRepository.save(user);
            /*verificationToken.setToken(null);
            verificationToken.setExpiryDate(null);*/
            verificationTokenRepository.delete(verificationToken);/*
            List<VerificationToken> verificationTokenList = verificationTokenRepository.findByUserId(user.getId());
            for (int i=0;i<verificationTokenList.size();i++) {
                verificationTokenRepository.delete(verificationTokenList.get(i));
            }*/
            return "success";
        } else {
            throw new EmailVerificationException("Link has expired");
        }

    }

    public String loginUser(Login login) throws GeneralException{
        User user = userRepository.findByEmail(login.getUserName());
        if (user == null) {
            throw new GeneralException("User not found Exception");
        }
        if (!user.isEmailEnabled()) {
            throw new GeneralException("Account is not yet activated");
        }
        String decodePassword = encryption.decrypt(user.getPassword());
        if (decodePassword.equals(login.getPassword())) {
            return "Login success";
        }
        throw new GeneralException("Incorrect Password");
    }

    private Date calculateExpiryDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        return new Date(dateTimeFormatter.format(tomorrow));
    }

    private String passwordEncoder(String password) {
        String encodedString = "";
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getPropertyValue("encrypt.key").getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypted=cipher.doFinal(password.getBytes());
            encodedString = new String(encrypted);

        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }
        return encodedString;
    }

    private String passwordDecode(String password) {
        String decodedString="";

        try {
            System.out.println(getPropertyValue("encrypt.key"));
            SecretKeySpec skeyspec=new SecretKeySpec(getPropertyValue("encrypt.key").getBytes(),"Blowfish");
            Cipher cipher=Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] decrypted=cipher.doFinal(password.getBytes());
            decodedString = new String(decrypted);

        } catch (Exception e) {
            throw new GeneralException(e.getMessage());
        }
        return decodedString;
    }

    private String getPropertyValue(String key) {
        return environment.getProperty(key);
    }
}
