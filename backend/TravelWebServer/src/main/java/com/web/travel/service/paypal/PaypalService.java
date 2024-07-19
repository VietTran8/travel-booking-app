package com.web.travel.service.paypal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {
    public static String SUCCESS_URL = "http://localhost:8080/api/payment/success";
    public static String CANCEL_URL = "http://localhost:8080/api/payment/cancel";
    @Value("${travel.app.server.ip}")
    public String ANDROID_SERVER_IP;
    @Autowired
    private APIContext apiContext;
    public Payment createPayment(Double total, Map<String, Long> idParams, String sessionToken, boolean isApp) throws PayPalRESTException{
        Amount amount = new Amount();
        amount.setCurrency("USD");
        total = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription("Pay your tour order");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl((!isApp ? CANCEL_URL : "http://"+ ANDROID_SERVER_IP +":8080/api/payment/cancel") + "/" + idParams.get("orderId") + "/" + sessionToken + "/" + idParams.get("tourId") + "/" + idParams.get("tourDateId") + "/" + isApp);
        redirectUrls.setReturnUrl((!isApp ? SUCCESS_URL : "http://"+ ANDROID_SERVER_IP +":8080/api/payment/success") + "/" + idParams.get("orderId") + "/" + sessionToken + "/" + idParams.get("tourId") + "/" + idParams.get("tourDateId") + "/" + isApp);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}