package org.example.loan.service.article;

import lombok.RequiredArgsConstructor;
import org.example.loan.repository.article.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository _articleRepository;

    public Optional<ArticleEntity> findById(long id) {
        return  _articleRepository.selectById(id);
    }
}
