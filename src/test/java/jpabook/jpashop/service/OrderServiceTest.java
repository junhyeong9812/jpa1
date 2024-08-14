package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();
        //ctrl alt m을 통해 외부에 생성하는 함수로 분리

        Book book = createBook("JPA1", 10000, 10);
        int orderCount =2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
        //어설트 이퀄에서는 처음 메세지,기댓값,비교할 값 이렇게 3개의 값을 넣게 된다.
        assertEquals("주문한 상품 종류 수가 정확해야 한다",1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격* 수량이다.",10000*orderCount,getOrder.getTotalPrice());
        assertEquals("주문수량만큼 재고가 줄어야 한다.",8,book.getStockQuantity());
    }



    @Test(expected = NotEnoughStockException.class)//재고수량 초과 시 익셉셥을 item에서 사용
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createBook("JPA1", 10000, 10);
        int orderCount =11;


        //when
        orderService.order(member.getId(), item.getId(), orderCount);
        //then
        fail("재고 수량 부족 예외가 발행해야 한다.");
        //만약 정상 로직으로 넣어봣을 때 fail까지 오면 잘 작동되는 것까지도 확인 가능하다.
    }
    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("jpa", 10000, 10);
        int orderCount =2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //준비완료까지가 테스트 준비 단계
        //when
        orderService.cancelOrder(orderId);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소 시 상태는 CANCEL이다.",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.",10,book.getStockQuantity());

    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
    //ctrl alt p를 통해 변수를 외부로 꺼내서 설정하도록 할 수 있다.

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","종로구","12321"));
        em.persist(member);
        return member;
    }
}