package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entryNumber: String,
    val timestamp: Long,
    val accountCode: String,
    val accountName: String,
    val description: String,
    val debit: Long = 0L,
    val credit: Long = 0L,
    val unitCategory: String = "ALL", // BAR, BILLIARD, GOR, UMUM
    val referenceId: String? = null,
    val authorizedBy: String = "Owner",
    val isSyncedCloud: Boolean = true
)
