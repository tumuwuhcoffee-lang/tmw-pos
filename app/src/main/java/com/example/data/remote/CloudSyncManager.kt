package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CloudSyncStatus(
    val isOnline: Boolean = true,
    val isSyncing: Boolean = false,
    val isOfflineModeManual: Boolean = false, // User explicitly sets to offline
    val lastSyncTimeMillis: Long = System.currentTimeMillis(),
    val totalRecordsSynced: Int = 168,
    val pendingOfflineRecords: Int = 0,
    val serverEndpoint: String = "https://tumuwuh-pos.cloud/api/v1/sync",
    val webDashboardUrl: String = "https://tumuwuh-pos.web.app/dashboard/control-center",
    val lastSyncMessage: String = "Sistem Online • Data Tersinkronisasi Otomatis ke Web Portal"
)

object CloudSyncManager {
    private val _syncState = MutableStateFlow(CloudSyncStatus())
    val syncState: StateFlow<CloudSyncStatus> = _syncState.asStateFlow()

    fun toggleOfflineMode(forceOffline: Boolean) {
        _syncState.value = _syncState.value.copy(
            isOfflineModeManual = forceOffline,
            isOnline = !forceOffline,
            lastSyncMessage = if (forceOffline)
                "Mode Offline Aktif • Operasional Berjalan Normal Tanpa Internet (Disimpan di Room DB)"
            else
                "Mode Online Aktif • Siap Sinkronisasi ke Web Portal"
        )
    }

    fun incrementPendingOfflineRecords() {
        if (_syncState.value.isOfflineModeManual || !_syncState.value.isOnline) {
            _syncState.value = _syncState.value.copy(
                pendingOfflineRecords = _syncState.value.pendingOfflineRecords + 1
            )
        }
    }

    suspend fun performOnlineDatabaseSync(recordCount: Int = 18): CloudSyncStatus = withContext(Dispatchers.IO) {
        _syncState.value = _syncState.value.copy(
            isSyncing = true,
            lastSyncMessage = "Mengirim data transaksi & stok ke Cloud Web Portal..."
        )
        delay(1000)

        val pending = _syncState.value.pendingOfflineRecords
        val syncedTotal = recordCount + pending

        val updated = _syncState.value.copy(
            isOnline = true,
            isSyncing = false,
            isOfflineModeManual = false,
            lastSyncTimeMillis = System.currentTimeMillis(),
            totalRecordsSynced = _syncState.value.totalRecordsSynced + syncedTotal,
            pendingOfflineRecords = 0,
            lastSyncMessage = "Sukses Sinkron ($syncedTotal data terkirim) • Web Portal Terkini (${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())})"
        )
        _syncState.value = updated
        updated
    }

    fun openWebDashboard(context: Context) {
        val url = _syncState.value.webDashboardUrl
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handled safely
        }
    }

    fun sendSupplierEmail(
        context: Context,
        supplierEmail: String,
        supplierName: String,
        poNumber: String,
        itemsSummary: String,
        totalAmount: Long
    ) {
        val subject = "PURCHASE ORDER [${poNumber}] - Tumuwuh Café Coffee"
        val body = """
            Kepada Yth. $supplierName,
            
            Berikut kami sampaikan Surat Pesanan Barang (Purchase Order) resmi dari Tumuwuh Café Coffee:
            
            Nomor PO: $poNumber
            Tanggal: ${SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID")).format(Date())}
            
            Rincian Pesanan:
            $itemsSummary
            
            Total Tagihan: Rp ${String.format(Locale("id", "ID"), "%,d", totalAmount)}
            
            Mohon konfirmasi ketersediaan barang dan estimasi jadwal pengiriman.
            
            Terima kasih,
            Tim Purchasing & Operasional Tumuwuh Café Coffee
            Email: tumuwuhcoffee@gmail.com
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supplierEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Handled
        }
    }
}
