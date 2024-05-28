package vn.ecall.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtagController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello etag Header";
    }
}
