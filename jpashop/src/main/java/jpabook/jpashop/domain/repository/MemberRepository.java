package jpabook.jpashop.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    //jpa를 사용할것이기때문에 이렇게 해줘야함(스프링이 엔티티매니저를 만들어서 여기에 주입을 시켜줌)
    //근데 이것도 아까 멤버서비스 클래스에서 했던것처럼 @RequiredArgsConstructor 어노테이션을 사용하여서 생략이 가능함
//    @PersistenceContext
//    private EntityManager em;
    private final EntityManager em;

    //이렇게 해주면 회원을 jpa에 저장하는 로직이 되는것
    public void save(Member member) {
        em.persist(member);
    }

    //이렇게 find를 이용하여서 멤버를 찾는 로직을 만들어줌(멤버를 반환해줌)(이건 단일 조회임)
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    //이건 회원 리스트 조회
    //리스트를 조회할땐 jpql을 작성해야함 (첫번째가 JPQL 두번째가 반환 타입)
    //이렇게하면 리스트로 뽑아줘서 가능
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        return result;
    }
    //만약 특정 이름으로 검색하고싶다하면 이런식으로 이름을 받아서 JPQL을 작성하여 할 수 있음
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
