package jpabook.jpashop.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    @GetMapping("/members/new")
    public String createForm(Model model){
        //Model이란 controller에서 view로 넘어갈 때 데이터를 실어서 넘긴다.
        model.addAttribute("memberForm",new MemberForm());
        //model 내부에 MemberForm의 껍데기를 가져간다. 밸리데이션이나 그런 것을 해주기 때문에
        //가져간다.
        return "members/createMemberForm";
        //화면 이동 시
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result){
        //회원 이름을 필수로 사용하고 싶을 경우 @Valid를 사용
        //사용하면 다양한 어노테이션 기능을 스프링에서 벨리데이션을 해준다.
        if(result.hasErrors()){
            //이때 result가 에러를 페이지로 가져갈 수 있다.
            return "members/createMemberForm";
        }
        Address address =
                new Address
                        (memberForm.getCity(), memberForm.getStreet(),
                                memberForm.getZipcode());
        Member member =new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
                //저장 후 재로딩 되거나 하면 안되서 리다이렉트로 home으로 보낸다.
    }
    @GetMapping("/members")
    public String list(Model model){
        List<Member> members=memberService.findMembers();
        //이부분도 화면에 필요한 부분만 DTO나 폼으로 데이터를 만들어서 넘기는 것이 좋다.
        //템플릿 엔진에서는 서버 안에서 돌기 때문에 이렇게 전달해도 되지만
        //API를 만들 때는 이유 불문하고 절대 엔티티를 넘기면 안된다.
        //절대 기억해!! API를 만들 때는 이유 불문!! 절대 엔티티를 외부 반환x
        //API는 스팩인데 엔티티를 반환하면 필드가 추가되버린다. 패스워드도 노출되고
        //API 스팩도 변한다.>>정말 불완전한 스팩이 된다.
        model.addAttribute("members",members);
        //리스트를 모델에 담아서 화면으로 넘긴다. ctrl alt n으로 인라인 방식으로 구현
        return "members/memberList";
    }
}
