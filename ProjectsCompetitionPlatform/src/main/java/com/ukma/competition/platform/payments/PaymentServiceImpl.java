package com.ukma.competition.platform.payments;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment, String, PaymentRepository> implements PaymentService {

    @Autowired
    public PaymentServiceImpl(PaymentRepository repository) {
        super(repository);
    }
}
