package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_in_logs")
data class StockInLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: Long,
    val productName: String,
    val category: String, // "BAR", "BILLIARD", "GOR"
    val timestamp: Long = System.currentTimeMillis(),
    val supplierName: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalCost: Double,
    val paymentSource: String = "KAS_LACI", // "KAS_LACI", "BANK_TRANSFER"
    val notes: String? = null
)
