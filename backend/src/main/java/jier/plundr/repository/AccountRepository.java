package jier.plundr.repository;

import jier.plundr.model.Account;
import jier.plundr.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByOwningCustomer(Customer owningCustomer);
}
