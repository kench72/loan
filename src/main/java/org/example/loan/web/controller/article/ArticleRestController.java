package org.example.loan.web.controller.article;

import org.example.loan.service.article.ArticleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ArticleRestController {

    private final ArticleService _articleService = new ArticleService();

    // GET /articles/1
    @GetMapping("/articles/{id}")
    public ArticleDto showAritcle(@PathVariable("id") long id) {

        var enttiy = _articleService.findById(id);

        return new ArticleDto(
                enttiy.id(),
                enttiy.title(),
                enttiy.contents(),
                enttiy.createdAt(),
                enttiy.updatedAt()
        );
    }
}
