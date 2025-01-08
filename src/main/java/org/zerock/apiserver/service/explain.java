package org.zerock.apiserver.service;

public class explain {
}

//서비스 인터페이스 (ProductService)
//서비스 계층의 **기본적인 계약(Contract)**를 정의합니다.
//어떤 메서드들이 제공되는지를 명확히 선언하며, 구체적인 구현은 담지 않습니다.
//예: getList(PageRequestDTO pageRequestDTO) 메서드는 선언만 되어 있고, 실제 구현은 ProductServiceImpl에 있습니다.
//구현 클래스 (ProductServiceImpl)
//인터페이스에 정의된 메서드를 구체적으로 구현합니다.
//실제 비즈니스 로직, 데이터 조회/가공 작업은 구현 클래스에 작성됩니다.
//
//이 설계의 장점
//        (1) 유지보수성
//서비스 인터페이스와 구현을 분리하면 코드 변경이 특정 클래스에만 영향을 미치도록 제어할 수 있습니다.
//예를 들어:
//새로운 구현 클래스가 필요할 때 기존 인터페이스를 변경하지 않고도 추가 가능합니다.
//구현 클래스의 로직을 수정하더라도, 인터페이스를 사용하는 다른 클래스에는 영향을 주지 않습니다.
//        (2) 확장성
//인터페이스를 통해 다양한 구현을 제공할 수 있습니다.
//        예를 들어, ProductService 인터페이스를 구현하는 또 다른 클래스를 만들어 다른 데이터 소스나 비즈니스 로직을 처리할 수 있습니다:
//        (3) 테스트 용이성
//인터페이스를 활용하면 가짜(Mock) 구현을 만들어 테스트할 수 있습니다.
//예를 들어:
//데이터베이스 없이 ProductService를 테스트하고 싶다면, ProductService 인터페이스를 구현한 Mock 클래스를 작성하여 테스트에 사용할 수 있습니다.
//        (4) 의존성 주입과 유연성
//스프링 프레임워크의 **의존성 주입(DI)**을 통해 구현체를 유연하게 변경할 수 있습니다.
//@Service 어노테이션이 붙은 ProductServiceImpl이 기본적으로 주입되지만, 다른 구현체로 교체가 필요하면 스프링 설정에서 이를 쉽게 변경할 수 있습니다.
//        (5) 계약 기반 개발
//인터페이스를 통해 **계약 기반 개발(Contract-Driven Development)**이 가능합니다.
//서비스의 입력과 출력이 명확히 정의되므로, 개발자가 이를 기반으로 의존성을 작성하거나 상호 작용을 예측할 수 있습니다.
//
//서비스 계층에서의 @Transactional
//인터페이스 수준에 @Transactional을 설정하고, 구현체에서 이를 자동으로 상속받는 방식은 코드 중복을 줄이고 선언적인 트랜잭션 관리를 가능하게 합니다.
//트랜잭션 관리의 이유:
//서비스 계층은 데이터베이스와의 상호 작용을 포함한 비즈니스 로직을 처리합니다.
//@Transactional을 통해 다음을 보장합니다:
//트랜잭션 롤백: 예외가 발생하면 데이터베이스 상태를 이전 상태로 복구.
//일관성 유지: 여러 개의 데이터 변경 작업이 하나의 트랜잭션으로 묶여 원자성을 유지.
//왜 인터페이스에 선언하는가?
//인터페이스에 선언하면 모든 구현체에 자동으로 적용됩니다.
//특정 구현체만 트랜잭션 처리가 필요하다면, 구현 클래스에 별도로 선언할 수도 있습니다.
//
//구체적인 설계 이유
//        (1) 인터페이스 중심 설계
//스프링에서는 인터페이스 중심 설계가 권장됩니다.
//비즈니스 로직은 서비스 계층에서 처리하고, 이를 위한 계약을 인터페이스로 정의합니다.
//        (2) SRP(단일 책임 원칙) 준수
//서비스 계층은 비즈니스 로직을 처리하는 책임만 가집니다.
//데이터베이스 접근이나 DTO 변환은 서비스 계층이 담당하며, 이를 인터페이스와 구현 클래스로 분리함으로써 각 클래스가 명확한 책임을 갖게 됩니다.
//        (3) DI(의존성 주입)와 IoC(제어 역전)의 활용
//스프링 프레임워크는 의존성 주입(DI)과 제어 역전(IoC)을 기반으로 동작합니다.
//서비스 인터페이스와 구현을 분리하면, 스프링이 구현체를 쉽게 교체하거나 주입할 수 있습니다.
//
//왜 구현 클래스에 @Service를 붙이는가?
//@Service는 스프링이 해당 클래스를 서비스 계층의 빈(Bean)으로 등록하도록 지시합니다.
//        인터페이스에는 @Transactional만 선언하고, 구체적인 구현 클래스에 @Service를 붙이는 이유는 다음과 같습니다:
//인터페이스는 비즈니스 계약만 선언하고, 구현체는 실제 로직을 처리.
//스프링은 @Service를 보고 구현체를 빈으로 등록하여 의존성 주입 가능.