package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val invoiceNumber: String,
    val shiftId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val customerName: String? = null,
    val tableOrOrderRef: String? = null, // e.g. "Meja 4" / "Lap 2" / "Takeaway #12"
    val orderType: String = "DINE_IN", // "DINE_IN", "TAKEAWAY", "BOOKING"
    val paymentMethod: String = "CASH", // "CASH", "QRIS", "DEBIT"
    val paymentStatus: String = "PAID", // "PAID", "HELD" (Bayar Nanti), "CANCELLED" (Void)
    val subtotal: Double = 0.0,
    val taxAmount: Double = 0.0,
    val discountAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val totalCostPrice: Double = 0.0, // Total HPP
    val grossProfit: Double = 0.0, // totalAmount - totalCostPrice
    val cashTendered: Double = 0.0,
    val changeAmount: Double = 0.0,
    val barRevenue: Double = 0.0,
    val billiardRevenue: Double = 0.0,
    val gorRevenue: Double = 0.0,
    val cancelReason: String? = null,
    val cancelledAt: Long? = null
)
