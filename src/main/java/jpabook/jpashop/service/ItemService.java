package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional //전역 설정이 readOnly이기 때문에 오버라이딩을 통해
    //readOnly가 save함수에 대해서는 false가 되도록 설정
    public void saveItem(Item item){
        itemRepository.save(item);
    }
    @Transactional
//    public void updateItem(Long itemId, Book bookParam){
    public void updateItem(Long itemId, Book bookParam){
        Item findItem = itemRepository.findOne(itemId);
        //위처럼 영속상태의 아이템을 찾아온다.
        //이렇게 찾아온 아이템은 영속상태 엔티티를 찾아온 것으로
        findItem.setPrice(bookParam.getPrice());
        findItem.setName(bookParam.getName());
        findItem.setStockQuantity(bookParam.getStockQuantity());
        //이렇게 데이터를 영속상태 엔티티에 집어넣고
//        itemRepository.save(findItem);
        //이렇게 수정만 하고 아무것도 호출 할 필요가 없다.
        //다시한번 findOne으로 직접 DB에서 값을 찾아온 것은 영속 상태로 본다.
        //여기서 끝나면 스프링의 트렌젝션에 의해 commit이 되며 flush를 하게 된다.
        //영속성 변경내용을 flush단계에서 찾아서 업데이트문을 날린다.
//        return;
        //그리고 업데이트는 위와 같이 단발성으로 동작하면 안된다.
        //엔티티나 폼 내부에 의미있는 메소드를 만들어서 동작해야된다. set으로 하는 게 아닌
        //변경지점을 엔티티로 해야된다.
        //만약 복잡해지면 set데이터를 어디서 바꾸는 지 이걸 찾는 것도 일이 되어버린다.
    }
    //변경
    @Transactional
//    public void updateItem(Long itemId, Book bookParam){
    public void updateItem1(Long itemId,
//                            int price,String name,int stockQuantity
    UpdateItemDto updateItemDto
    ){
        Item findItem = itemRepository.findOne(itemId);
        //위처럼 영속상태의 아이템을 찾아온다.
        //이렇게 찾아온 아이템은 영속상태 엔티티를 찾아온 것으로
//        findItem.setPrice(price);
//        findItem.setName(name);
//        findItem.setStockQuantity(stockQuantity);
        findItem.setPrice(updateItemDto.getPrice());
        findItem.setName(updateItemDto.getName());
        findItem.setStockQuantity(updateItemDto.getStockQuantity());
        //이렇게 데이터를 영속상태 엔티티에 집어넣고
//        itemRepository.save(findItem);
        //이렇게 수정만 하고 아무것도 호출 할 필요가 없다.
        //다시한번 findOne으로 직접 DB에서 값을 찾아온 것은 영속 상태로 본다.
        //여기서 끝나면 스프링의 트렌젝션에 의해 commit이 되며 flush를 하게 된다.
        //영속성 변경내용을 flush단계에서 찾아서 업데이트문을 날린다.
//        return;
        //그리고 업데이트는 위와 같이 단발성으로 동작하면 안된다.
        //엔티티나 폼 내부에 의미있는 메소드를 만들어서 동작해야된다. set으로 하는 게 아닌
        //변경지점을 엔티티로 해야된다.
        //만약 복잡해지면 set데이터를 어디서 바꾸는 지 이걸 찾는 것도 일이 되어버린다.
    }
    public List<Item> findItems(){
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
