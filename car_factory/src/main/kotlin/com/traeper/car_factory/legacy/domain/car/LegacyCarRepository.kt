package com.traeper.car_factory.legacy.domain.car

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface LegacyCarRepository : JpaRepository<LegacyCarEntity, Long> {
    @Query("SELECT c FROM LegacyCarEntity c INNER JOIN FETCH c.wheels " +
        "WHERE c.carId = :carId")
    fun findWithWheelsById(carId: Long): LegacyCarEntity?
}