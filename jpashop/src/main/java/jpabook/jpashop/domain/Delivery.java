package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded //내장타입이라 임베디드 써줘야함
    private Address address;

    @Enumerated(EnumType.STRING) // enum을 쓸때는 이렇게 어노테이션으로 이넘레이티드를 사용해야하고
    // enumtype은 기본이 ordinary인데 이건 중간에 상태가 하나 추가되면 값이 밀려서 반드시 string으로 해줘야함(안그러면 장애남)
    private DeliveryStatus status; //READY, COMP
}
