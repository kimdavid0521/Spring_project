package jpabook.jpashop.service;

import jakarta.persistence.Entity;
import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.ItemRepository;
import jpabook.jpashop.domain.repository.MemberRepository;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.domain.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository; //order 메서드에서 멤버id를 파라미터로 받았기 때문에 멤버리포지토리가 필요함
    private final ItemRepository itemRepository; //order 메서드에서 아이템id를 파라미터로 받았기 때문에 멤버리포지토리가 필요함

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //멤버와 아이템의 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성 (멤버에서 getAddress로 주소 뽑아와서 delivery에 넣기) but 실제론 배송지 입력 정보 넣어야됨
        //static 생성 메서드를 통해 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    //주문 취소
    @Transactional
    public void cancleOrder(Long orderId) {
        //우선 id로 order리포지토리에서 찾음
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancle();
    }

    //검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findSearchByCriteria(orderSearch);

    }


}
