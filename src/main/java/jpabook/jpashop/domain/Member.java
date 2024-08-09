package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private  Address address;
    //Address>>Create Class로 생성

    @OneToMany(mappedBy = "member" )//order테이블에 있는
    // member필드에 의해 매핑이 됬다고 설정
    private List<Order> orders = new ArrayList<>();
    //Order>>Create Class로 생성



}
