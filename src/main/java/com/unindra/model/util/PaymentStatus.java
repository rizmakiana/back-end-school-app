package com.unindra.model.util;

public enum PaymentStatus {

    PENDING,    // sudah ada tagihan, belum dibayar
    PAID,       // sudah dibayar & diverifikasi
    FAILED,     // pembayaran gagal (contoh: transfer ditolak)
    CANCELLED,  // dibatalkan (oleh siswa/staff)
    EXPIRED     // tagihan kadaluarsa (contoh QRIS timeout)

}