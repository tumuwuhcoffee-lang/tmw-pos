package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CustomerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerProfileDao {
    @Query("SELECT * FROM customer_profiles ORDER BY totalSpent DESC")
    fun getAllCustomers(): Flow<List<CustomerProfileEntity>>

    @Query("SELECT * FROM customer_profiles WHERE phone = :phone LIMIT 1")
    suspend fun getCustomerByPhone(phone: String): CustomerProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerProfileEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerProfileEntity>)

    @Update
    suspend fun updateCustomer(customer: CustomerProfileEntity)
}
