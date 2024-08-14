package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j //롬복 사용 시 아래 로거팩토리를 어노테이션으로 대체 가능
public class HomeController {
//    Logger log = LoggerFactory.getLogger(getClass());
//    //slf4j로거를 사용
    @RequestMapping("/")
    public String home(){
        log.info("home Controller");
        return "home";
        //return home이란 home.html로 타임 리프 파일을 찾아가게 된다.
    }
}
