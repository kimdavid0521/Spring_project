package jpabook.jpashop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    //이 생성자를 막아주는 코드를 위에 @NoArgsConstructor(access = AccessLevel.PROTECTED) 어노테이션으로 줄일수있음
//    protected OrderItem() {
//    }

    //--생성 메서드 --//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //아이템을 생성시에는 재고에서 까줘야됨
        item.removeStock(count);

        return orderItem;
    }


    //--비즈니스 로직 --//
    public void cancle() {  //orderitem의 cancle의미는 재고수량을 원복해준다는 의미임.
        this.getItem().addStock(count);
    }

    //Order 클래스에서 전체 금액 조회에 필요한 메서드
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
