package com.traeper.car_factory.ddd.domain.car

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CarFactoryServiceTest {

    private lateinit var carFactoryService: CarFactoryService

    @Autowired
    private lateinit var carRepository: CarRepository

    @BeforeEach
    fun setUp() {
        carFactoryService = CarFactoryService(carRepository)
    }

    @Test
    fun `create & delete car - test`() {
        carFactoryService.createCar()

        val savedCar = carRepository.findWithWheelsById(1)!!
        assertThat(savedCar).isNotNull
        assertThat(savedCar.wheels).hasSize(4)

        // CascadeType.REMOVE를 쓰지 않으면 h2의 기본 FK reference에 걸려서 Spring DataIntegrity 예외가 발생한다.
        carRepository.delete(savedCar)

        // Transactional이 걸려있는 테스트에서 delete query를 실행시키기 위함
        carRepository.findAll()
    }
}