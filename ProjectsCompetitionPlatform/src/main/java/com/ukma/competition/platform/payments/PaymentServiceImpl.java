package com.ukma.competition.platform.payments;

import com.ukma.competition.platform.shared.GenericServiceImpl;
import org.apache.logging.log4j.*;

public class PaymentServiceImpl extends GenericServiceImpl<PaymentEntity, String, PaymentRepository> implements PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);
    private static final Marker IMPORTANT_MARKER = MarkerManager.getMarker("IMPORTANT");

    public PaymentServiceImpl(PaymentRepository repository) {
        super(repository);
    }

    public PaymentEntity savePayment(PaymentEntity payment) {

        ThreadContext.put("paymentID", payment.getId());
        ThreadContext.put("paymentAmount", String.valueOf(payment.getSum()));

        try {
            logger.info(IMPORTANT_MARKER, "Saving payment with ID: {}", payment.getId());
            return save(payment);
        } finally {
            ThreadContext.clearAll();
        }
    }

}
