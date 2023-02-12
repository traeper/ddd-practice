package com.traeper.car_factory.ddd.domain.car

import com.traeper.ddd.core.DomainService
import org.springframework.transaction.annotation.Transactional

@DomainService
class CarFactoryService(
    private val carRepository: CarRepository,
) {
    @Transactional
    fun createCar() {
        val car = CarEntity.of()

        repeat((1..4).count()) {
            val newWheel = WheelEntity.of(car)
            car.addWheel(newWheel)
        }

        carRepository.save(car)
    }
}

