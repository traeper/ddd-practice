package com.traeper.car_factory.ddd.domain.car

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CarRepository : JpaRepository<CarEntity, Long> {
    @Query("SELECT c FROM CarEntity c INNER JOIN FETCH c.wheels " +
        "WHERE c.carId = :carId")
    fun findWithWheelsById(carId: Long): CarEntity?
}