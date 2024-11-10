package org.example.loan.web.controller.article;

import java.time.LocalDateTime;

public record ArticleDto (
        long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
