//package jpabook.jpashop;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jpabook.jpashop.domain.Member;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class MemberRepository {
//    //dao와 유사
////    entity매니져를 PersistenceContext
//    @PersistenceContext
//    private EntityManager em;
//    //스프링부트는 모든 것이 스프링 위에서 동작하기 때문에
//    //어노테이션에 있으면 엔티티메니저를 em에 주입해준다.
//
//    public Long save(Member member) {
//        em.persist(member);
//        return member.getId();
//        //member를 반환하는게 아닌 member.getId로 Long으로
//        // 하나만 반환하는 이유는??
//        //커멘드랑 쿼리를 분리해야 된다.
//        //이렇게 저장을 하면 사이드이팩트부터 에러가 발생할 수 있으니
//        //반환한 ID로 나중에 조회를 하기 위해서 ID만 반환한 것
//        //그래서 ID만 조회하도록 설계
//    }
//
//    public Member find(Long id) {
//        return em.find(Member.class, id);
//    }//이렇게 jpa함수를 이용하여 값을 Member형태로 반환
//}
