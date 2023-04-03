package com.traeper.emarket.ddd.domain.emarket.product

import com.traeper.emarket.ddd.domain.emarket.order.event.OrderCreated
import io.github.oshai.KLogger
import io.github.oshai.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

private val log: KLogger = KotlinLogging.logger {}

@Component
class ProductStockEventListener {
    @Transactional(propagation = Propagation.REQUIRED)
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handleOrderCreated(event: OrderCreated) {
        log.info { "Order ${event.orderEntity.id} is created. The product stock decrement by one." }
    }
}