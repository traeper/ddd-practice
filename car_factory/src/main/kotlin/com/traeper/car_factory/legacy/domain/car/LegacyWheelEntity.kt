package com.traeper.car_factory.legacy.domain.car

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "legacy_wheel")
@Entity
class LegacyWheelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var wheelId: Long = 0

    @Column(nullable = false)
    var carId: Long = 0

    companion object {
        fun of(carId: Long): LegacyWheelEntity =
            LegacyWheelEntity().apply {
                this.carId = carId
            }
    }
}
