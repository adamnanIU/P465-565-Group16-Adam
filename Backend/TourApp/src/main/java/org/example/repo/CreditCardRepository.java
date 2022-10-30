package org.example.repo;

import org.example.entity.CreditCard;
import org.example.entity.State;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {


}
