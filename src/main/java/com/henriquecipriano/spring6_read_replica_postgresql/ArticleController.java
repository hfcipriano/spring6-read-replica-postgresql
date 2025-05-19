package com.henriquecipriano.spring6_read_replica_postgresql;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
@AllArgsConstructor
class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    ResponseEntity<ArticleEntity> findArticleById(@PathVariable Long id) {
        return this.articleService
                .findArticleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    ResponseEntity<Object> saveArticle(@RequestBody ArticleEntity articleDTO) {
        Long articleId = this.articleService.saveArticle(articleDTO);
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("{id}")
                        .buildAndExpand(articleId)
                        .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/random")
    ResponseEntity<Object> saveArticleRandom() {
        ArticleEntity random = ArticleEntity.builder()
                .id(Long.valueOf(new Random().nextInt(1000)))
                .title(UUID.randomUUID().toString())
                .authored(LocalDateTime.now())
                .build();

        Long articleId = this.articleService.saveArticle(random);
        URI location =
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("{id}")
                        .buildAndExpand(articleId)
                        .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        boolean exists = this.articleService.existsById(id);
        if (exists) {
            this.articleService.deleteById(id);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
