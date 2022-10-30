package org.example.service;


import com.twilio.twiml.voice.Pay;
import org.example.entity.CreditCard;
import org.example.entity.Payment;
import org.example.exception.CreditCardInvalid;
import org.example.exception.UserAlreadyPaid;
import org.example.repo.CreditCardRepository;
import org.example.repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private JavaMailSender mailSender;

    public PaymentService(PaymentRepository paymentRepository, JavaMailSender mailSender){
        this.paymentRepository = paymentRepository;
        this.mailSender = mailSender;
    }


    public Payment addPayment(Payment payment) throws Exception{
        System.out.println("service payment value is : " + payment);
        Payment payment1 = paymentRepository.findPaymentById(payment.getId());
        if(payment1 == null){
            CreditCard card1 = payment.getCreditCardId();
            if(!payment.validCreditCard(card1))
                throw new CreditCardInvalid("Invalid credit card");
            payment.setTotalCost(payment.getItineraryPrice());
            paymentRepository.save(payment);
            sendEmailReceipt(payment);
            return payment;
        }
        else{
            throw new UserAlreadyPaid("You already paid");
        }


    }


    private void sendEmailReceipt(Payment payment) throws Exception{
        Payment payment1 = paymentRepository.findPaymentById(payment.getId());
        String toAddress = payment.getUser().getEmail();
        String fromAddress = "adam.edward.nan@gmail.com";
        String senderName = "National Camping Services";
        String totalPayment = Double.toString(payment.getItineraryPrice());
        String subject = "National Park Tour Receipt";
        String content = "Dear [[name]],<br>"
                + "Thank you for your payment. You paid a total of: [[totalCost]]<br>"
                + "Thank you,<br>"
                + "[[companyName]].";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);
        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);
        content = content.replace("[[name]]", payment.getUser().getName());
        content = content.replace("[[companyName]]", senderName);
        content = content.replace("[[totalPayment]]", totalPayment);
        mimeMessageHelper.setText(content, true);
        mailSender.send(message);
    }




}
