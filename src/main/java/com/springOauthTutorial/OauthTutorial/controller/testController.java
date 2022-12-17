package com.springOauthTutorial.OauthTutorial.controller;
import com.springOauthTutorial.OauthTutorial.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class testController {
    @Autowired
    private UserRepository userRepository;
    @ResponseBody
    @GetMapping("/submit")
    public String helloController(){
        return "hello";
    }



    @GetMapping("/basic/login")
    public String loginBasic(Model model){
        System.out.println("hello?");
        return "login";
    }
    @ResponseBody
    @GetMapping("/create")
    public String createUser(){
        return "temp";
    }
}
