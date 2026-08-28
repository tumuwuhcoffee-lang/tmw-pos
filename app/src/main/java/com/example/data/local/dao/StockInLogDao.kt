package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.StockInLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockInLogDao {
    @Query("SELECT * FROM stock_in_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<StockInLogEntity>>

    @Query("SELECT * FROM stock_in_logs WHERE productId = :productId ORDER BY timestamp DESC")
    fun getLogsByProduct(productId: Long): Flow<List<StockInLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: StockInLogEntity): Long
}
