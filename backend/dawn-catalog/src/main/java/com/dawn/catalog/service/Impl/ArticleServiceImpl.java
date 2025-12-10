package com.dawn.backend.service.Impl;

import com.dawn.backend.dto.request.ArticleRequest;
import com.dawn.backend.dto.response.ArticleResponse;
import com.dawn.backend.exception.wrapper.ResourceNotFoundException;
import com.dawn.backend.helper.ArticleMappingHelper;
import com.dawn.backend.model.Article;
import com.dawn.backend.repository.ArticleRepository;
import com.dawn.backend.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleResponse> getAll() {
        return articleRepository
                .findAll()
                .stream()
                .map(ArticleMappingHelper::map)
                .toList();
    }

    @Override
    public ArticleResponse getById(Long id) {
        return articleRepository
                .findById(id)
                .map(ArticleMappingHelper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
    }

    @Override
    public ArticleResponse create(ArticleRequest req) {
        Article article = ArticleMappingHelper.map(req);
        return ArticleMappingHelper.map(articleRepository.save(article));
    }

    @Override
    public ArticleResponse update(Long id, ArticleRequest req) {
        Article article = articleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.setTitle(req.getTitle());
        article.setSlug(req.getSlug());
        article.setSummary(req.getSummary());
        article.setContent(req.getContent());
        article.setStatus(req.getStatus());
        return ArticleMappingHelper.map(articleRepository.save(article));
    }

    @Override
    public Void delete(Long id) {
        articleRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.deleteById(id);
        return null;
    }
}
