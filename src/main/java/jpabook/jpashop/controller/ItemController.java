package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }
    @PostMapping("/items/new")
    public String create(BookForm bookForm){
        Book book=new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());
        //편의를 위해 여기서는 setter를 사용했지만 더 좋은 방식은
        //createBook을 통해 파라미터를 넘기는 게 더 좋은 설계이다.
        //그렇기 때문에 이렇게 생성할 때는 static생성자 메소드를 통해 의도에 맞도록
        //생성하도록 하는 게 좋다.
        itemService.saveItem(book);
        return "redirect:/items";
    }
    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }
    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model ){
        Book item =(Book) itemService.findOne(itemId);
        BookForm form =new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form",form);
        //모델 메퍼 같은 라이브러리를 쓰면 좀 더 편하게 사용 가능
        //너무 많으면 라인 셀렉트를 통해 dto나 폼에서 데이터를 가져와서 사용할 때도 있다.
        //멀티라인 셀렉트 활용
        return "items/updateItemForm";
    }
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form,@PathVariable("itemId") Long itemId ){
//        @ModelAttribute("form")이걸 넣는 이유는 html에서 오브젝트 명이 form이였으며
//        넘어오는 오브젝트 명을 지정 할 수 있다.
//        Book book = new Book();
//        //ctrl ctrl 아래키로 사용으로 다중커서 활성화
//        //이후 컨트롤 시프트로 이름 선택
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);
        UpdateItemDto dto=new UpdateItemDto();
        dto.setName(form.getName());
        dto.setPrice(form.getPrice());
        dto.setStockQuantity(form.getStockQuantity());
        itemService.updateItem1
                (itemId, dto
//                        form.getPrice(),form.getName(),form.getStockQuantity()

                );
        return "redirect:/items";
    }
}
