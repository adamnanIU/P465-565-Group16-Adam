package org.example.controller;

import jakarta.ws.rs.QueryParam;
import org.example.entity.User;
import org.example.model.Login;
import org.example.service.OtpService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @PostMapping()
    public ResponseEntity<User> add(@RequestBody User user, HttpServletRequest request) throws Exception{
        System.out.println("User details are : "+ user);
        System.out.println("site url is : " + getSiteURL(request));
        User newUser = userService.add(user, getSiteURL(request));
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/verifyEmail", params = "code")
    public ResponseEntity<String> emailVerificationCode(@QueryParam(value = "code") String code) throws Exception{
        String result = userService.verifyEmailCode(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/verifyOtp", params = "code")
    public ResponseEntity<String> otpVerification(@QueryParam(value = "code") Integer code) throws Exception {
        String result = otpService.verifyOtp(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody Login login) throws Exception{
        String response = userService.loginUser(login);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/resend/email")
    public ResponseEntity<String> emailReVerification(@QueryParam(value = "email") String email, HttpServletRequest httpServletRequest) throws Exception {
        String result = userService.emailReVerification(email, getSiteURL(httpServletRequest));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
