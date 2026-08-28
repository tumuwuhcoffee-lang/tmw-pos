package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CashflowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CashflowDao {
    @Query("SELECT * FROM cashflow ORDER BY timestamp DESC")
    fun getAllCashflows(): Flow<List<CashflowEntity>>

    @Query("SELECT * FROM cashflow WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getCashflowsByDateRange(startTime: Long, endTime: Long): Flow<List<CashflowEntity>>

    @Query("SELECT * FROM cashflow WHERE type = :type ORDER BY timestamp DESC")
    fun getCashflowsByType(type: String): Flow<List<CashflowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCashflow(cashflow: CashflowEntity): Long
}
