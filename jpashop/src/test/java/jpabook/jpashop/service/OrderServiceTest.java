package jpabook.jpashop.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.repository.OrderRepository;
import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;



    //상품 주문 테스트
    @Test
    public void makeOrder() throws Exception {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("서울", "강가", "12-213"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(1000);
        book.setStockQuantity(10); //재고 설정
        em.persist(book);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);


        //상품 주문시 상태 테스트
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태");

        //상품 주문시 종류 수 테스트
        assertEquals(1, getOrder.getOrderItems().size(),"주문한 상품 종류 수");

        //주문한 상품의 총 가격 테스트
        assertEquals(1000* orderCount, getOrder.getTotalPrice(),"주문한 상품의 총 가격 테스트");

        //주문 수량 만큼 제고 주는지 테스트(재고를 10개로 설정해놓고 2개뺌)
        assertEquals(orderCount, 8, book.getStockQuantity(), "주문 갯수만큼 재고 수량 줄어야한다.");
        //assertEquals("상품 주문시 주문 id 값",orderId, getOrder.getId());


        //예외 테스트 (상품 주문 재고 초과)

    }

    //예외 테스트 (상품 주문 재고 초과)
    @Test
    public void overCount() throws Exception {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("서울", "강가", "12-213"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(1000);
        book.setStockQuantity(10); //재고 설정
        em.persist(book);

        int orderCount = 11;  //주문 수량을 재고보다 높게 설정

        assertThrows(NotEnoughStockException.class, ()
                -> orderService.order(member.getId(), book.getId(), orderCount));
    }

    //주문 취소 테스트케이스
    @Test
    public void cancleOrder() throws Exception{
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("서울", "강가", "12-213"));
        em.persist(member);

        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(1000);
        book.setStockQuantity(10); //재고 설정
        em.persist(book);

        int orderCount = 3;

        //주문 후 주문 취소 메서드 사용하여 검증하기
        Order getOrder = new Order();
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancleOrder(orderId);

        //주문 취소 후 재고가 잘 복원이 됐는지 검증하기
        Order getorder = orderRepository.findOne(orderId);

        //주문 취소시 상태는 캔슬이 되어야함
        assertEquals(OrderStatus.CANCLE, getorder.getStatus(),"주문시 상태는 취소가 되는걸 검증");

        //주문 취소시 재고 원복 검증하기(기존 10개가 다 돌아왔는지)
        assertEquals(10, book.getStockQuantity());

    }
}