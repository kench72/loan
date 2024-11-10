package org.example.loan.service.article;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ArticleEntity (
        long id,
        String title,
        String contents,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
