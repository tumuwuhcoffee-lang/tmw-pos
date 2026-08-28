package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.SupplierOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierOrderDao {
    @Query("SELECT * FROM supplier_orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<SupplierOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: SupplierOrderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<SupplierOrderEntity>)

    @Update
    suspend fun updateOrder(order: SupplierOrderEntity)

    @Query("DELETE FROM supplier_orders WHERE id = :id")
    suspend fun deleteOrder(id: Long)
}
