package com.dawn.backend.service;


import com.dawn.backend.model.Voucher;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAll();

    Voucher create(Voucher req);

    Voucher update(Voucher req);

    void delete(Long id);
}
