package com.codezada.creatorstore.repositories;

import com.codezada.creatorstore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
