package jier.plundr.repository;

import jier.plundr.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    @Query(value =
            "SELECT * FROM (SELECT contacts.contact_id FROM CONTACTS contacts WHERE contacts.customer_id = ?1) c " +
            "INNER JOIN CUSTOMER customer ON customer.user_id = c.contact_id " +
            "INNER JOIN PLUNDR_USER plundr_user ON plundr_user.user_id = c.contact_id",
            countQuery = "SELECT COUNT(*) FROM CONTACTS c WHERE c.customer_id = ?1",
            nativeQuery = true)
    Page<Customer> findContactsByCustomerId(Long customerId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM CONTACTS contacts WHERE contacts.customer_id = ?1 AND contacts.contact_id = ?2",
            nativeQuery = true)
    void deleteContactByContactId(Long customerId, Long contactId);
}
