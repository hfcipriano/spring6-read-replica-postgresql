package com.henriquecipriano.spring6_read_replica_postgresql;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Optional<ArticleEntity> findArticleById(Long id) {
        return this.articleRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return articleRepository.existsById(id);
    }

    @Transactional
    public Long saveArticle(ArticleEntity article) {
        ArticleEntity savedArticle = this.articleRepository.save(article);
        return savedArticle.getId();
    }

    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
}
