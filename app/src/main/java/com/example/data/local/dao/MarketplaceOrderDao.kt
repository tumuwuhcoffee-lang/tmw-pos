package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.MarketplaceOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketplaceOrderDao {
    @Query("SELECT * FROM marketplace_orders ORDER BY orderDate DESC")
    fun getAllMarketplaceOrders(): Flow<List<MarketplaceOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: MarketplaceOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<MarketplaceOrderEntity>)
}
