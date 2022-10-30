package org.example.controller;


import org.example.entity.Payment;
import org.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/pay")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> add(@RequestBody Payment payment) throws Exception{
        Payment payment1 = paymentService.addPayment(payment);
        return new ResponseEntity<>(payment, HttpStatus.ACCEPTED);
    }
}
