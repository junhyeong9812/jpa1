package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.")//값이 비여있으면 에러 메세지를 표현
    private String name;
    private String city;
    private String street;
    private String zipcode;

}
