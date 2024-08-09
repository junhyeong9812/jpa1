package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//리포지토리 어노테이션을 통해 스프링 빈으로 관리
@RequiredArgsConstructor
public class MemberRepository {
//    @Autowired//부트를 사용하면 이렇게 오토와이어드가 가능하다.
    //jpa가 지원을 해주기 때문에
    private final EntityManager em;
//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    //jpa를 사용하기 때문에 제공하는 표준 어노테이션 @PersistenceContext를 사용
//    @PersistenceContext//이 어노테이션으로 엔티티 메니져를 주입받는다.
//    private EntityManager em;
//    //이렇게 주입되면 em에 각종 로직이 들어있다.

    @PersistenceUnit //엔티티메니져를 직접 주입받고 싶으면 위처럼 퍼시스턴스 유닛을 사용
    private EntityManagerFactory emf;

    //사용자 정보를 저장하는 로직
    public void save(Member member) {
        em.persist(member);
    }//이러면 member에 대한 정보를 저장한다.

    //사용자 정보 조회로직
    public Member findOne(Long id) {
        return em.find(Member.class, id);
        //id값을 통해 맴버 정보 단일조회
    }

    //사용자 전체 정보 조회로직
    public List<Member> findAll() {
        //이때는
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
        //이렇게 jpql을 사용하고 뒤에 반환 타입을 넣어서.
        //데이터를 리스트로 반환하여 반환한다.
        //또한 jpql과 sql은 약간 다른 방식으로 작성한다.
        //기능적으로는 거의 동일하지만 sql은 테이블 대상 쿼리지만
        //jpql은 엔티티 객체에 대한 alias를 m으로 할당하고 엔티티 맴버를 조회하도록
        //한다.
    }

    //만약 사용자 이름으로 데이터를 검색해야 될 때는
    public List<Member> findByName(String name){
        //파라미터를 이름으로 넘겨서
        return em.createQuery("select m from Member m where m.name= :name",Member.class)
                .setParameter("name",name).getResultList();
        //이렇게 하면 파라미터가 바인딩 되고 결과값을 리졀트 리스트를 통해 가져온다.
        //
    }


}



