package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //service에도 컴포넌트가 존재하여 자동으로
//스캔 대상이 된다. jpa의 데이터 변경이나 어떤 로직들은 가급적이면 트랜잭션 안에서 다
//등록이 트랜젝션 안에서 다 실행이 되어야 한다.
//@Transactional//그렇기 때문에 이와 같이 Transactional이 필요하다.
//이렇게 하나의 트랜젝션에서 동작해야 레이지로딩 등 기능을 사용할 수 있다.
//기본적으로 트랜젝션 안에서 데이터 변경앟는 것은 트랜젝션이 꼭 존재해야 된다.
//Transactional은 javax도 있지만 기능이 spring이 더 많이 가지고 있기 때문에
//스프링 프레임 워크의 트랜젝션을 사용하는게 더 좋다.
@Transactional(readOnly = true)
//@AllArgsConstructor
@RequiredArgsConstructor
public class MemberService {



//    @Autowired //필드 인젝션
    private final MemberRepository memberRepository;
//이 리포지토리는 변경될 일이 없기 때문에 final로 설정하는 게 좋다.
    //파이널로 안하면 에러가 잘 나오지 않는다.
    //컴파일 시점에 체크가 가능하기 떄문에 파이널을 설정하는 게 좋다.
    //여기에 롬복을 적용을 하면 AllArgsConstructor를 사용하여 생성자를
    //만들어주는 방식도 있다.
    //이것보다 좋은건 @RequiredArgsConstructor를 통해 final을 가지고 있는
    //값에 대해서만 생성자를 만들어준다.


    //이코드의 단점은 테스트를 하거나 잠시 변경을 해야될 때 직접 엑세스할 수 있는
    //방법이 없다.
    //그래서 이럴 때는 세터를 사용하게 되는데
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    //이처럼 만들면 테스트 코드를 할때 필요한 리포지토리를 주입할 수 있어서 편리하다.
    //이 방식의 단점으로는 한번 런타임에 뭔가 실제 시점에 누가 이걸 바꿀 수 있다.
    //하지만 서버가 빌드가 완료된 이후에 이걸 바꿀 이유가 없기때문에 현재는 잘 쓰이지 않고
    //다른 방법을 사용하는데 생성자를 사용하여 초기화 해준다.
//    @Autowired //오토와이어드 생략 가능
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }//이렇게 하면 생성자에서 인젝션을 해준다. 그런데 생성자 인젝션을 쓰면
    //이 작업이 끝나서 중간에 바꿀 수 없다. 그래서 생성된 리액션이 좋고
    //테스트 케이스 작성할때 맴버 서비스를 작성할 때new 연산자에서 직접
    //리포지토리를 주입해야된다는 것을 놓지지 않고 작성할 수 있다.
    //이러한 장점이 있는데 요즘에는 최신버전에서는 생성자가 하나만 있는 경우에는
    //자동으로 인젝션을 해준다.

    //회원 가입
    //하지만 이러한 쓰기 동작에서는 read-only기능을 넣으면 안된다.
    @Transactional//이렇게 따로 설정하면 우선권을 가져서 디폴트값 펄스가 존재
    public Long join(Member member){
        //같은 이름의 중복 회원 확인
        validateDuplicateMember(member);
        //회원 정보 저장
        memberRepository.save(member);
        //아이디를 반환해준다.
        //이때 리포지토리에서 em.persist를 이용하여 맴버 정보를 저장하게 되면
        //영속성 객체에 member객체를 올리게 된다.
        //이때 영속성 컨텍스트는 pk가 값이 키가 되고(@Id값)
        //GeneratedValue를 이용하면 id값에 보장이 된다.
        //db에 들어가지 않아도 이렇게 해준다.
        return member.getId();
    }
    //같은 이름의 중복 회원 확인 절차 확인
    private void validateDuplicateMember(Member member) {
        //중복 시 EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        //동일한 이름을 가진 맴버 목록을가져온다.
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }//동일한 이름을 가진 맴버가 존재할 경우 throw를 통해 익셉션을 생성하고
        //새로운 익셉션을 생성한다.

    }//현재 위 코드 구조에서는 WAS가 여러개가 발생할 수 있기 때문에 문제가 된다.
    //서로 다른 두명의 A라는 아이디를 가진 유저가 듀플리케이트 맴버를 통과하게 되면
    //로직을 둘 다 동시에 호출 할 수 있다.
    //회원 A라는 이름의 회원 2명 가입이 동시에 이뤄지는 것이다.
    //그래서 이에 대해 멀티스레드나 데이터베이스의 맴버의 네임을 유니크 제약을 넣어서
    //하는 것을 추천한다.




    //회원 전체 조회
//    @Transactional(readOnly = true)//jpa가 조회하는 곳에서 성능 최적화를한다.
    //영속성 컨텍스트를 플러시 안하고 더티 체킹을 안하고 DB에 따라서 읽기 전용
    //트랜잭션으로 부하를 최소화하고 읽기용 모드로 단순화해줄 수 있다.
    //그래서 읽기에는 읽기 전용으로 설정하는 게 좋다.
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    //회원 단일 조회
//    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
