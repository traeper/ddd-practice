# DDD 프랙티스 #1 자동차 공장
DDD스럽게 코드를 작성하는 방법을 탐구하는 자동차 공장 예제.
JPA를 적극적으로 사용하여 DDD를 구현하는 예제이며 전통적인(?) 개발 보다는 장점이 있음을 확인하였다.
여기서는 언어만 이용하여 순수한 도메인을 추출하는 시도까지는 하지 않는다. 그리고 스프링의 Transactional, Service 정도 까지는 활용한다.

## 자동차 공장 설명
Domain 간 유기적인 관계를 크게 이용하지 않는 [legacy](./src/main/kotlin/com/traeper/car_factory/legacy) 예제와 JPA의 여러 옵션을 활용하여 유기적인 관계 및 도메인에서 핵심이 되는 Aggregate Root등을 파악하기 쉬운 [ddd](./src/main/kotlin/com/traeper/car_factory/ddd) 예제를 작성하였다.

자동차 공장에는 자동차, 바퀴 개념이 존재하는데 자동차 없는 바퀴는 존재하지 않는 것을 전제로 하여 자동차는 Aggregate Root, 바퀴는 자동차에 포함되어 생성 및 삭제되는 종속적인 개념으로 가정한다. 즉 시스템의 핵심을 자동차에 두었을 때 코드를 작성해보자.

(예제 작성 시 바퀴는 자동차의 특성(VO)이 아닌 영속성이 있는 Entity의 지위로 간주하였다.)

### legacy 예제
자동차([LegacyCarEntity](./src/main/kotlin/com/traeper/car_factory/legacy/domain/car/LegacyCarEntity.kt)에 바퀴를 OneToMany로 붙여서 fetch join까지는 활용하도록 했지만 자동차와 바퀴는 엄연히 독립적으로 관리되는 것을 알 수 있다. [테스트코드](./src/test/kotlin/com/traeper/car_factory/legacy/domain/car/LegacyCarFactoryServiceTest.kt)를 보면 바퀴 Repository가 독립적으로 존재하여 관리되므로 바퀴가 자동차에 완전히 종속적인 개념인지 알기 어렵다. 즉 코드를 봤을 때 어떤 개념이 프로젝트의 핵심이 되는지 한 눈에 알기 어렵다.

### ddd 예제
자동차([CarEntity](./src/main/kotlin/com/traeper/car_factory/ddd/domain/car/CarEntity.kt)도 바퀴를 마찬가지로 OneToMany으로 관리하는데 legacy와 다른 점은 Cascade 옵션을 주어 자동차가 생성, 삭제될 때 바퀴도 따라 생성, 삭제되는 것이다. 바퀴 Repository가 존재하지 않아 바퀴를 직접적으로 제어할 수 없는 것을 볼 수 있다. 즉 바퀴는 독립할 수 없는 덜 중요한(?) 개념인 것과 동시에 자동차에 종속된 것을 알 수 있다. 그리고 자동차와 바퀴에 AggregateRoot와 DomainEntity interface를 붙여 둘의 관계를 명확하게 표현하였다. 

[테스트코드](./src/test/kotlin/com/traeper/car_factory/ddd/domain/car/CarFactoryServiceTest.kt)를 보면 바퀴 Repository가 더는 존재하지 않고 자동차에 초점이 맞춰진 것을 볼 수 있다.