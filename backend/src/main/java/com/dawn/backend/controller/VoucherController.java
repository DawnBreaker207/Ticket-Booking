package com.dawn.backend.controller;

import com.dawn.backend.config.response.ResponseObject;
import com.dawn.backend.model.Voucher;
import com.dawn.backend.service.VoucherService;
import lombok.RequiredArgsConstructor;
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
