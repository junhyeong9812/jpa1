package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;

import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;
    //주문 생성
    public void save(Order order){
        em.persist(order);
    }
    //단일 조회
    public Order findOne(Long id){
        return em.find(Order.class,id);
    }
    //검색기능 개발
    public List<Order> findAll(OrderSearch orderSearch){
        //하지만 아래 쿼리는 오더 서치에 모든 값이 존재할 경우만 되야 한다.
        //그래서 동적 쿼리로 이 문제를 해결해야 된다.
//        return em.createQuery("select o from Order o join o.member m" +
//                        "where o.status= :status" +
//                        "and m.name like :name",
//                Order.class)//파라미터 바인딩
//                .setParameter("status",orderSearch.getOrderStatus())
//                .setParameter("name",orderSearch.getMemberName())
//                .setMaxResults(1000)//1000개의 결과까지만 제한
//                .getResultList();
                //테이블을 객체로 표현하기 때문에 위처럼 표현
        //이때 결과를 페이징 처리하고 싶을 경우 setFirstResult를 사용해서 스타트
        //포지션을 지정
        //--동적 쿼리 해결방안 1안--//
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition =true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null){
            if (isFirstCondition) {
                jpql += " where "; // 조건문이 처음이면 where 추가
                isFirstCondition = false; // 이후부터는 false로 설정
            } else {
                jpql += " and "; // 두 번째 조건부터는 and 추가
            }//처음 조건이라면 where을 사용할 수 있도록
            jpql += "o.status =:status";
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            if (isFirstCondition) {
                jpql += " where "; // 조건문이 처음이면 where 추가
                isFirstCondition = false; // 이후부터는 false로 설정
            } else {
                jpql += " and "; // 두 번째 조건부터는 and 추가
            }
            jpql +="m.name like :name";
        }
        //위처럼 어느정도 jpql이 작성되며 이후에


        TypedQuery<Order> query = em.createQuery(jpql, Order.class)//파라미터 바인딩
                .setMaxResults(1000);//1000개의 결과까지만 제한
        //그리고 이후에 파라미터 바인딩을 동적으로 해야되기 때문에 우선적으로
        //쿼리 생성 객체만 생성
        //상태 바인딩
        if(orderSearch.getOrderStatus()!=null){
            query=query.setParameter("status",orderSearch.getOrderStatus());
        }
        //이름 바인딩
        if(StringUtils.hasText(orderSearch.getMemberName())){
            query=query.setParameter("name",orderSearch.getMemberName());
        }
        return  query.getResultList();
        //d이렇게 문자를 += 형태나 빌더 형태로 사용하면 상당히 에러가 많을 것이고
        //내용도 쓸대없이 길어진다.
        //그리고 실수로 인한 버그가 생성될 이유가 많다.
        //마이바티스는 위와 같은 동적 쿼리 생성이 편리하다는 이점을 가지게 된다.



    }
    //--동적 쿼리 해결방안 2안--//
    //findByallCriteria by
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        //jpa가 제공하는 표준 동적 쿼리를 빌드해주는 jpa표준
        //실무에서는 쓰지 못한다.
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);
        List<Predicate> criteria = new ArrayList<>();
        //동적 쿼리 조합을 편하게 만들 수 있는데
        //주문 상태 검색
        if(orderSearch.getOrderStatus()!=null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.<String>get("name"),"%"+orderSearch.getMemberName()+"%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }
    //빌드하고 나면 크라이테리아로 동적 쿼리를 작성할 때 메리트가 큰데
    //치명적인 단점이 존재한다.
    //이건 실무에 맞지 않는 방식>>이 코드를 보면 유지보수성이 거의 0에 가깝다.
    //이걸 보면 어떤 쿼리를 사용했는 지 눈에 잘 보이지 않는다. 그래서 jpa표준 스팩에 있지만
    //크게 쓰지 않는다. 도저히 유지보수가 안되기 때문이다.
    //criteria는 jpa스팩에 설명이 존재

    //이런 동적 쿼리를 더 단순하게 설계할 수 있는 방법
    //위와 같은 문제를 해결하기 위해서 나온 것이 query DSL이다.


}
