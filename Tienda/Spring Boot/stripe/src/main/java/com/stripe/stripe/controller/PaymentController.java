package com.stripe.stripe.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.stripe.http.PaymentIntentDTO;
import com.stripe.stripe.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> payment(@RequestBody PaymentIntentDTO paymentIntentDTO) throws StripeException {
        PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDTO);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        response.put("id", paymentIntent.getId()); // ← Agregar esta línea

        return ResponseEntity.ok(response);
    }


    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirm(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = paymentService.confirm(id);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = paymentService.cancel(id);

        String paymetStr = paymentIntent.toJson();

        return new ResponseEntity<String>(paymetStr, HttpStatus.OK);
    }
}
