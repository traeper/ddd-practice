package com.traeper.car_factory.legacy.domain.car

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal

@Table(name = "legacy_car")
@Entity
class LegacyCarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var carId: Long = 0

    @Column(nullable = false)
    var fuelAmountPercent: BigDecimal = BigDecimal.ZERO

    @OneToMany
    @JoinColumn(name = "carId", referencedColumnName = "carId")
    var wheels: MutableList<LegacyWheelEntity> = mutableListOf()

    companion object {
        fun of(): LegacyCarEntity = LegacyCarEntity()
    }
}