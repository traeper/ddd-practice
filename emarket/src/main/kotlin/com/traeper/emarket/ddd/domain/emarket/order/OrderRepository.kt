package com.traeper.emarket.ddd.domain.emarket.order

import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderEntity, Long>