package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//생성자 접근 레벨을 프로텍트로 걸어서 생성 메소드가 아닌 생성자를 통한 생성을 막을 수 있다.
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    //create로 Item생성

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;//주문 가격
    private int count;//주문 수량
    //이렇게 protected를 생성자에 걸어놓으면 쓰지말라는 의미로 사용된다.
    //이렇게 제약을 걸 수 있다.
//    protected OrderItem(){
//
//    }
    //--생성 메서드--//
    public static OrderItem createOrderItem(Item item,int orderPrice,int count){
        OrderItem orderItem= new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
    //--비즈니스 로직--//
    public void cancel() {
        getItem().addStock(count);
        //현재 이 메서드의 상품 객체를 가져와서 주문 수량만큼 갯수를 증가시켜줘야된다.
    }
    //--가격 조회 로직--//
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
        //이렇게 주문 가격과 주문 수량을 곱해서 반환해주는 로직 추가
    }
}
