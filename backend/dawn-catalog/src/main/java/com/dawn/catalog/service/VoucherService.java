package com.dawn.catalog.service;


import com.dawn.catalog.model.Voucher;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAll();

    Voucher create(Voucher req);

    Voucher update(Voucher req);

    void delete(Long id);
}
