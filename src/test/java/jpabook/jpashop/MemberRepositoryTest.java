//package jpabook.jpashop;
//
//import jpabook.jpashop.domain.Member;
//import org.assertj.core.api.Assertions;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.Assert.*;
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MemberRepositoryTest {
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Transactional //스프링껄 쓰는게 좋다.
//    @Rollback(value = false)
//    public void TestMember() throws Exception{
//        //given
//        Member member= new Member();
//        member.setUsername("memberA");
//        //when
//        Long saveId = memberRepository.save(member);
//        Member findMember = memberRepository.find(saveId);
//        //then
//        //검증은 Assertions
//        Assertions
//                .assertThat(findMember
//                        .getId()).isEqualTo(member.getId());
//        Assertions
//                .assertThat(findMember.getUsername())
//                .isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member);
//        //확인을 하기 위해서 출력을 해보면
//        System.out.println("findMember = " + findMember);
//        System.out.println("member = " + member);
//    }
//}