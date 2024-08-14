package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String memberName; //회원 이름
    private OrderStatus orderStatus;// 주문 상태[ORDER,CANCEL]
    //위 파라미터 조건이 있다면 where문으로 검색이 되어야 한다.
}
