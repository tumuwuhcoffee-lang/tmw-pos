package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CashflowDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.ShiftDao
import com.example.data.local.dao.StockInLogDao
import com.example.data.local.dao.TransactionDao
import com.example.data.local.dao.TransactionItemDao
import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ShiftEntity
import com.example.data.local.entity.StockInLogEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity

@Database(
    entities = [
        ProductEntity::class,
        ShiftEntity::class,
        TransactionEntity::class,
        TransactionItemEntity::class,
        StockInLogEntity::class,
        CashflowEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun shiftDao(): ShiftDao
    abstract fun transactionDao(): TransactionDao
    abstract fun transactionItemDao(): TransactionItemDao
    abstract fun stockInLogDao(): StockInLogDao
    abstract fun cashflowDao(): CashflowDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pos_kasir_hub.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
