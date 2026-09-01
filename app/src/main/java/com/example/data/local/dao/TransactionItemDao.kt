package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.TransactionItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionItemDao {
    @Query("SELECT * FROM transaction_items WHERE transactionId = :transactionId")
    fun getItemsForTransaction(transactionId: Long): Flow<List<TransactionItemEntity>>

    @Query("SELECT * FROM transaction_items WHERE transactionId = :transactionId")
    suspend fun getItemsForTransactionOnce(transactionId: Long): List<TransactionItemEntity>

    @Query("SELECT * FROM transaction_items")
    fun getAllTransactionItems(): Flow<List<TransactionItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<TransactionItemEntity>)

    @Query("DELETE FROM transaction_items WHERE transactionId = :transactionId")
    suspend fun deleteItemsForTransaction(transactionId: Long)

    @Query("DELETE FROM transaction_items")
    suspend fun deleteAllTransactionItems()
}
