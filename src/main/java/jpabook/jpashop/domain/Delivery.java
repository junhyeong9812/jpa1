package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = LAZY)
    private Order order;
    
    @Embedded //내장타입은 별도 설정
    private Address address;

    @Enumerated(EnumType.STRING)//디폴트인 ORDINAL는 1234숫자로 들어간다.
    private DeliveryStatus status; //READY,COMP 배송준비/배송
    //READY,COMP에서 ORDINAL는 0,1 이런 식으로 들어간다.
    //이때 중간에 다른 상태가 들어오면 READY,xxx,COMP이런 식이 되면
    //다 꼬인다. 그래서 반드시 String을 사용
    //그래야 순서로 밀리는 게 없다.
}
