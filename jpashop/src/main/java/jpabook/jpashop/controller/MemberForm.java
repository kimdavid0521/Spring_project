package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.") //이름을 필수로 받기 위해 설정해주기
    private String name;
    private String city;
    private String street;
    private String zipcode;

}
