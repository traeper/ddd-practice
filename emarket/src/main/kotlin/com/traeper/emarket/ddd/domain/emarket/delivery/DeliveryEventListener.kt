package com.traeper.emarket.ddd.domain.emarket.delivery

import com.traeper.emarket.ddd.domain.emarket.order.event.OrderCreated
import io.github.oshai.KLogger
import io.github.oshai.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

private val log: KLogger = KotlinLogging.logger {}

@Component
class DeliveryEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleOrderCreated(event: OrderCreated) {
        log.info { "Order ${event.orderEntity.id} was created. And then new delivery will be created!" }
    }
}