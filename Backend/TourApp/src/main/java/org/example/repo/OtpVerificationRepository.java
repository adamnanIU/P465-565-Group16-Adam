package org.example.repo;

import org.example.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Integer> {

    @Query("select o from OtpVerification o where o.token = :token")
    OtpVerification findByToken(Integer token);
}
