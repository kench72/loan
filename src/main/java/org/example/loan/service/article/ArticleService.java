package org.example.loan.service.article;

import java.time.LocalDateTime;

public class ArticleService {

    public ArticleEntity findById(long id){
        return new ArticleEntity(
                id,
                "title",
                "contents",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
