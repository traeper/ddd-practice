package com.traeper.emarket.ddd.domain.emarket.order

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OrderTest {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    fun domainEvents() {
        // given
        val order = OrderEntity.of()

        // when
        orderRepository.save(order)
    }
}