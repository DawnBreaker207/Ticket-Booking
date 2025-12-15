package com.dawn.catalog.service.Impl;

import com.dawn.catalog.model.Voucher;
import com.dawn.catalog.repository.VoucherRepository;
import com.dawn.catalog.service.VoucherService;
import com.dawn.common.exception.wrapper.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository voucherRepository;

    @Override
    public List<Voucher> getAll() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher create(Voucher req) {
        return voucherRepository.save(req);
    }

    @Override
    public Voucher update(Voucher req) {
        return voucherRepository.save(req);
    }

    @Override
    public void delete(Long id) {
        voucherRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        voucherRepository.deleteById(id);
    }
}
