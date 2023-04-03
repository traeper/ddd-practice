package com.traeper.emarket.ddd.domain.emarket.order.event

import com.traeper.ddd.core.DomainEvent
import com.traeper.emarket.ddd.domain.emarket.order.OrderEntity

class OrderCreated(
    val orderEntity: OrderEntity,
) : DomainEvent