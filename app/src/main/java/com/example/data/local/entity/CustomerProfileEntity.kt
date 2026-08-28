package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer_profiles")
data class CustomerProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customerName: String,
    val phone: String,
    val email: String = "",
    val tier: String = "REGULAR", // REGULAR, SILVER, GOLD, VIP
    val loyaltyPoints: Int = 0,
    val favoriteCategory: String = "BAR",
    val favoriteItem: String = "Kopi Susu Gula Aren",
    val totalSpent: Long = 0L,
    val visitCount: Int = 1,
    val lastVisitDate: Long = System.currentTimeMillis(),
    val activeCoupons: String = "NGOPI10K, TUMUWUH_WEEKEND",
    val isSyncedCloud: Boolean = true
)
