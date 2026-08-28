package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val shiftNumber: Int = 1,
    val cashierName: String = "Kasir Utama",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val status: String = "OPEN", // "OPEN", "CLOSED"
    val initialCash: Double = 0.0, // Total Modal Awal
    val initialBarCash: Double = 0.0,
    val initialBilliardCash: Double = 0.0,
    val initialGorCash: Double = 0.0,
    val closingCashActual: Double? = null,
    val closingCashExpected: Double? = null,
    val cashDifference: Double? = null, // Selisih kas fisik vs sistem
    val notes: String? = null
)
