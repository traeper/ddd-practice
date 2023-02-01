package com.traeper.gas_station.legacy.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.math.BigDecimal

@Entity
class LegacyCarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long = 0

    @Column(nullable = false)
    var fuelAmountPercent: BigDecimal = BigDecimal.ZERO

    @OneToMany(fetch = FetchType.LAZY)
    var wheels: MutableList<LegacyWheelEntity> = mutableListOf()

    companion object {
        fun of(): LegacyCarEntity =
            LegacyCarEntity()
    }
}