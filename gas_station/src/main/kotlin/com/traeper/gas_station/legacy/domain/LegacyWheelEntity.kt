package com.traeper.gas_station.legacy.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class LegacyWheelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	var wheelId: Long = 0

	@Column(nullable = false)
	var carId: Long = 0
}
