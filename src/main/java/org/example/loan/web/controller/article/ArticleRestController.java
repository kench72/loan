package org.example.loan.web.controller.article;

import lombok.RequiredArgsConstructor;
import org.example.loan.service.article.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService _articleService;

    // GET /articles/1
    @GetMapping("/articles/{id}")
    public ArticleDto showArticle(@PathVariable("id") long id) {

        return _articleService.findById(id) // Optional<ArticleEntity>
                .map(ArticleDto::from) // Optional<ArticleDTO>
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); // AritcleDTO
    }
}
