package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //java8은 로컬데이트타임을 쓰면 hibernate에서 지원을 해줘서
    //어노테이션 따로 안써도됨
    private LocalDateTime orderData;

    @Enumerated(EnumType.STRING) // 여기서 반드시 어노테이션으로 이넘레이티드 타입은 반드시 string(안그러면 밀려서 장애남)
    private OrderStatus status; //주문 상태 [ORDER, CANCLE]


}
