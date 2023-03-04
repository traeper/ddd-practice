# DDD Core

![](../resources/DDD.png)

DDD에서 언급되는 Aggregate Root, Domain Service, Domain Event 등을 코드로 녹여냈다.
예전에 함께 일했던 동료가 작성한 것을 보고 감명 깊었던 적이 있어서 나도 이렇게 코드를 작성해본다.

## Aggregate Root
특정 Aggregate(애그리거트)에 포함된 Entity끼리는 변경에 대해 일관성(같은 Root를 통해 함께 변경됨)을 가져야 한다. Aggregate의 Root를 담당하는 Entity만 Domain에서 Repository를 유일하게 소유할 수 있어 제어의 창구가 되어 Aggregate 내의 다른 Entity들을 관리한다. [AggregateRoot interface](./src/main/kotlin/com/traeper/ddd/core/AggregateRoot.kt)를 정의하였는데 이것을 Aggregate Root가 되는 Entity에 붙여서 Repository를 유일하게 가질 수 있음을 명시적으로 알려주도록 하였다. 
* 사용 예시 : [CarEntity](../car_factory/src/main/kotlin/com/traeper/car_factory/ddd/domain/car/CarEntity.kt) 

## Domain Entity
Aggregate Root가 아닌 Domain 객체는 Repository를 가질 수 없어야 하며 연관된 AggregateRoot가 존재한다. 이를 코드로 구현하기 위해 [DomainEntity interface](./src/main/kotlin/com/traeper/ddd/core/DomainEntity.kt)를 정의하였고 연관된 AggregateRoot를 명시하도록 하였다.   
* 사용 예시 : [CarEntity](../car_factory/src/main/kotlin/com/traeper/car_factory/ddd/domain/car/CarEntity.kt)

## DomainService
[DomainService](./src/main/kotlin/com/traeper/ddd/core/DomainService.kt)를 추가하여 Domain Layer의 DomainService임을 명시해준다. 이 개념을 추가한 이유는 Layered Architecture 기반에서 프로그래밍하는 경우 상위 레이어인 ApplicationService와 구분하기 위함이다. DomainService는 단일 Repository만으로 해결할 수 없거나 인접한 Domain 간 복잡도가 높은 비즈니스를 수행하기 위해 이용하는 용도이다. 

인접한 Domain을 DomainService로 묶어 처리하여 응집도가 증가하며 가독성이 증가하게 된다. 추후 복잡도가 올라가는 예제를 작성하면서 활용할 예정이다.
* 사용 예시 : [CarFactoryService](../car_factory/src/main/kotlin/com/traeper/car_factory/ddd/domain/car/CarFactoryService.kt)

## DomainEvent
Aggregate 간 결합도를 낮추기 위해 활용하는 개념이다. A Aggregate가 비즈니스를 수행한 뒤 B Aggregate의 동작이 수행되어야 할 때 A Aggregate는 DomainEvent를 발행하며 B는 그 DomainEvent를 구독하여 동기 혹은 비동기로 자신의 비즈니스를 수행할 수 있다. Aggregate 간 결합도는 낮추면서 비즈니스를 연결하는 방법이다. 이것 역시 추후 프랙티스를 작성할 예정이다.