package com.dawn.catalog.service.Impl;

import com.dawn.catalog.dto.request.ArticleRequest;
import com.dawn.catalog.dto.response.ArticleResponse;
import com.dawn.catalog.helper.ArticleMappingHelper;
import com.dawn.catalog.model.Article;
import com.dawn.catalog.repository.ArticleRepository;
import com.dawn.catalog.service.ArticleService;
import com.dawn.common.core.exception.wrapper.ResourceNotFoundException;
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
