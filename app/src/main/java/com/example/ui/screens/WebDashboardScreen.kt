package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.CloudSyncManager
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CoffeeBrown
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.ui.viewmodel.PosViewModel
import com.example.util.ReportExportManager
import com.example.util.WebDashboardHtmlGenerator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebDashboardScreen(
    viewModel: PosViewModel,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val transactions by viewModel.allTransactions.collectAsState()
    val transactionItems by viewModel.allTransactionItems.collectAsState()
    val cashflows by viewModel.allCashflows.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val financialSummary by viewModel.financialStatement.collectAsState()
    val taxSummary by viewModel.taxReportSummary.collectAsState()
    val cloudStatus by viewModel.cloudSyncStatus.collectAsState()

    var showServerSettingsDialog by remember { mutableStateOf(false) }
    var showExportSheet by remember { mutableStateOf(false) }

    val paidTransactions = remember(transactions) {
        transactions.filter { it.paymentStatus == "PAID" }
    }
    val totalGrossRevenue = remember(paidTransactions) {
        paidTransactions.sumOf { it.totalAmount }
    }
    val totalBarRev = remember(paidTransactions) {
        paidTransactions.sumOf { it.barRevenue }
    }
    val totalBilliardRev = remember(paidTransactions) {
        paidTransactions.sumOf { it.billiardRevenue }
    }
    val totalGorRev = remember(paidTransactions) {
        paidTransactions.sumOf { it.gorRevenue }
    }

    val htmlContent = remember(transactions, transactionItems, cashflows, products, financialSummary, taxSummary) {
        WebDashboardHtmlGenerator.generateDashboardHtml(
            transactions = transactions,
            transactionItems = transactionItems,
            cashflows = cashflows,
            products = products,
            financialSummary = financialSummary,
            taxSummary = taxSummary
        )
    }

    Scaffold(
        topBar = {
            Surface(
                color = Slate900,
                shadowElevation = 4.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (onBack != null) {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali",
                                        tint = White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(GoldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Web",
                                    tint = CoffeeBrown,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Website Daring & Server",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = White
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(if (cloudStatus.isOnline) EmeraldGreen else CrimsonRed)
                                    )
                                }
                                Text(
                                    text = if (cloudStatus.isOnline) "Terhubung: Vercel Cloud Server" else "Mode Offline (${cloudStatus.pendingOfflineRecords} Pending)",
                                    fontSize = 11.sp,
                                    color = if (cloudStatus.isOnline) EmeraldGreenLight else Slate400
                                )
                            }
                        }

                        // Action Buttons on Top Bar
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. Quick Sync Button
                            IconButton(
                                onClick = {
                                    viewModel.syncOnlineCloudDatabase()
                                    Toast.makeText(context, "Sinkronisasi ke Server Vercel Dimulai...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("refresh_web_dashboard_button")
                            ) {
                                if (cloudStatus.isSyncing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = GoldAccent,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Sinkronkan",
                                        tint = GoldAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            // 2. Export Sheet Dialog
                            IconButton(
                                onClick = { showExportSheet = true },
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("export_files_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Unduh File",
                                    tint = EmeraldGreenLight,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            // 3. Server Settings Dialog
                            IconButton(
                                onClick = { showServerSettingsDialog = true },
                                modifier = Modifier
                                    .size(44.dp)
                                    .testTag("server_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Pengaturan Server",
                                    tint = Slate300,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    // Status Bar Subtitle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate800)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐 ${cloudStatus.webDashboardUrl}",
                            fontSize = 10.sp,
                            color = Slate200,
                            maxLines = 1
                        )
                        Text(
                            text = "Outlet: ${cloudStatus.outletId}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate50),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO CARD: GOOGLE CHROME DIRECT WEBSITE ACCESS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate900),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = EmeraldGreenLight,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Website Daring Tumuwuh (Vercel)",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                                Text(
                                    text = "Pantau penjualan real-time tanpa membuka aplikasi POS",
                                    fontSize = 11.sp,
                                    color = Slate300
                                )
                            }
                        }

                        // Web Link Box
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Slate800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "URL SERVER DARING:",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldAccent
                                    )
                                    Text(
                                        text = cloudStatus.webDashboardUrl,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = White,
                                        maxLines = 1
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldGreen.copy(alpha = 0.25f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "LIVE HOSTING",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldGreenLight
                                    )
                                }
                            }
                        }

                        // BIG ACTION BUTTON: BUKA GOOGLE CHROME DENGAN DATA RIL
                        Button(
                            onClick = {
                                viewModel.openWebDashboard(context)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("open_external_chrome_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🚀 Buka Dashboard Web di Google Chrome",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // ROW OF 2 ACTION BUTTONS: BUKA LINK VERCEL & SINKRONKAN DATA
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // BUTTON: BUKA LINK HOSTING VERCEL
                            OutlinedButton(
                                onClick = {
                                    Toast.makeText(context, "Membuka URL Vercel: ${cloudStatus.webDashboardUrl}", Toast.LENGTH_SHORT).show()
                                    CloudSyncManager.openWebDashboard(context)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("open_vercel_url_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate600)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Slate300
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "URL Vercel",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                            }

                            // BUTTON: SINKRONKAN DATA SEKARANG
                            Button(
                                onClick = {
                                    viewModel.syncOnlineCloudDatabase()
                                    Toast.makeText(context, "Mengirim seluruh data POS ke server Vercel...", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(46.dp)
                                    .testTag("sync_now_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AmberOrange)
                            ) {
                                if (cloudStatus.isSyncing) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Slate900, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Mengirim...", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Slate900
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "⚡ Sync Cloud",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SYNC STATUS & SERVER METRICS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Status Sinkronisasi Server",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (cloudStatus.isOnline) EmeraldGreen.copy(alpha = 0.15f) else CrimsonRed.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (cloudStatus.isOnline) "TERHUBUNG LIVE" else "OFFLINE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (cloudStatus.isOnline) EmeraldGreenDark else CrimsonRed
                                )
                            }
                        }

                        HorizontalDivider(color = Slate100)

                        // 4 Mini Metric Badges
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Total Transaksi Terkirim
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = Slate50,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Data Terkirim", fontSize = 10.sp, color = Slate500)
                                    Text(
                                        text = "${cloudStatus.totalRecordsSynced + transactions.size}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900
                                    )
                                }
                            }

                            // Antrean Pending
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = Slate50,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Antrean Offline", fontSize = 10.sp, color = Slate500)
                                    Text(
                                        text = "${cloudStatus.pendingOfflineRecords}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (cloudStatus.pendingOfflineRecords > 0) GoldAccent else EmeraldGreenDark
                                    )
                                }
                            }

                            // Auto Sync
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = Slate50,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Auto-Sync", fontSize = 10.sp, color = Slate500)
                                    Text(
                                        text = if (cloudStatus.autoSyncOnTransaction) "AKTIF" else "NONAKTIF",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (cloudStatus.autoSyncOnTransaction) PrimaryBlue else Slate600
                                    )
                                }
                            }
                        }

                        // Last Sync Message Box
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate100
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timeline,
                                    contentDescription = null,
                                    tint = Slate600,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = cloudStatus.lastSyncMessage,
                                    fontSize = 11.sp,
                                    color = Slate700,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // DATA SUMMARY CARDS FOR REMOTE MONITORING
            item {
                Text(
                    text = "📊 Ringkasan Penjualan Siap Daring",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Omzet Bar
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(CoffeeBrown)
                                )
                                Text("Omzet Bar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Rp ${String.format(Locale("id", "ID"), "%,d", totalBarRev.toLong())}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoffeeBrown
                            )
                        }
                    }

                    // Omzet Billiard
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlue)
                                )
                                Text("Omzet Billiard", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Rp ${String.format(Locale("id", "ID"), "%,d", totalBilliardRev.toLong())}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                    }

                    // Omzet GOR
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldGreen)
                                )
                                Text("Omzet GOR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Rp ${String.format(Locale("id", "ID"), "%,d", totalGorRev.toLong())}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreenDark
                            )
                        }
                    }
                }
            }

            // REAL-TIME SYNC LOGS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📋 Riwayat Sinkronisasi Vercel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "${cloudStatus.syncLogs.size} Log",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }
            }

            if (cloudStatus.syncLogs.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = Slate400,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "Belum ada riwayat pengiriman data",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate700
                            )
                            Text(
                                text = "Tekan tombol 'Sinkronkan Seluruh Data Sekarang' untuk mengirim data ke website https://website1-henna-ten.vercel.app/",
                                fontSize = 11.sp,
                                color = Slate500,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(cloudStatus.syncLogs.take(10)) { log ->
                    val dateFormat = SimpleDateFormat("dd/MM HH:mm:ss", Locale("id", "ID"))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (log.status == "SUCCESS") EmeraldGreen.copy(alpha = 0.15f) else GoldAccent.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (log.status == "SUCCESS") Icons.Default.CheckCircle else Icons.Default.Sync,
                                        contentDescription = null,
                                        tint = if (log.status == "SUCCESS") EmeraldGreenDark else CoffeeBrown,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(log.event, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                        Text(
                                            text = if (log.status == "SUCCESS") "HTTP ${log.httpCode} OK" else log.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (log.status == "SUCCESS") EmeraldGreenDark else Slate600
                                        )
                                    }
                                    Text(log.details, fontSize = 10.sp, color = Slate600)
                                }
                            }

                            Text(
                                text = dateFormat.format(Date(log.timestamp)),
                                fontSize = 9.sp,
                                color = Slate400
                            )
                        }
                    }
                }
            }

            // BOTTOM EXPORT & QUICK DOWNLOAD OPTIONS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Ekspor CSV
                    OutlinedButton(
                        onClick = {
                            val file = ReportExportManager.exportTransactionsCsv(context, transactions, transactionItems)
                            if (file != null) {
                                ReportExportManager.shareExportedFile(
                                    context,
                                    file,
                                    "Laporan Penjualan CSV",
                                    "Data Penjualan CSV Tumuwuh POS"
                                )
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate800),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate300)
                    ) {
                        Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp), tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ekspor CSV", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    // Unduh HTML Backup
                    OutlinedButton(
                        onClick = {
                            ReportExportManager.shareDashboardHtmlFile(context, htmlContent)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Slate800),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate300)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldGreenDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unduh HTML", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // DIALOG: DOWNLOAD / EXPORT FILES
    if (showExportSheet) {
        AlertDialog(
            onDismissRequest = { showExportSheet = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, tint = GoldAccent)
                    Text("Unduh & Ekspor Laporan Web", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Pilih format file laporan yang ingin Anda unduh atau bagikan ke WhatsApp, Email, atau Google Drive:",
                        fontSize = 12.sp,
                        color = Slate700
                    )

                    // Option 1: HTML Standalone
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showExportSheet = false
                                ReportExportManager.shareDashboardHtmlFile(context, htmlContent)
                            },
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(PrimaryBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Language, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("File Dashboard HTML Mandiri (.html)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                Text("Dapat dibuka langsung di browser HP/Laptop tanpa internet", fontSize = 10.sp, color = Slate500)
                            }
                        }
                    }

                    // Option 2: Transactions CSV
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showExportSheet = false
                                val file = ReportExportManager.exportTransactionsCsv(context, transactions, transactionItems)
                                if (file != null) {
                                    ReportExportManager.shareExportedFile(context, file, "Laporan Transaksi CSV", "Laporan Transaksi Kasir POS")
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(EmeraldGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TableChart, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Laporan Penjualan Excel / CSV (.csv)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                Text("${transactions.size} transaksi lengkap dengan rincian item, pajak, diskon", fontSize = 10.sp, color = Slate500)
                            }
                        }
                    }

                    // Option 3: Cashflow CSV
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showExportSheet = false
                                val file = ReportExportManager.exportCashflowCsv(context, cashflows)
                                if (file != null) {
                                    ReportExportManager.shareExportedFile(context, file, "Laporan Arus Kas CSV", "Laporan Arus Kas Kasir POS")
                                }
                            },
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(GoldAccent, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = CoffeeBrown, modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Buku Arus Kas & Petty Cash (.csv)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Slate900)
                                Text("${cashflows.size} catatan kas masuk & keluar", fontSize = 10.sp, color = Slate500)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportSheet = false }) {
                    Text("Tutup")
                }
            }
        )
    }

    // DIALOG: SERVER & WEB HOSTING CONFIGURATION
    if (showServerSettingsDialog) {
        var endpointInput by remember { mutableStateOf(cloudStatus.serverEndpoint) }
        var webUrlInput by remember { mutableStateOf(cloudStatus.webDashboardUrl) }
        var apiKeyInput by remember { mutableStateOf(cloudStatus.apiKey) }
        var outletInput by remember { mutableStateOf(cloudStatus.outletId) }
        var terminalInput by remember { mutableStateOf(cloudStatus.terminalId) }
        var autoSyncInput by remember { mutableStateOf(cloudStatus.autoSyncOnTransaction) }

        AlertDialog(
            onDismissRequest = { showServerSettingsDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Dns, contentDescription = null, tint = GoldAccent)
                    Text("Konfigurasi Server & Web Hosting", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Konfigurasikan alamat server backend / hosting Vercel dan autentikasi token untuk sinkronisasi otomatis POS Tumuwuh:",
                            fontSize = 11.sp,
                            color = Slate700
                        )
                    }

                    // Auto Sync Switch
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Slate100),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Auto-Sync saat Transaksi Baru", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text("Kirim data otomatis saat kasir online; tunda jika offline", fontSize = 9.sp, color = Slate500)
                                }
                                Switch(
                                    checked = autoSyncInput,
                                    onCheckedChange = { autoSyncInput = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldGreen)
                                )
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = endpointInput,
                            onValueChange = { endpointInput = it },
                            label = { Text("API Sync Endpoint (REST HTTPS)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = webUrlInput,
                            onValueChange = { webUrlInput = it },
                            label = { Text("Alamat Web Portal Dashboard", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = apiKeyInput,
                            onValueChange = { apiKeyInput = it },
                            label = { Text("API Key / Bearer Auth Token", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = outletInput,
                                onValueChange = { outletInput = it },
                                label = { Text("ID Outlet", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = terminalInput,
                                onValueChange = { terminalInput = it },
                                label = { Text("ID Terminal POS", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // API Payload Structure Preview
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Slate900),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "📄 Struktur REST API Endpoint (JSON):",
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = """POST ${endpointInput.ifBlank { "/api/sync" }}
Headers: 
  Authorization: Bearer $apiKeyInput
  Content-Type: application/json
Payload:
{
  "outletId": "$outletInput",
  "terminalId": "$terminalInput",
  "syncTimestamp": ${System.currentTimeMillis()},
  "summary": { ... },
  "transactions": [ ... ],
  "products": [ ... ],
  "cashflows": [ ... ]
}""",
                                    color = Slate200,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        CloudSyncManager.updateServerConfig(
                            serverEndpoint = endpointInput,
                            webDashboardUrl = webUrlInput,
                            apiKey = apiKeyInput,
                            outletId = outletInput,
                            terminalId = terminalInput,
                            autoSync = autoSyncInput
                        )
                        showServerSettingsDialog = false
                        Toast.makeText(context, "Konfigurasi Server Vercel Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text("Simpan Konfigurasi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showServerSettingsDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
