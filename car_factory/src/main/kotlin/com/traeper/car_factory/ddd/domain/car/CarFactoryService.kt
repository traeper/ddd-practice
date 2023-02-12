package com.traeper.car_factory.ddd.domain.car

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
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

