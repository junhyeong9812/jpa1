package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.DeliveryStatus;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;
    @Test
    public void updateTest() throws Exception{
        //given
        Book book = em.find(Book.class, 1L);
        //Tx
        book.setName("asdfsf");
        //jpa가 원래는 변경 부분에 대해서 찾아서 업데이트 쿼리를 자동으로 생성해서 반영을 하는데
        //이걸 더티 채킹이라 하며 변경감지라 한다.
        //변경 감지 ==dirty Checking
//        public void cancel(){
//            //만약 배송이 완료되어 있으면 취소가 불가능하도록 설정
//            if(delivery.getStatus()== DeliveryStatus.COMP){
//                throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
//            }
//            this.setStatus(OrderStatus.CANCEL);
//            //위 조건을 통과하면 주문의 상태를 캔슬로 변경
//            for(OrderItem orderItem: orderItems){
//                orderItem.cancel();
//            }//주문에 있는 아이템들에 대해 취소 함수를 동작시켜서 재고를 원래대로 되돌린다.
//        위 취소 코드를 보면 오더의 상태를 변경하고 db에 업데이트를 하는 명령어가 별도로 없는데
//        값을 바꿔놓으면 트렌젝션 커밋 시점에 db업데이트문을 날린다.
        //flush할 때 더티채킹이 일어나는 것이다. 하지만
        //준영속 엔티티일 때 문제가 생긴다.
        //영속성 컨텍스터가 더이상 관리하지 않는 것을 준속성 엔티티라 하는데
        //예시로 우리가 수정할 때 BookForm으로 받은 데이터로 book을 생성하면 id가 세팅이 되어있는
        //값에 대해서 이미 들어갔다가 나온 값>>DB에 갔다 온 식별자가 있는 것을 준속성 객체라 한다.
        //쉽게 말해 더이상 영속성으로 관리하지 않는 객체인 것
        //이런 준영속성 엔티티는 jpa가 관리하지 않는다.
        //관리하면 변경감지가 일어나는데 준영속성은 변경감지가 일어나지 않는다.
        //그래서 book에 값을 바꿔도 db에 업데이트가 안일어난다.
        //이런 준영속성 상태는 어떻게 데이터를 변경할 수 있을까??
        //변경 감지 기능이 인식하게 하거나 merge를 쓰는 방법 두가지가 존재한다.
//        변경감지에 잡히는 방법은 ItemService에서 구현

        //Tx commit;
    }
}
