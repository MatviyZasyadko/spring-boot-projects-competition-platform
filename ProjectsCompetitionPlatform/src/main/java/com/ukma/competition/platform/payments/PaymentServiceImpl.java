package com.ukma.competition.platform.payments;

import com.ukma.competition.platform.shared.GenericServiceImpl;

public class PaymentServiceImpl extends GenericServiceImpl<PaymentEntity, String, PaymentRepository> implements PaymentService {

    public PaymentServiceImpl(PaymentRepository repository) {
        super(repository);
    }
}
