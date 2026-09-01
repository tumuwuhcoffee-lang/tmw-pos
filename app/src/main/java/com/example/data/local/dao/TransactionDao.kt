package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE paymentStatus = 'PAID' ORDER BY timestamp DESC")
    fun getPaidTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE paymentStatus = 'HELD' ORDER BY timestamp DESC")
    fun getHeldTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE shiftId = :shiftId ORDER BY timestamp DESC")
    fun getTransactionsByShift(shiftId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE shiftId = :shiftId AND paymentStatus = 'CANCELLED' ORDER BY timestamp DESC")
    fun getCancelledTransactionsByShift(shiftId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE timestamp >= :startTime AND timestamp <= :endTime AND paymentStatus = 'PAID' ORDER BY timestamp DESC")
    fun getTransactionsByDateRange(startTime: Long, endTime: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}
