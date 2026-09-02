package com.codezada.creatorstore.services;

import com.codezada.creatorstore.dto.OrderItemRequest;
import com.codezada.creatorstore.dto.OrderRequest;
import com.codezada.creatorstore.entities.Order;
import com.codezada.creatorstore.entities.OrderItem;
import com.codezada.creatorstore.entities.Product;
import com.codezada.creatorstore.repositories.OrderRepository;
import com.codezada.creatorstore.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {



    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest){
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setStatus("CONFIRMED");

        for(OrderItemRequest itemRequest :orderRequest.getItems()){
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found with id" + itemRequest.getProductId()
                    ));

//            check the product stock
            if(product.getStockQuantity() < itemRequest.getQuantity()){
                throw new RuntimeException("Not enough stock for"+ itemRequest.getProductId()) ;
            }

//        Calculate total price
            BigDecimal priceOfItem = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
           totalPrice = totalPrice.add(priceOfItem);

//      update the product table with latest stock quantity
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity()
            );

            productRepository.save(product);

//            Builder pattern to make obj
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItems.add(orderItem);

        }
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);
      return orderRepository.save(order);
    }


    public List<Order> getAllOrders(){

        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Order not found with id"+ id));
    }
}
