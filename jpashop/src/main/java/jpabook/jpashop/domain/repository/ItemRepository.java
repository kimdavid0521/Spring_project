package jpabook.jpashop.domain.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) { //기존에 등록되지않은 item이면 id값이 없어서 만약 id값이 없다면 persist로 아이템 신규등록
        if (item.getId() == null) {
            em.persist(item);
        }
        else {
            em.merge(item); //여기서는 이미 id 값이 있는것이므로 업데이트라고 생각하면됨.
        }
    }

    //아이템 단일 조회
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    //아이템 전체 조회(전체조회는 JPQL 작성해줘야함)
    public List<Item> findAll() {
        return em.createQuery("select m from Item m", Item.class)
                .getResultList();
    }

}
