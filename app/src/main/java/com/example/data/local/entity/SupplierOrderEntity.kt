package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier_orders")
data class SupplierOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val poNumber: String,
    val supplierName: String,
    val supplierEmail: String,
    val supplierPhone: String = "",
    val category: String = "BAR", // BAR (Kopi/Susu), BILLIARD (Bola/Tip), GOR (Shuttlecock/Net), UMUM
    val orderDate: Long,
    val dueDate: Long,
    val itemsSummary: String,
    val totalAmount: Long,
    val status: String = "SENT", // DRAFT, SENT, RECEIVED, PAID
    val notes: String = "",
    val isSyncedCloud: Boolean = true
)
