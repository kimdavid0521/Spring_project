package jpabook.jpashop;

import jakarta.annotation.security.RunAs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    //멤버 레포지토리 테스트 코드
    @Autowired MemberRepository memberRepository;

    @Test
    //트렌젝션 어노테이션이 테스트케이스에 있으면 테스트 끝나고 바로 롤백을 해버려서 데이터 없어짐
    @Transactional
    //이렇게 롤백 어노테이션을 false로 해주면 되긴하는데 테스트케이스에서는 바로바로되게끔 롤백을 안해주는게 정석
    @Rollback(false)
    public void testMember() throws Exception {

        Member member = new Member();
        member.setUsername("kimtaeyoung1");

        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        Assertions.assertEquals(member.getId(), findMember.getId());
        Assertions.assertEquals(member.getUsername(), findMember.getUsername());
    }
}