package com.stripe.stripe.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.stripe.http.PaymentIntentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentService {

    @Value("${stripe.key.secret}")
    private String secretKey;

    public PaymentIntent paymentIntent(PaymentIntentDTO paymentIntentDto) throws StripeException {
        Stripe.apiKey = this.secretKey;
        HashMap<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDto.getAmount());
        params.put("currency", "usd");
        params.put("description", paymentIntentDto.getDescripcion());

        ArrayList paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");

        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }

    public PaymentIntent confirm(String id) throws StripeException {
        Stripe.apiKey = this.secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);

        HashMap<String, Object> params = new HashMap<>();

        params.put("payment_method", "pm_card_visa");

        paymentIntent.confirm(params);

        return paymentIntent;
    }

    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = this.secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();

        return paymentIntent;
    }

}
