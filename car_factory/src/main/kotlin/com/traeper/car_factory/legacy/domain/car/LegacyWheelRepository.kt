package com.traeper.car_factory.legacy.domain.car

import org.springframework.data.jpa.repository.JpaRepository

interface LegacyWheelRepository : JpaRepository<LegacyWheelEntity, Long>