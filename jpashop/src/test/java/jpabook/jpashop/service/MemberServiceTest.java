package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

//    @Autowired
//    EntityManager em;

    //회원가입 테스트 케이스 작성
    @Test
    //@Rollback(false) //정 눈으로 데이터베이스에 정보가 들어가는지 보고싶으면 롤백에 false값 넣어주고 데이터베이스에서 직접 관찰
    public void joinMembership() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

        //em.flush(); //호출해주면 쿼리가 데이터 베이스에 적용됌
        assertEquals(member, memberRepository.findOne(saveId)); //가입된 멤버랑 findOne메서드에 id를 넣어서 도출된 멤버가 똑같은지 검증

    }



    //중복 회원 예외 테스트 케이스 작성
    @Test
    public void validMember() throws Exception {
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");

        memberService.join(member1);

        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
//        try {
//            memberService.join(member2);  //같은 이름으로 두번 가입했기에 이부분에서 예외에 터져야함
//        } catch (IllegalStateException e) { //근데 이런식으로 테스트 코드를 작성하면 너무 더러우니까 expected = IllegalStateException.class라는 조건이있음
//            return;
//        }

    }
}