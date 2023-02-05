package com.kramphub.jiramic.controllers;

import com.kramphub.jiramic.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private OAuthService oAuthService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String getLoginPage(Model model) {
        logger.info("--> LoginController.getLoginPage(...)");

        model.addAttribute("url", oAuthService.getOAuthLoginUrl());

        // check to see if the user is authenticated
        model.addAttribute("authenticated", oAuthService.isUserAuthenticated());
        if (oAuthService.isUserAuthenticated()) {
            model.addAttribute("principal",  oAuthService.getAuthenticationToken().getPrincipal().getName());
        }

        logger.info("<-- LoginController.getLoginPage(...)");
        return "index";
    }

    @GetMapping("/login-failure")
    public String getLoginFailurePage(Model model, HttpServletRequest request, HttpSession session) {
        model.addAttribute("SPRING_SECURITY_LAST_EXCEPTION", session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"));
        return "login-failure";
    }
}

