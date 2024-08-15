package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        //화면을 생각해보면 회원과 상품을 선택하도록 되어있다.
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();
        //모델을 통해 view로 데이터를 넘겨준다.
        model.addAttribute("members",members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
        //@RequestParam은 form submit방식으로 데이터가 오면 select의 name의 value값을
        //받을 수 있게 된다.
        //변수 바인딩 완료가 된다. 이때 컨트롤러에서 form데이터 내부의 값을 직접 찾는 것보다
        //리쿼스트 파람을 통해 바인딩하는게 좋은 이유는 컨트롤러 로직이 지저분해지며
        //코드 자체가 단순화되는 것이기 때문에 직관적이고 깔끔하다.
        //또한 엔티티를 서비스 계층에서 찾는 게 할 수 있는 것도 더 많아진다.
        //그리고 엔티티들도 영속성 상태로 흘러가기 때문에 훨씬 깔끔하게 동작하도록 할 수 있다.
        //주로 커맨드성 주문등은 컨트롤러 내부에서는 식별자만 넘기고 핵심 비즈니스 서비스에서
        //엔티티를 찾는 것부터 전부 거기서 하면 엔티티 값들도 트랜젝션 안에서 엔티티를 조회해야
        //영속상태로 진행되기 때문에 맴버나 아이템의 상태도 변경할 수 있기 때문에
        //가급적 커맨드성 핵심 비즈니스 로직은 밖에서 식별자만 넘기고 비즈니스 로직에서 찾아서
        //영속성 상태를 유지시키는 것이 중요하다.
        //컨트롤러에서 값을 가져와버리면 트렌젝션 없이 관리되기 때문에 영속성 상태가 끝나버린다.
        //그 상태로 오더에 넘어가면 jpa와 관계없는 준영속성 상태로 넘어오는 것
        //그냥 서비스 트렌젝션이 동작하는 곳에서 찾아!

        Long order = orderService.order(memberId, itemId, count);
        //이렇게 order식별자를 받아서 이걸 통해 결과 페이지로 리다이렉션을 해줘도 좋다.
        return "redirect:/orders";

    }
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch,
                            Model model){
        List<Order> orders = orderService.findOrder(orderSearch);
        model.addAttribute("orders",orders);
        return "order/orderList";
        //여기로 값이 넘어와서 선택된 데이터를 그대로 넘겨줘서
    }
    //주문 취소
//     <a th:if="${item.status.name() == 'ORDER'}" href="#"
//    th:href="'javascript:cancel('+${item.id}+')'"
//    class="btn btn-danger">CANCEL</a>
//    <script>
//    function cancel(id) {
//        var form = document.createElement("form");
//        form.setAttribute("method", "post");
//        form.setAttribute("action", "/orders/" + id + "/cancel");
//        document.body.appendChild(form);
//        form.submit();
//    }
//</script>
    //위 코드를 참조하여 주문 취소 경로를 생성
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
