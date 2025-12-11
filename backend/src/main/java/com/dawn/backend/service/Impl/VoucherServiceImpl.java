package com.dawn.backend.service.Impl;

import com.dawn.backend.exception.wrapper.ResourceNotFoundException;
import com.dawn.backend.model.Voucher;
import com.dawn.backend.repository.VoucherRepository;
import com.dawn.backend.service.VoucherService;
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
