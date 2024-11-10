package org.example.loan.web.controller.article;

import org.example.loan.service.article.ArticleEntity;

import java.time.LocalDateTime;

public record ArticleDto(
        long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ArticleDto from(ArticleEntity entity) {
        return new ArticleDto(
                entity.id(),
                entity.title(),
                entity.contents(),
                entity.createdAt(),
                entity.updatedAt()
        );
    }
}
