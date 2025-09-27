package com.unindra.model.util;

public enum PaymentMethod {
    
    CASH,       // bayar langsung di sekolah
    TRANSFER,   // via bank transfer (manual/VA)
    QRIS,       // scan QRIS
    DEBIT,      // kartu debit
    CREDIT,     // kartu kredit
    OTHER       // jaga-jaga kalau ada metode lain

}