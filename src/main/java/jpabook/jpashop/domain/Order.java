package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)//다 대 다 관계를 생성
    @JoinColumn(name = "member_id")//매핑을 어떤 것을 사용해서 할 지 설정
    private Member member;   //foriegn key가 member_id가 되는 것
    //이때는 전에 말한 연관관계 주인을 정해야 되는데 이때
    //member와 Order의 관계를 변경하고 싶을 때 주인은 이대로 두면 되고
    // 추가로 mapped by 설정 필요

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL) //1의 관계이기 때문에 oneToMany설정
    private List<OrderItem> orderItems = new ArrayList<>();
    //OrderItem create로 생성

    @OneToOne(fetch = LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private  Delivery delivery;
    //Delivery create로 생성

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주문의 상태 [ORDER,CANCEL]두가지
    //OrderStatus create로 생성

    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }//이렇게 따로 넣어야 된다.
    //원래대로라면
//    public static void main(String[] args){
//        Member member=new Member();
//        Order order = new Order();
//        //위처럼 객체를 생성하고
//        member.getOrders().add(order);
//        order.getMember(member);
//    }//위 코드처럼 메인에서 객체를 주입해줘야 한다.
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }



}
