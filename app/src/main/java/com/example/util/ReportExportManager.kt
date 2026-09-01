package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity
import com.example.data.model.FinancialStatementSummary
import com.example.data.model.TaxReportSummary
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportExportManager {

    private const val FILE_PROVIDER_SUFFIX = ".fileprovider"

    /**
     * Save HTML Dashboard file locally and return the File object.
     */
    fun saveDashboardHtmlFile(context: Context, htmlContent: String): File? {
        return try {
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(exportDir, "tumuwuh_dashboard_live.html")
            FileOutputStream(file).use { out ->
                out.write(htmlContent.toByteArray(Charsets.UTF_8))
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Open the interactive HTML dashboard directly in Google Chrome / default external browser using FileProvider.
     * This renders 100% of live local database transactions with complete metrics, charts, tables, and search.
     */
    fun openDashboardInBrowser(context: Context, htmlContent: String) {
        try {
            val file = saveDashboardHtmlFile(context, htmlContent)
            if (file == null || !file.exists()) {
                Toast.makeText(context, "Gagal membuat file dashboard", Toast.LENGTH_SHORT).show()
                return
            }

            val authority = "${context.packageName}$FILE_PROVIDER_SUFFIX"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            // Try opening in Google Chrome directly
            val chromeIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "text/html")
                setPackage("com.android.chrome")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            try {
                context.grantUriPermission("com.android.chrome", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(chromeIntent)
                Toast.makeText(context, "Membuka Dashboard Real-Time di Google Chrome...", Toast.LENGTH_SHORT).show()
            } catch (chromeEx: Exception) {
                // Fallback to chooser / default browser
                val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "text/html")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                val chooser = Intent.createChooser(fallbackIntent, "Buka Dashboard Website di Browser").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(chooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal membuka di browser: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Share HTML Dashboard file to WhatsApp, Telegram, Email, Drive, etc.
     */
    fun shareDashboardHtmlFile(context: Context, htmlContent: String) {
        try {
            val file = saveDashboardHtmlFile(context, htmlContent)
            if (file == null || !file.exists()) {
                Toast.makeText(context, "Gagal menyiapkan file dashboard", Toast.LENGTH_SHORT).show()
                return
            }

            val authority = "${context.packageName}$FILE_PROVIDER_SUFFIX"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/html"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Laporan Dashboard Web Tumuwuh POS")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Berikut terlampir file Website Dashboard Interaktif Tumuwuh POS (Penjualan Ril, Arus Kas & Laba Rugi). File dapat dibuka langsung di Google Chrome / Browser HP & Komputer."
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Bagikan File Dashboard HTML").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal membagikan file: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Export all transaction data to CSV
     */
    fun exportTransactionsCsv(
        context: Context,
        transactions: List<TransactionEntity>,
        transactionItems: List<TransactionItemEntity>
    ): File? {
        return try {
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(exportDir, "tumuwuh_laporan_transaksi.csv")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))

            val itemsByTrx = transactionItems.groupBy { it.transactionId }

            FileOutputStream(file).use { out ->
                // UTF-8 BOM for Microsoft Excel compatibility
                out.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
                val writer = out.bufferedWriter(Charsets.UTF_8)

                writer.write("ID Transaksi,No Struk,Waktu,Status,Pelanggan,Meja/Ref,Tipe Order,Total Belanja,Diskon,Pajak,Total Bayar,HPP,Laba Kotor,Metode Bayar,Detail Menu / Item\n")

                transactions.forEach { trx ->
                    val items = itemsByTrx[trx.id].orEmpty()
                    val itemDetails = items.joinToString(" | ") { "${it.productName} (${it.quantity}x @${it.unitPrice.toLong()})" }
                        .replace("\"", "\"\"")

                    val custName = trx.customerName?.ifBlank { "-" } ?: "-"
                    val tableRef = trx.tableOrOrderRef?.ifBlank { "-" } ?: "-"

                    val line = listOf(
                        trx.id.toString(),
                        "\"${trx.invoiceNumber}\"",
                        "\"${dateFormat.format(Date(trx.timestamp))}\"",
                        "\"${trx.paymentStatus}\"",
                        "\"$custName\"",
                        "\"$tableRef\"",
                        "\"${trx.orderType}\"",
                        trx.subtotal.toLong().toString(),
                        trx.discountAmount.toLong().toString(),
                        trx.taxAmount.toLong().toString(),
                        trx.totalAmount.toLong().toString(),
                        trx.totalCostPrice.toLong().toString(),
                        trx.grossProfit.toLong().toString(),
                        "\"${trx.paymentMethod}\"",
                        "\"$itemDetails\""
                    ).joinToString(",")

                    writer.write("$line\n")
                }
                writer.flush()
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Export Cashflow records to CSV
     */
    fun exportCashflowCsv(
        context: Context,
        cashflows: List<CashflowEntity>
    ): File? {
        return try {
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(exportDir, "tumuwuh_laporan_arus_kas.csv")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id", "ID"))

            FileOutputStream(file).use { out ->
                out.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
                val writer = out.bufferedWriter(Charsets.UTF_8)

                writer.write("ID,Waktu,Tipe (Masuk/Keluar),Kategori,Unit Bisnis,Keterangan,Metode Pembayaran,Nominal (Rp)\n")

                cashflows.forEach { cf ->
                    val typeStr = if (cf.type == "DEBIT") "PEMASUKAN (DEBIT)" else "PENGELUARAN (KREDIT)"
                    val line = listOf(
                        cf.id.toString(),
                        "\"${dateFormat.format(Date(cf.timestamp))}\"",
                        "\"$typeStr\"",
                        "\"${cf.category}\"",
                        "\"${cf.businessUnit}\"",
                        "\"${cf.description.replace("\"", "\"\"")}\"",
                        "\"${cf.paymentMethod}\"",
                        cf.amount.toLong().toString()
                    ).joinToString(",")

                    writer.write("$line\n")
                }
                writer.flush()
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Export comprehensive financial statement to CSV
     */
    fun exportFinancialStatementCsv(
        context: Context,
        financialSummary: FinancialStatementSummary,
        taxSummary: TaxReportSummary
    ): File? {
        return try {
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val file = File(exportDir, "tumuwuh_laporan_laba_rugi.csv")
            val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))

            FileOutputStream(file).use { out ->
                out.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
                val writer = out.bufferedWriter(Charsets.UTF_8)

                writer.write("LAPORAN LABA RUGI & KEUANGAN - TUMUWUH POS\n")
                writer.write("Tanggal Dibuat,${dateFormat.format(Date())}\n\n")

                writer.write("KOMPONEN KEUANGAN,NOMINAL (RP)\n")
                writer.write("Pendapatan Kotor (Gross Revenue),${financialSummary.totalRevenue.toLong()}\n")
                writer.write("Beban Pokok Penjualan (HPP),-${financialSummary.totalHpp.toLong()}\n")
                writer.write("Laba Kotor (Gross Profit),${financialSummary.grossProfit.toLong()}\n")
                writer.write("Total Beban Operasional,-${financialSummary.totalOperationalExpenses.toLong()}\n")
                writer.write("Laba Bersih (Net Profit),${financialSummary.netProfit.toLong()}\n\n")

                writer.write("ESTIMASI KEWAJIBAN PERPAJAKAN,NOMINAL (RP)\n")
                writer.write("Total PPN 11%,${taxSummary.totalPpn.toLong()}\n")
                writer.write("Total PPh Final UMKM 0.5%,${taxSummary.totalPphFinal.toLong()}\n")
                writer.flush()
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Share any generated CSV / Excel file
     */
    fun shareExportedFile(context: Context, file: File, title: String, subject: String) {
        try {
            if (!file.exists()) {
                Toast.makeText(context, "File tidak ditemukan untuk dibagikan", Toast.LENGTH_SHORT).show()
                return
            }

            val authority = "${context.packageName}$FILE_PROVIDER_SUFFIX"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Berikut terlampir file $subject dari Tumuwuh POS (Dapat dibuka di Microsoft Excel, Google Sheets, & WPS Office)."
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, title).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal membagikan file: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
