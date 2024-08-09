package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;

import static org.junit.Assert.*;

//테스트 실행을 위해서는 jpa가 DB까지 도는 것이 필요해서
//메모리 모드로 테스트 하는게 중요하다.
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional//데이터 변경을 위한 트렌젝셔널이 필요(롤백을 위해)
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    //쿼리를 확인하고 싶다면
    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception{
        //given
        Member member=new Member();
        member.setName("Kim");
        //when
        Long saveId= memberService.join(member);
        //join을 통해 DB테이블의 값을 입력한다.
        //then
        em.flush();
        assertEquals(member,memberRepository.findOne(saveId));
        //Assert를 통해 입력된 정보가 정상적으로 호출되는 지 확인한다.
    }
    //이게 가능한 이유는 같은 트랜젝션 안에서 동작할 떄는 같은 영속성
    //컨텍스트가 관리한다. 단일로 관리하고 있기 떄문에 test는 True
    @Test
    public void 중복_회원_가입() throws Exception{
        //given
        
        //when
        
        //then
    }
}