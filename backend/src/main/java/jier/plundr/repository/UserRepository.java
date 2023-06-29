package jier.plundr.repository;

import jier.plundr.model.PlundrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PlundrUser, Long> {

    PlundrUser findByUsername(String username);
}
