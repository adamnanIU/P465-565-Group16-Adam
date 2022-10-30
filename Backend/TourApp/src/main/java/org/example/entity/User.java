package org.example.entity;

import org.example.validation.ValidEmail;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "User", schema = "dop")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @ValidEmail
    private String email;

    @Column(name = "contactNo")
    private Long contactNo;

    @Column(name = "securityQuestion")
    private String securityQuestion;

    @Column(name = "securityAnswer")
    private String securityAnswer;

    @Column(name = "emailEnabled")
    private boolean emailEnabled;

    @Column(name = "phoneEnabled")
    private boolean phoneEnabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public boolean getEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isPhoneEnabled() {
        return phoneEnabled;
    }

    public void setPhoneEnabled(boolean phoneEnabled) {
        this.phoneEnabled = phoneEnabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", contactNo=" + contactNo +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", emailEnabled=" + emailEnabled +
                ", phoneEnabled=" + phoneEnabled +
                '}';
    }
}
