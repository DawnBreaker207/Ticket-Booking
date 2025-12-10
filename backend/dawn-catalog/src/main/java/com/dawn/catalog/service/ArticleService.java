package com.dawn.catalog.service;

import com.dawn.catalog.dto.request.ArticleRequest;
import com.dawn.catalog.dto.response.ArticleResponse;

import java.util.List;

public interface ArticleService {
    List<ArticleResponse> getAll();

    ArticleResponse getById(Long id);

    ArticleResponse create(ArticleRequest req);

    ArticleResponse update(Long id, ArticleRequest req);

    Void delete(Long id);
}
