package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    //==생성 메서드==//
    //복잡한 연관관계를 가진 경우 별도의 생성 메서드가 존재하는 게 좋다.
    public static Order createOrder
    (Member member,Delivery delivery,OrderItem... orderItems){
        //...문법을 통해 여러개를 넘기도록 선언
        Order order =new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem: orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        //초기값은 오더 스테이터스가 오더 상태로 설정
        order.setOrderDate(LocalDateTime.now());
        //주문시간은 현재 시간으로
        return order;
        //이와 같이 생성 메서드를 통해 생성하는 것이 중요한 이유는
//        생성하는 지점 변경해야되면 생성 메서드만 바꾸면 되기 때문이다.

    }
    //==비즈니스 로직==//
//  주문취소
    public void cancel(){
        //만약 배송이 완료되어 있으면 취소가 불가능하도록 설정
        if(delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        //위 조건을 통과하면 주문의 상태를 캔슬로 변경
        for(OrderItem orderItem: orderItems){
            orderItem.cancel();
        }//주문에 있는 아이템들에 대해 취소 함수를 동작시켜서 재고를 원래대로 되돌린다.
    }
    //--조회 로직--//
    //전체 주문 가격 조회
    public int getTotalPrice(){
        //모든 주문 상품의 가격을 다 더하면 되는데
        int totalPrice=0;
        for(OrderItem orderItem: orderItems){
            totalPrice+=orderItem.getTotalPrice();
            //주문할 때 가격과 수량이 orderItem내부에 있기 때문에 내부 비즈니스 로직을 통해
            //총 갯수를 연산 후 더해주는 것이다.
        }
        return totalPrice;
        //람다나 스트림을 사용하면 더욱 편리하게 사용할 수 있다.
    }
    //스트림 로직
    //기본로직 for문에 alt enter를 통해 Stream형태로 변환하면 아래와 같이 stream형태로 변경 가능
//    public int getTotalPrice(){
//        //모든 주문 상품의 가격을 다 더하면 되는데
//        int totalPrice= orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//        //주문할 때 가격과 수량이 orderItem내부에 있기 때문에 내부 비즈니스 로직을 통해
//        //총 갯수를 연산 후 더해주는 것이다.
//        return totalPrice;
//        //람다나 스트림을 사용하면 더욱 편리하게 사용할 수 있다.
//    }


}
