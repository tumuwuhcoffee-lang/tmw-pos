package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String, // "BAR", "BILLIARD", "GOR"
    val subCategory: String = "",
    val price: Double,
    val costPrice: Double = 0.0, // HPP
    val stock: Int = 0,
    val lowStockThreshold: Int = 5,
    val unit: String = "Pcs", // "Cup", "Jam", "Porsi", "Botol", "Pcs"
    val sku: String = "",
    val isAvailable: Boolean = true
)
