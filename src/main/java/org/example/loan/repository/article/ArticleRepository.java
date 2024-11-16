package org.example.loan.repository.article;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.loan.service.article.ArticleEntity;

import java.util.Optional;

@Mapper
public interface ArticleRepository {

    @Select("""
            SELECT
                id
                ,title
                , body
                , created_at
                , updated_at 
              FROM articles 
              WHERE id = #{id}
            """)
    Optional<ArticleEntity> selectById(@Param("id") long id);
}
