package org.yann.eureka.client.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {

    @GetMapping("")
    public String forwardModify(RedirectAttributes redirectAttributes, Model model) {

        return "index";
    }

    @GetMapping("/math-expression")
    public String mathExpressionPage() {
        return "math-expression";
    }
}
