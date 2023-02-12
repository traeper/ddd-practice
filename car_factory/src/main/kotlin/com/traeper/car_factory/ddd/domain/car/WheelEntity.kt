package com.traeper.car_factory.ddd.domain.car

import com.traeper.ddd.core.DomainEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "wheel")
@Entity
class WheelEntity : DomainEntity<CarEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var wheelId: Long = 0

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carId", nullable = false)
    lateinit var car: CarEntity
        protected set

    companion object {
        fun of(car: CarEntity): WheelEntity =
            WheelEntity().apply {
                this.car = car
            }
    }
}
