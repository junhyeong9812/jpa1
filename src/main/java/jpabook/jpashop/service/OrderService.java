package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    //주문
    @Transactional
    public Long order(Long memberId,long itemId,int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        //회원정보의 배달 정보를 넣는다.
        Delivery delivery =new Delivery();
        delivery.setAddress(member.getAddress());
        //실제로는 배송지 정보를 별도로 입력 받아야 된다.

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //서비스에서 구축하는 방식
//        OrderItem orderItem1 =new OrderItem();
//        orderItem1.setCount();
//        이렇게 채워넣는 식으로 서비스를 구축하는 방식도 있는데 이렇게 여기로직을 개발하게 되면
//        나중에 로직이 퍼지면서 유지보수가  힘들어진다
//                생성 로직을 변경하거나 로직을 추가하거나 그럴 때 분산할 때 필요하기 때문에
//                그래서 Order에서 별도로 protected 생성자를 통해 새로운 생성자를 만드는 것을
//                컴파일 에러가 나오도록 해야 된다.
        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        //이렇게 오더가 생성 후
        //주문 저장
        orderRepository.save(order);
        //이때 오더정보를 저장할 때 딜리버리와 오더아이템에 대해서도 별도로 리포지토리를
        //통해서 저장이 되어야 하는데 지금 이코드를 보면 오더리포지토리로 단일 저장을 하는데
        //이게 가능한 이유는 Cascade옵션때문에 가능한 것이다.
        //cascadeType.all로 설정하면 Order를 Persist하게 되면 그 내부에 컬랙션들도 강제로
        //퍼시스트를 하게 된다.
        //이 cascade범위는 여러 고민을 하기는 하는데 보통은
        //참조하는 주인이 프라이빗 오더인 경우에만 사용
        //현재는 오더 아이템과 딜리버리는 오더에서만 사용된다.
        //라이프 사이클에 대해서 동일하게 관리를 할 때는 의미가 있다.
        //다른 것이 참조할 수 없는 프라이빗 오더인 경우 도움이 된다.
        //하지만 딜리버리가 다른 곳에서도 사용된다면 케스케이드를 막 쓰면 안된다.
        //복잡하게 얽혀서 돌아가기 때문에 케스케이드가 아닌 리포지토리를 생성해서
//        퍼시스트를 별도로 하는 게 좋다
        //퍼시스트 라이프라이클이 동일한 지/다른 곳에서 참조를 하고 있는 지를 기준으로
        //케스케이드 사용 유무를 판단하면 된다.
        return order.getId();//오더의 식별자 값은 따로 반환
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
        //오더 자체에 주문 취소 로직이 있기 때문에 이를 통해 취소가 진행된다.
        //jpa의 강점이 이런 부분에서 나오는데
        //원래는 마이바티스나 jdbc에서는 데이터를 변경하게 되면 업데이트 쿼리를 직접
        //  짜서 날려줘야 된다. 리포지토리로
        //아이템 로직을 바꾸고 데이터를 꺼내서 쿼리에다가 파라미터를 넣고 동작시켜야 된다.
//        그렇기 때뭉네 서비스 계층에서 비즈니스 로직을 쓰게 된다. sql을 직접 사용한다면
//                하지만 jpa를 활용하면 데이터를 변경하면 하면  jpa가 알아서 바뀐 변경 포인트를
//                dirty checking(변경내용 감지)가 일어나면서 변경된 내역을 찾아서
//                데이터베이스에 업데이트 쿼리가 날라간다.
//        오더의 아이템은 변경된 게 없지만 아이템에 스톡 퀀티티가 달라지면서 업데이트 쿼리가 날라간다

    }

    //검색
//    public List<Order> findOrder(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }

}
