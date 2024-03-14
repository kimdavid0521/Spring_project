package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@Slf4j //log를 사용할 수 있음
public class HomeControllder {

    @RequestMapping("/")
    public String home() {
        log.info("homeController");
        return "home"; //이렇게 하면 home.html 파일로 찾아감
    }
}
