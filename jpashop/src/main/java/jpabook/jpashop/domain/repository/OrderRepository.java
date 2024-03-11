package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderRepository {

    private final EntityManager em;

    //주문 등록
    @Transactional
    public void save(Order order) {
        em.persist(order);
    }

    //주문 단일 조회
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //주문 전체 조회
    public List<Order> findAll() {
        return em.createQuery("select m from Order m", Order.class)
                .getResultList();
    }
}
