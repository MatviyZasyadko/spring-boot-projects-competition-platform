
    package com.ukma.competition.platform.payments;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @Configuration
    public class PaymentServiceConfiguration {

        @Bean
        @ConditionalOnProperty(name = "payment.enabled", havingValue = "true", matchIfMissing = true)
        public PaymentService paymentService(PaymentRepository paymentRepository) {
            return new PaymentServiceImpl(paymentRepository);
        }
        }
