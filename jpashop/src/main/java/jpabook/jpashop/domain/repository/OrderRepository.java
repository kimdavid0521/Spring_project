package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    //주문 등록
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

    //주문 검색 기능
    //JPQL order을 조회를 하고 order랑 member을 join하는것
//    select o: Order 엔티티를 선택
//    from Order o: Order 엔티티를 가져옴. "o"는 Order 엔티티의 별칭.
//    join o.member m: Order 엔티티의 member 속성과 조인, "m"은 member 엔티티의 별칭
    public List<Order> findSearchByString(OrderSearch orderSearch) {

        String jpql = 'select o from Order o join o.member m';
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql = jpql + " where";
                isFirstCondition = false;
            } else {
                jpql = jpql + " and";
            }
            jpql = jpql + " o.status = :status";
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql = jpql + " where";
                isFirstCondition = false;
            }
            else {
                jpql = jpql + " and";
            }
            jpql = jpql + " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000)
        if(orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    //동적쿼리를 JPA Criteria로 해결하기
    public List<Order> findSearchByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }
    }

    //Querydsl로 처리

//    public List<Order> findSearchBydsl(OrderSearch orderSearch) {
//        JPAQueryFa
//    }

//    where o.status = :status: Order 엔터티의 status 필드가 주어진 상태와 일치하는지 확인합니다. 이때 :status는 JPQL의 매개변수로, 나중에 실제 값으로 바인딩됩니다.
//    and m.name like :name: Member 엔터티의 name 필드가 주어진 이름과 부분적으로 일치하는지 확인합니다. 여기서 like 연산자를 사용하여 부분 일치를 검색합니다. %는 와일드카드로 사용되어 어떠한 문자열이라도 일치할 수 있음을 나타냅니다.
//            setParameter(): JPQL 쿼리에 있는 매개변수(:status와 :name)를 실제 값으로 바인딩합니다. 이를 통해 동적인 쿼리를 생성할 수 있습니다.
//            getResultList(): 쿼리를 실행하여 결과를 가져옵니다. 여기서는 Order 엔터티의 목록을 반환합니다.
//}
