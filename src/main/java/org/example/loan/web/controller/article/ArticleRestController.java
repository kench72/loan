package org.example.loan.web.controller.article;

import org.example.loan.service.article.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
public class ArticleRestController {

    private final ArticleService _articleService = new ArticleService();

    // GET /articles/1
    @GetMapping("/articles/{id}")
    public ArticleDto showAritcle(@PathVariable("id") long id) {

         var dto =  _articleService.findById(id) // Optional<ArticleEntity>
                // map 変換・写像
                .map(entity ->
                     new ArticleDto(
                            enttiy.id(),
                            enttiy.title(),
                            enttiy.contents(),
                            enttiy.createdAt(),
                            enttiy.updatedAt()
                    )
                ) // Optional<ArticleDTO>
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // AritcleDTO
        return dto;
    }
}
