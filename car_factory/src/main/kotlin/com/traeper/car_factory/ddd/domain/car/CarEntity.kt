package com.traeper.car_factory.ddd.domain.car

import com.traeper.ddd.core.AggregateRoot
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal

@Table(name = "car")
@Entity
class CarEntity : AggregateRoot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var carId: Long = 0

    @Column(nullable = false)
    var fuelAmountPercent: BigDecimal = BigDecimal.ZERO

    // 양방향 매핑, car 생성/삭제할 때 wheel도 함께 생성, 삭제
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE], mappedBy = "car")
    var wheels: MutableList<WheelEntity> = mutableListOf()

    // 바퀴의 추가를 자동차가 관리한다.
    fun addWheel(wheel: WheelEntity) {
        wheels.add(wheel)
    }

    // 바퀴의 제거를 자동차가 관리한다.
    fun removeWheel(wheel: WheelEntity) {
        wheels.remove(wheel)
    }

    companion object {
        fun of(): CarEntity =
            CarEntity()
    }
}