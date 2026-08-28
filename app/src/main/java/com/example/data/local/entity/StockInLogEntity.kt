package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_in_logs")
data class StockInLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long? = null, // null if custom raw material not tied to menu
    val productName: String,
    val category: String, // "BAR", "BILLIARD", "GOR", "OPERASIONAL"
    val timestamp: Long = System.currentTimeMillis(),
    val supplierName: String,
    val quantity: Int,
    val unit: String = "Pcs", // "kg", "liter", "pack", "dus", "pcs", "ikat", "botol", "kaleng"
    val unitPrice: Double,
    val totalCost: Double,
    val paymentSource: String = "KAS_LACI", // "KAS_LACI", "BANK_TRANSFER"
    val notes: String? = null,
    val batchNumber: String = ""
)
