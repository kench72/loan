package org.example.loan.web.controller.article;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ArticleRestController {

    // GET /articles/1
    @GetMapping("/articles/{id}")
    public ArticleDto showAritcle(@PathVariable("id") long id) {

        return new ArticleDto(
          id,
          "This is title: id = " + id,
          "This is content",
          LocalDateTime.now(),
          LocalDateTime.now()
        );
    }
}
