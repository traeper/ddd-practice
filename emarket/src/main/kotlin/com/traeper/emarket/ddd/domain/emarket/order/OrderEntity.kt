package com.traeper.emarket.ddd.domain.emarket.order

import com.traeper.emarket.ddd.domain.emarket.order.event.OrderCreated
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.domain.AbstractAggregateRoot

@Entity
class OrderEntity : AbstractAggregateRoot<OrderEntity>() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L

    companion object {
        fun of(): OrderEntity =
            OrderEntity().apply {
                this.registerEvent(OrderCreated(this))
            }
    }
}