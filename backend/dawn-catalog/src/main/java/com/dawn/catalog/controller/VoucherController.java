package com.dawn.catalog.controller;

import com.dawn.catalog.model.Voucher;import com.dawn.catalog.service.VoucherService;import com.dawn.common.config.response.ResponseObject;import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("")
    public ResponseObject<List<Voucher>> findAll() {
        return ResponseObject.success(voucherService.getAll());
    }

    @PostMapping("")
    public ResponseObject<Voucher> create(@RequestBody Voucher req) {
        return ResponseObject.created(voucherService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseObject<Voucher> update(@RequestBody Voucher req) {
        return ResponseObject.success(voucherService.update(req));
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Long id) {
        voucherService.delete(id);
        return ResponseObject.deleted();
    }
}
