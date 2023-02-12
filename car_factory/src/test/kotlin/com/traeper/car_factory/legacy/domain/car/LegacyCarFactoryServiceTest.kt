package com.traeper.car_factory.legacy.domain.car

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class LegacyCarFactoryServiceTest {

    private lateinit var carFactoryService: LegacyCarFactoryService

    @Autowired
    private lateinit var carRepository: LegacyCarRepository

    @Autowired
    private lateinit var wheelRepository: LegacyWheelRepository

    @BeforeEach
    fun setUp() {
        carFactoryService = LegacyCarFactoryService(carRepository, wheelRepository)
    }

    @Test
    fun `legacy - create & delete car - test`() {
        carFactoryService.createCar()

        val wheels = wheelRepository.findAll()
        assertThat(wheels).hasSize(4)

        val car = carRepository.findWithWheelsById(1)!!

        // car 삭제하기 전 wheel을 삭제해줘야 FK constraints 오류에 걸리지 않음.
        wheelRepository.deleteAll()

        carRepository.delete(car)

        // Transactional이 걸려있는 테스트에서 delete query를 실행시키기 위함
        carRepository.findAll()
    }
}