package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cashflow")
data class CashflowEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // "DEBIT" (Uang Masuk), "KREDIT" (Uang Keluar)
    val category: String, // "PENJUALAN", "MODAL_AWAL", "BELANJA_STOK", "OPERASIONAL", "GAJI", "MAINTENANCE", "LAINNYA"
    val businessUnit: String = "UMUM", // "BAR", "BILLIARD", "GOR", "UMUM"
    val amount: Double,
    val paymentMethod: String = "CASH", // "CASH", "QRIS", "BANK"
    val description: String,
    val referenceId: String? = null,
    val shiftId: Long? = null
)
