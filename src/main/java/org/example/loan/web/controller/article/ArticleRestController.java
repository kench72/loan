package org.example.loan.web.controller.article;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleRestController {

    // GET /articles/1
    @GetMapping("/articles/{id}")
    public String showAritcle(@PathVariable("id") long id) {
        return "This is artcle:id = " + id;
    }
}
