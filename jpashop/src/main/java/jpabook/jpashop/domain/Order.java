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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //java8은 로컬데이트타임을 쓰면 hibernate에서 지원을 해줘서
    //어노테이션 따로 안써도됨
    private LocalDateTime orderData;

    @Enumerated(EnumType.STRING) // 여기서 반드시 어노테이션으로 이넘레이티드 타입은 반드시 string(안그러면 밀려서 장애남)
    private OrderStatus status; //주문 상태 [ORDER, CANCLE]


//    연관관계 메서드
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //--생성 메서드--//
    //이렇게 생성 메서드를 만들어놓으면 생성 지점을 변경해야되면 이것만 바꾸면됨
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        //orderItems들을 반복문으로 넣어줌
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);  //오더의 상태를 처음에 강제로 order로 놓을거다.
        order.setOrderData(LocalDateTime.now()); //지금 시간을 설정해줌
        return order;
    }


    //--비즈니스 로직--//

    //주문 취소 로직
    public void cancle() {
        if(delivery.getStatus() == DeliveryStatus.COMP) {  //취소 로직중 만약 배송이 이미 되어버린 상태라면 취소못함
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가능 합니다");
        }
        this.setStatus(OrderStatus.CANCLE);  //this는 굳이 안써도되는데 그냥 강조해주고싶을때 쓰면됨
        for(OrderItem orderItem : orderItems) { //고객이 상품을 2개 주문하면 orderitem이 2개 생겼기때문에 반복문으로 제거해줘야함
                orderItem.cancle();
        }

    }

    //--조회 로직-//

    //전체 주문 가격 조회 로직
    public int getTotalPrice() {
    //orderitem 클래스 내부에서 주문 수량과 가격이있기때문에 곱해줘서 가져와야함 원래 기존 코드가 아래인데 stream을 써서 코드 축소
    //        for (OrderItem orderItem : orderItems) {
    //            totalPrice = totalPrice + orderItem.getTotalPrice();
    //        }
        //stream을 써서 코드 축소
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }



}
