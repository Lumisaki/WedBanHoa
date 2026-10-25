package org.example.wedbanhoa.service;

import org.example.wedbanhoa.entity.BankSetting;
import org.example.wedbanhoa.repository.BankSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankSettingService {

    @Autowired
    private BankSettingRepository bankSettingRepository;

    private static final int DEFAULT_ID = 1;

    @Transactional
    public BankSetting getBankSetting() {
        return bankSettingRepository.findById(DEFAULT_ID).orElseGet(() -> {
            BankSetting defaultSetting = BankSetting.builder()
                    .id(DEFAULT_ID)
                    .bankName("TP Bank")
                    .accountNumber("96966678888")
                    .accountHolder("BUI CONG PHUOC")
                    .qrImage("thanhtoan.jpg")
                    .build();
            return bankSettingRepository.save(defaultSetting);
        });
    }

    @Transactional
    public BankSetting updateBankSetting(String bankName, String accountNumber, String accountHolder, String qrImage) {
        BankSetting setting = getBankSetting();
        if (bankName != null && !bankName.trim().isEmpty()) setting.setBankName(bankName.trim());
        if (accountNumber != null && !accountNumber.trim().isEmpty()) setting.setAccountNumber(accountNumber.trim());
        if (accountHolder != null && !accountHolder.trim().isEmpty()) setting.setAccountHolder(accountHolder.trim().toUpperCase());
        if (qrImage != null && !qrImage.trim().isEmpty()) setting.setQrImage(qrImage.trim());
        return bankSettingRepository.save(setting);
    }
}
