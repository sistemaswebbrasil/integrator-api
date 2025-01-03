package br.com.siswbrasil.integrator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    private String getCleanPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        return uri.substring(contextPath.length());
    }

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", "home");
        model.addAttribute("cleanPath", getCleanPath(request));
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", "about");
        model.addAttribute("cleanPath", getCleanPath(request));
        return "about";
    }
}