package ru.bsuedu.cad.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bsuedu.cad.lab.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.customer")
    List<Order> findAllWithCustomer();

    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.orderId = :id")
    Optional<Order> findByIdWithCustomer(Long id);
}
