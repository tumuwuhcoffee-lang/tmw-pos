package com.example.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatUtils {
    private val localeId = Locale("id", "ID")
    private val rupiahFormat = NumberFormat.getCurrencyInstance(localeId).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    fun formatRupiah(amount: Double): String {
        return try {
            val formatted = rupiahFormat.format(amount)
            // Replace "Rp" with "Rp " if needed
            if (formatted.startsWith("Rp") && !formatted.startsWith("Rp ")) {
                "Rp " + formatted.substring(2)
            } else {
                formatted
            }
        } catch (e: Exception) {
            "Rp ${amount.toLong()}"
        }
    }

    fun formatCompactRupiah(amount: Double): String {
        val absAmount = Math.abs(amount)
        val sign = if (amount < 0) "-" else ""
        return when {
            absAmount >= 1_000_000_000 -> "${sign}Rp ${String.format(localeId, "%.1fB", absAmount / 1_000_000_000)}"
            absAmount >= 1_000_000 -> "${sign}Rp ${String.format(localeId, "%.1fM", absAmount / 1_000_000)}"
            absAmount >= 1_000 -> "${sign}Rp ${String.format(localeId, "%.0fk", absAmount / 1_000)}"
            else -> "${sign}Rp ${absAmount.toLong()}"
        }
    }

    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", localeId)
        return sdf.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", localeId)
        return sdf.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", localeId)
        return sdf.format(Date(timestamp))
    }

    fun formatShortDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", localeId)
        return sdf.format(Date(timestamp))
    }
}
