package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Movie;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 책상품등록() throws Exception{
        //given
        //item은 추상클래스이기 때문에 이 클래스를 상속받는
        //Book/Album/Movie클래스를 사용하여 테스트
        Book book = new Book();
        book.setName("JPA??");
        book.setPrice(10000);
        book.addStock(100);
        book.setAuthor("김준형");
        book.setIsbn("123-4567890123");
        //when
        itemRepository.save(book);
        //save를 호출할 때 Id어노테이션을 통해 Id값을 채우게 된다.
        //그래서 SAVE이후 book.getId()를 사용 가능
        //then
        em.flush();
        Assert.assertEquals(book,itemRepository.findOne(book.getId()));
    }
    @Test
    public void 앨범상품등록() throws Exception{
        //given
        //item은 추상클래스이기 때문에 이 클래스를 상속받는
        //Book/Album/Movie클래스를 사용하여 테스트
        Album album = new Album();
        album.setName("Album");
        album.setPrice(15000);
        album.setStockQuantity(5);
        album.setArtist("Artist");
        album.setEtc("Edition");
        //when
        itemRepository.save(album);
        //save를 호출할 때 Id어노테이션을 통해 Id값을 채우게 된다.
        //그래서 SAVE이후 book.getId()를 사용 가능
        //then
        em.flush();
        Assert.assertEquals(album,itemRepository.findOne(album.getId()));
    }
    @Test
    public void 영화상품등록() throws Exception{
        //given
        //item은 추상클래스이기 때문에 이 클래스를 상속받는
        //Book/Album/Movie클래스를 사용하여 테스트
        Movie movie = new Movie();
        movie.setName("Movie");
        movie.setPrice(20000);
        movie.setStockQuantity(20);
        movie.setDirector("Director");
        movie.setActor("Actor");
        //when
        itemRepository.save(movie);
        //save를 호출할 때 Id어노테이션을 통해 Id값을 채우게 된다.
        //그래서 SAVE이후 book.getId()를 사용 가능
        //then
        em.flush();
        Assert.assertEquals(movie,itemRepository.findOne(movie.getId()));
    }
    @Test
    public void 갯수감소() throws Exception{
        //given
        Book book = new Book();
        book.setName("JPA??");
        book.setPrice(10000);
        book.addStock(10);
        book.setAuthor("김준형");
        book.setIsbn("123-4567890123");
        itemRepository.save(book);
        //when
        book.removeStock(5);
        //then
        Assert.assertEquals(5, book.getStockQuantity());
        //Expection test
        Assert.assertThrows(NotEnoughStockException.class, () -> book.removeStock(6));
        //
    }
}
