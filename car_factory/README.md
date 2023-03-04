# DDD 프랙티스 #1 자동차 공장
![](../resources/DDD.png)

DDD스러운 코드를 작성하는 방법을 탐구하는 자동차 공장 예제.
JPA 기반으로 작성한 예제이며 보다 핵심 도메인에 집중할 수 있는 장점이 있음을 확인할 수 있었다.
README.md와 소스코드를 각각 켜놓고 읽는 것을 추천한다. (테스트코드 포함)

## 자동차 공장 설명
Domain 간 유기적인 관계를 크게 이용하지 않는 [legacy](./src/main/kotlin/com/traeper/car_factory/legacy) 예제와 JPA의 여러 옵션을 활용하여 Aggregate 내부의 일관성을 유지하고 도메인에서 핵심이 되는 Aggregate Root등을 파악하기 쉬운 [ddd](./src/main/kotlin/com/traeper/car_factory/ddd) 예제를 작성하였다.

자동차 공장에는 자동차, 바퀴 개념이 존재하는데 자동차 없는 바퀴는 존재하지 않는 것을 전제로 하여 자동차는 Aggregate Root, 바퀴는 자동차에 포함되어 생성 및 삭제되는 종속적인 개념으로 가정한다. 즉 시스템의 핵심을 자동차에 두었을 때 코드를 작성하였다.

### legacy vs ddd 비교 미리보기
![](../resources/car_factory/car_factory-project-tree.png)

legacy가 ddd에 비해 WheelRepository를 하나 더 가지고 있는 것을 볼 수 있다. 단순히 파일 목록만 보면 바퀴의 지위와 쓰임새가 자동차와 동등한 수준일 수도 있다는 생각이 든다. 물론 바퀴와 자동차를 보면 누구나 자동차가 핵심이라고 생각할 것이지만 실제로 도메인이 복잡한 현업에서는 어떤 객체가 더 핵심인지 한 눈에 살펴보기 힘들 수 있을 수 있다.

### legacy 예제
자동차([LegacyCarEntity](./src/main/kotlin/com/traeper/car_factory/legacy/domain/car/LegacyCarEntity.kt))에 바퀴를 OneToMany로 붙여서 fetch join까지는 활용하도록 했지만 자동차와 바퀴는 엄연히 독립적으로 관리되는 것을 알 수 있다. [테스트코드](./src/test/kotlin/com/traeper/car_factory/legacy/domain/car/LegacyCarFactoryServiceTest.kt)를 보면 바퀴 Repository가 독립적으로 존재하여 관리되므로 바퀴가 자동차에 완전히 종속적인 개념인지 알기 어렵다. 즉 코드를 봤을 때 어떤 개념이 프로젝트의 핵심이 되는지 한 눈에 알기 어렵다.

```kotlin
@Entity
class LegacyCarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var carId: Long = 0

    @OneToMany
    @JoinColumn(name = "carId", referencedColumnName = "carId")
    var wheels: MutableList<LegacyWheelEntity> = mutableListOf()

    companion object {
        fun of(): LegacyCarEntity = LegacyCarEntity()
    }
}

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
```

바퀴와 연관 관계는 있지만 바퀴는 독립적으로 조작(CRUD)될 수 있는 가능성이 있다.
이런 코드라면 프로그램 실행 중에 자동차 객체 자신도 모르게 바퀴가 하나 더 달려버리는 문제가 생길 수 있다.

[LegacyCarFactoryService](./src/main/kotlin/com/traeper/car_factory/legacy/domain/car/LegacyCarFactoryService.kt)를 보면 자동차와 바퀴를 따로 생성해야 하는 것을 볼 수 있고 Service의 의존성이 2개인 것을 알 수 있다. 비즈니스가 복잡해지면서 의존성은 훨씬 복잡해질텐데 이대로라면 핵심을 파악하기 어려워지지 않을까? 

```kotlin
@Service
class LegacyCarFactoryService(
    private val carRepository: LegacyCarRepository,
    private val wheelRepository: LegacyWheelRepository,
) {
    @Transactional
    fun createCar() {
        val car = LegacyCarEntity.of()
        carRepository.save(car)

        repeat((1..4).count()) {
            val wheel = LegacyWheelEntity.of(car.carId)
            wheelRepository.save(wheel)
        }
    }
}
```

### ddd 예제
ddd 예제에서도 자동차([CarEntity](./src/main/kotlin/com/traeper/car_factory/ddd/domain/car/CarEntity.kt))는 OneToMany로 바퀴를 관리하는데 달라진 점은 Cascade 옵션을 주어 자동차가 생성, 삭제될 때 바퀴도 따라 생성, 삭제되도록 하여 바퀴의 생애가 자동차에 종속적인 것임을 알 수 있는 것이다. 이제는 바퀴 Repository가 존재하지 않아 바퀴를 독립적으로 제어할 수 없다. 그리고 자동차와 바퀴에 AggregateRoot와 DomainEntity interface를 붙여 둘의 관계를 명확하게 표현하였다. 

```kotlin
@Entity
class CarEntity : AggregateRoot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var carId: Long = 0

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
```
legacy와 다르게 자동차가 바퀴를 직접 관리한다. 즉 자동차와 바퀴의 상태는 Aggregate Root인 자동차를 통해서만 변경할 수 있으며 프로그램 실행 중에 자동차 객체는 자신에게 바퀴가 달리는지, 없어지는지 항상 알 수 있다. 즉 자동차와 바퀴의 집합이 일관성 있게 변경되는 것이다. 


[CarFactoryService](./src/main/kotlin/com/traeper/car_factory/ddd/domain/car/CarFactoryService.kt)를 보면 legacy와 다르게 WheelRepository가 더는 존재하지 않으며 자동차를 통해서만 바퀴 추가가 가능하다. 의존성으로 보나 어떤 객체가 중요한지를 보나 로직에서 핵심이 무엇인지 알기 쉬워졌다.

```kotlin
@DomainService
class CarFactoryService(
    private val carRepository: CarRepository,
) {
    @Transactional
    fun createCar() {
        val car = CarEntity.of()

        repeat((1..4).count()) {
            val newWheel = WheelEntity.of(car)
            car.addWheel(newWheel)
        }

        carRepository.save(car)
    }
}
```

## 질문
* 새로운 요구사항으로 인해 만약 바퀴가 Aggregate Root로 승격하는 경우는 없을까? 이런 경우 자동차와 다른 Aggregate로 개발되어야 되는데 그럴 때는 자동차와 바퀴 관계를 어떻게 개발해야 할까?