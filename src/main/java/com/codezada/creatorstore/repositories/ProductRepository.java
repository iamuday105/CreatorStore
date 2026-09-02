package com.codezada.creatorstore.repositories;

import com.codezada.creatorstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
