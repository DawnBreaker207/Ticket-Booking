package com.dawn.catalog.controller;

import com.dawn.catalog.dto.request.ArticleRequest;
import com.dawn.catalog.dto.response.ArticleResponse;
import com.dawn.catalog.service.ArticleService;
import com.dawn.common.dto.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("")
    public ResponseObject<List<ArticleResponse>> getAll() {
        return ResponseObject.success(articleService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseObject<ArticleResponse> getOne(@PathVariable Long id) {
        return ResponseObject.success(articleService.getById(id));
    }

    @PostMapping("")
    public ResponseObject<ArticleResponse> create(@RequestBody ArticleRequest req) {
        return ResponseObject.created(articleService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseObject<ArticleResponse> update(@PathVariable Long id, @RequestBody ArticleRequest req) {
        return ResponseObject.success(articleService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseObject.deleted();
    }

}
