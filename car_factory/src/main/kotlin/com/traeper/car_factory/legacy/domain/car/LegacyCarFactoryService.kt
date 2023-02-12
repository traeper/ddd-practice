package com.traeper.car_factory.legacy.domain.car

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LegacyCarFactoryService(
    private val carRepository: LegacyCarRepository,
    private val wheelRepository: LegacyWheelRepository,
) {
    @Transactional
    fun createCar() {
        val car = LegacyCarEntity.of()
        carRepository.save(car)

        repeat((1..4).count()) {
            val wheel = LegacyWheelEntity.of(car.carId)
            wheelRepository.save(wheel)
        }
    }
}

