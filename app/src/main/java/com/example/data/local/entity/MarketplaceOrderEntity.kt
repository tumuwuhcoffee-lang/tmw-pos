package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "marketplace_orders")
data class MarketplaceOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val marketplace: String, // TOKOPEDIA, SHOPEE, BUKALAPAK
    val orderNumber: String,
    val buyerName: String,
    val orderDate: Long,
    val itemsSummary: String,
    val totalPrice: Long,
    val ppnAmount: Long = 0L,
    val pphAmount: Long = 0L,
    val status: String = "COMPLETED", // PENDING, PROCESSED, COMPLETED, SYNCED
    val isEFakturIntegrated: Boolean = true
)
