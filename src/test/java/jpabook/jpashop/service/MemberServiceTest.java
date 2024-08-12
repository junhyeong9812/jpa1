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
//juit을 실행할 때 스프링과 함께 엮어서 사용할 때는 위 코드를 넣으면 된다.
@SpringBootTest
//스프링 부트를 띄운 상태로 테스트 하려면 위 스프링 부트 테스트가 필요하다. 만약 없다면
//아래 오토와이어드 같은 자동 주입이 실패하고만다.
@Transactional//데이터 변경을 위한 트렌젝셔널이 필요(롤백을 위해)
//트렌젝션을 걸고 테스트를 돌리고 이후에 테스트가 끝나면 전부 롤백을 해준다.
//테스트에서만 롤백을 하는 것(서비스나 리포지토리에서는 롤백하지 않는다.)
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
    //실제로 DB에 들어가는 지 궁금할 때나 보여줄 때는 Rollback어노테이션을 False로 설정

//    was를 띄울 때 메모리 DB를 띄워서 사용하는 게 가장 좋다.

    @Test (expected = IllegalStateException.class)
    public void 중복_회원_가입() throws Exception{
        //given
        Member member1 =new Member();
        member1.setName("Kim");

        Member member2 =new Member();
        member2.setName("Kim");
        //when
        memberService.join(member1);
        memberService.join(member2);
//        try {
//            memberService.join(member2);//이름이 중복으로 들어가기 때문에 예외가 발생해야 한다.
//        }catch (IllegalStateException e){
//            return;
//        }
            //then
        //현재 이 테스트코드는 위에 join코드에서 예외처리로 인해 밖으로 나가 아래로 내려오면
//        안되는데 예외가 나지 않아서 아래까지 도달해버리면서 테스트가 성공하지 않도록
        //그 위치까지 도달하면 fail를 통해 코드가 문제가 있다고 알려주는 역할을 한다.
        fail("예외가 발생한다.");
    }
}
