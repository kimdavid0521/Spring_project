package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional //스프링 트렌젝션 어노테이션 쓰는걸 권장
public class MemberService {

    //memberRepository를 사용할것이기 때문에 선언해줌
    // @Autowired
    private final MemberRepository memberRepository; //여기서 이렇게 메서드를 사용하지않고 private으로 선언하게되면 못바꾼다는 단점이있음

    //그래서 이렇게 메서드에 넣어서 주입하는 형식으로하면 테스트시에 목데이터를 넣기 편함 하지만 궁극적으로는 생성자 인젝션을 많이씀
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //보통 이렇게 생성자 인젝션이 하나만 있는경우 오토와이얼드 어노테이션을 사용하지않아서 인젝션을 해줌 근데 이것도 생략하수있는 방법이있음
    //@RequiredArgsConstructor 인데 이 어노테이션은 final이있는걸로 생성자 인젝션을 만들어줌 즉 아래 코드도 생략 가능 (lombok 기능)
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository();
//    }

    //회원가입 기능
    @Transactional // 가입시엔 readOnly true값을 주면 안됨 그러면 데이터 변경이 안됨
    public Long join(Member member) {

        validateDuplicateMember(member); //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    //중복 회원 검증 기능
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); //멤버 리포지토리에 있는 이름 조회기능으로 멤버의 이름을 조회
        //조회된 값이 findMembers에 들어가있다면 에러터지게
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체조회 기능
    @Transactional(readOnly = true) // 조회하는 부분에서 트렌젝션에 readOnly를 true로 주게되면 jpa가 조회시에 성능을 더 최적화함
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 id로 조회(단일 조회)
    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
