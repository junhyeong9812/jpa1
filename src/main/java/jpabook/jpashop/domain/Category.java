package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name ="category_item",
            //중간 테이블 매핑이 필요
            //객체는 컬렉션끼리 매칭이 가능한데 관계형 DB는 다대다 관계가 불가능
            //하기때문에 이런 구조가 필요
            joinColumns=@JoinColumn(name = "category_id"),
            //중간테이블에 존재하는 카테고리 아이디
            inverseJoinColumns=@JoinColumn(name = "item_id" )
            //이건 아이템의 컬럼을 넣는다.
    )
    private List<Item> items=new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")//내 자신을 지정
    private Category parent;

    @OneToMany(mappedBy = "parent")//부모 속성에 mapping을 건다.
    private List<Category> child=new ArrayList<>();
    //위처럼 셀프로 서로의 연관 관계를 걸 수 있다.

    //연관 관계 편의 메소드 사용
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
