package com.ukma.competition.platform.payments;


import com.ukma.competition.platform.votes.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String>, JpaSpecificationExecutor<PaymentEntity> {
}
