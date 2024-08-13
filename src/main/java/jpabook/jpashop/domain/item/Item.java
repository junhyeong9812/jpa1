package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    //구현체를 가지고 할 것이기 때문에 abstract사용
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private  int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories=new ArrayList<>();

    //상품을 주문할 때 재고의 증감을 파악하기 위한 기능
    //==비즈니스 로직==/
    //stock증가 함수
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }
    //stock감소 함수
    public void removeStock(int quantity){
        //감소 연산
        int restStock =this.stockQuantity-quantity;
        //감소 후 0보다 작은 지 판별하는 로직 추가
        if(restStock<0){
            throw new NotEnoughStockException("재고가 부족합니다.");
        }
        //예외가 안됬다면 정상적으로 값을 처리한다.
        this.stockQuantity=restStock;
    }
    //이와 같이 내부의 stockQuantity를 변경해야될 일이 있으면 setter가 아닌
    //핵심 비즈니스 메소드를 가지고 변경하는 게 좋다.
    //밖에서 연산하는 게 아니라 내부에서 로직을 동작시키는게 객체지향 다운 설계이다.


}
