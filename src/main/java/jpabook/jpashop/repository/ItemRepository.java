package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor//final이나 notNull을 가진 객체를 생성자에
// 자동 생성되도록 한다.
public class ItemRepository {
    private final EntityManager em;
    public void save(Item item){
        if(item.getId()==null){
            em.persist(item);
            //처음 생성된 item은 Id가 없기 때문에 null인 지 확인하고 없다면
            //persist로 데이터를 저장하게 된다.
        }else {
            em.merge(item);
            //또한 만약 Id가 있다면 기존의 아이템인 것이기 때문에
            //merge를 통해 item정보를 갱신한다.
        }

    }
    public Item findOne(Long id){
        return em.find(Item.class,id);
    }//단일 객체를 찾는 것은 find함수를 사용
    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
        //jpql을 사용하여 모든 item에 대해 가져와서 그결과를 list형태로 변환하여 반환

    }


}
