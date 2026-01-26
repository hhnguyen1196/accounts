package training.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import training.account.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
