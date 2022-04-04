package jier.plundr.repository;

import jier.plundr.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    @Query(value = "SELECT * FROM CONTACTS c WHERE c.customer_id = ?1",
            countQuery = "SELECT COUNT(*) FROM CONTACTS c WHERE c.customer_id = ?1",
            nativeQuery = true)
    Page<Customer> findContactsByCustomerId(Long customerId, Pageable pageable);
}
