package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.local.entity.MarketplaceOrderEntity
import com.example.data.local.entity.SupplierOrderEntity
import com.example.data.model.FinancialStatementSummary
import com.example.data.model.TaxReportSummary
import com.example.data.remote.CloudSyncStatus
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.AmberOrangeLight
import com.example.ui.theme.BilliardPurple
import com.example.ui.theme.BilliardPurpleLight
import com.example.ui.theme.CoffeeBrown
import com.example.ui.theme.CoffeeBrownLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GorNavy
import com.example.ui.theme.GorNavyLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.RoseRed
import com.example.ui.theme.RoseRedLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.ui.viewmodel.PosViewModel
import com.example.util.FormatUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessControlCenterSheet(
    viewModel: PosViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val cloudStatus by viewModel.cloudSyncStatus.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val activeTab by viewModel.controlCenterTab.collectAsState()
    val financialStatement by viewModel.financialStatement.collectAsState()
    val taxSummary by viewModel.taxReportSummary.collectAsState()
    val journalEntries by viewModel.allJournalEntries.collectAsState()
    val supplierOrders by viewModel.allSupplierOrders.collectAsState()
    val customers by viewModel.allCustomers.collectAsState()
    val marketplaceOrders by viewModel.allMarketplaceOrders.collectAsState()

    var showNewJournalDialog by remember { mutableStateOf(false) }
    var showNewPoDialog by remember { mutableStateOf(false) }
    var showNewCustomerDialog by remember { mutableStateOf(false) }
    var showMarketplaceImportDialog by remember { mutableStateOf(false) }
    var showResetConfirmationDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier.fillMaxHeight(0.95f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Sheet Header & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Slate900),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "Pusat Kendali",
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Pusat Kendali Bisnis & Cloud",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Tumuwuh Cloud Database & ERP Synchronization",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = Slate700
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cloud Connection Hero Card
            CloudConnectionHeaderCard(
                status = cloudStatus,
                userRole = userRole,
                onSyncClick = { viewModel.syncOnlineCloudDatabase() },
                onOpenWebClick = {
                    onDismiss()
                    viewModel.setNavTab(2)
                },
                onOpenChromeClick = {
                    viewModel.openWebDashboard(context)
                },
                onResetClick = { showResetConfirmationDialog = true },
                onRoleChange = { newRole -> viewModel.setUserRole(newRole) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Tabs
            val tabs = listOf(
                "Laba Rugi & Rasio",
                "Kas & Jurnal",
                "PO Supplier",
                "CRM & Promo",
                "Pajak & Marketplace"
            )

            ScrollableTabRow(
                selectedTabIndex = activeTab,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = { HorizontalDivider(color = Slate200) },
                indicator = { tabPositions ->
                    if (activeTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                            color = CoffeeBrown
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { viewModel.setControlCenterTab(index) },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (activeTab == index) CoffeeBrown else Slate500
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    0 -> FinancialStatementsTab(statement = financialStatement)
                    1 -> JournalEntriesTab(
                        entries = journalEntries,
                        onAddEntryClick = { showNewJournalDialog = true }
                    )
                    2 -> SupplierOrdersTab(
                        orders = supplierOrders,
                        onSendEmail = { order -> viewModel.sendSupplierPoEmail(context, order) },
                        onUpdateStatus = { order, status -> viewModel.updateSupplierOrderStatus(order, status) },
                        onAddPoClick = { showNewPoDialog = true }
                    )
                    3 -> CustomerCrmTab(
                        customers = customers,
                        onAddCustomerClick = { showNewCustomerDialog = true }
                    )
                    4 -> TaxAndMarketplaceTab(
                        taxSummary = taxSummary,
                        marketplaceOrders = marketplaceOrders,
                        onImportMarketplace = { showMarketplaceImportDialog = true }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showNewJournalDialog) {
        NewJournalEntryDialog(
            userRole = userRole,
            onDismiss = { showNewJournalDialog = false },
            onConfirm = { entryNo, accCode, accName, desc, debit, credit, unit ->
                viewModel.addJournalEntry(
                    entryNumber = entryNo,
                    accountCode = accCode,
                    accountName = accName,
                    description = desc,
                    debit = debit,
                    credit = credit,
                    unitCategory = unit,
                    authorizedBy = userRole
                )
                showNewJournalDialog = false
                Toast.makeText(context, "Entri Jurnal Berhasil Disimpan & Sinkron Cloud", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showNewPoDialog) {
        NewSupplierPoDialog(
            onDismiss = { showNewPoDialog = false },
            onConfirm = { name, email, phone, cat, due, items, total, notes ->
                viewModel.createSupplierOrder(
                    supplierName = name,
                    supplierEmail = email,
                    supplierPhone = phone,
                    category = cat,
                    dueDate = due,
                    itemsSummary = items,
                    totalAmount = total,
                    notes = notes
                )
                showNewPoDialog = false
                Toast.makeText(context, "Purchase Order Berhasil Dibuat", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showNewCustomerDialog) {
        NewCustomerDialog(
            onDismiss = { showNewCustomerDialog = false },
            onConfirm = { name, phone, email, favItem, points, spent ->
                viewModel.addOrUpdateCustomer(name, phone, email, favItem, points, spent)
                showNewCustomerDialog = false
                Toast.makeText(context, "Profil Pelanggan Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showMarketplaceImportDialog) {
        ImportMarketplaceDialog(
            onDismiss = { showMarketplaceImportDialog = false },
            onConfirm = { marketplace, buyer, items, total ->
                viewModel.importMarketplaceOrder(marketplace, buyer, items, total)
                showMarketplaceImportDialog = false
                Toast.makeText(context, "Transaksi Marketplace Berhasil Diimpor & e-Faktur Diperbarui", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showResetConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmationDialog = false },
            title = {
                Text("Reset Data Penjualan ke 0", fontWeight = FontWeight.Bold, color = Slate900)
            },
            text = {
                Text(
                    "Apakah Anda yakin ingin menghapus seluruh riwayat data penjualan, cash flow, dan transaksi kasir menjadi Rp 0? Daftar produk dan menu katalog tetap aman.",
                    fontSize = 14.sp,
                    color = Slate700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetSalesAndCashflowToZero()
                        showResetConfirmationDialog = false
                        Toast.makeText(context, "Data penjualan & cashflow berhasil direset menjadi 0", Toast.LENGTH_LONG).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseRed)
                ) {
                    Text("Ya, Reset ke 0", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmationDialog = false }) {
                    Text("Batal", color = Slate700)
                }
            }
        )
    }
}

@Composable
fun CloudConnectionHeaderCard(
    status: CloudSyncStatus,
    userRole: String,
    onSyncClick: () -> Unit,
    onOpenWebClick: () -> Unit,
    onOpenChromeClick: () -> Unit,
    onResetClick: () -> Unit,
    onRoleChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Online Cloud Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (status.isOnline) EmeraldGreen else RoseRed)
                    )
                    Text(
                        text = if (status.isSyncing) "SINKRONISASI CLOUD..." else "DATABASE ONLINE REAL-TIME",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (status.isOnline) EmeraldGreenLight else RoseRedLight,
                        letterSpacing = 0.5.sp
                    )
                }

                // Role Badge & Switcher
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Slate800)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Role",
                        tint = GoldAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Otorisasi: $userRole",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = status.lastSyncMessage,
                fontSize = 12.sp,
                color = Slate200,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Big Button: Buka Dashboard di Chrome
            Button(
                onClick = onOpenChromeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("open_chrome_dashboard_hero_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "Chrome",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("🚀 Buka Dashboard Web di Google Chrome", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Button Buka Website
                OutlinedButton(
                    onClick = onOpenWebClick,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("open_web_portal_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = "Website",
                        tint = Slate300,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Portal App", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = White)
                }

                // Button Sinkronisasi Cloud
                Button(
                    onClick = onSyncClick,
                    enabled = !status.isSyncing,
                    modifier = Modifier
                        .weight(1.1f)
                        .testTag("sync_cloud_now_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AmberOrange)
                ) {
                    if (status.isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Slate900,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sync",
                            tint = Slate900,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sync Cloud", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate900)
                }

                // Button Reset Data ke 0
                OutlinedButton(
                    onClick = onResetClick,
                    modifier = Modifier
                        .testTag("reset_sales_data_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = RoseRedLight
                    ),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Reset Data",
                        tint = RoseRedLight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Reset 0", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoseRedLight)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 0: Laporan Keuangan & Rasio
// -------------------------------------------------------------
@Composable
fun FinancialStatementsTab(statement: FinancialStatementSummary) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Laba Rugi Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Laporan Laba Rugi (P&L)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EmeraldGreenLight)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Margin: ${String.format(Locale.getDefault(), "%.1f", statement.netMarginPercentage)}%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreenDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    FinancialRowItem(label = "Total Pendapatan (Omset)", value = statement.totalRevenue, isPositive = true)
                    FinancialRowItem(label = "Beban Pokok Penjualan (HPP)", value = -statement.totalHpp, isPositive = false)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Slate200)
                    FinancialRowItem(
                        label = "Laba Kotor (Gross Profit)",
                        value = statement.grossProfit,
                        isBold = true,
                        isPositive = statement.grossProfit >= 0
                    )
                    FinancialRowItem(label = "Beban Operasional & Kas", value = -statement.totalOperationalExpenses, isPositive = false)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Slate200)
                    FinancialRowItem(
                        label = "Laba Bersih (Net Profit)",
                        value = statement.netProfit,
                        isHighlight = true,
                        isPositive = statement.netProfit >= 0
                    )
                }
            }
        }

        // 2. Neraca / Balance Sheet Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Neraca Keuangan (Balance Sheet)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("AKTIVA (ASET)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                    FinancialRowItem(label = "Kas Laci & Rekening Bank", value = statement.totalCashAndBank)
                    FinancialRowItem(label = "Piutang Usaha (Pesanan)", value = statement.totalAccountsReceivable)
                    FinancialRowItem(label = "Persediaan Barang & Stok", value = statement.totalInventoryValue)
                    FinancialRowItem(label = "Total Aset Lancar", value = statement.totalCurrentAssets, isBold = true)

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("PASIVA (KEWAJIBAN & EKUITAS)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                    FinancialRowItem(label = "Hutang Supplier (PO)", value = statement.totalAccountsPayable, isPositive = false)
                    FinancialRowItem(label = "Modal Disetor Pemilik", value = statement.ownerEquity)
                    FinancialRowItem(label = "Laba Ditahan / Akumulasi", value = statement.retainedEarnings)
                    FinancialRowItem(label = "Total Pasiva", value = statement.totalLiabilitiesAndEquity, isBold = true)
                }
            }
        }

        // 3. Rasio Keuangan Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Rasio Likuiditas & Kinerja Keuangan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RatioMetricBox(
                            title = "Current Ratio",
                            value = "${String.format(Locale.getDefault(), "%.2f", statement.currentRatio)}x",
                            desc = "Likuiditas Aset",
                            modifier = Modifier.weight(1f)
                        )
                        RatioMetricBox(
                            title = "Quick Ratio",
                            value = "${String.format(Locale.getDefault(), "%.2f", statement.quickRatio)}x",
                            desc = "Aset Cepat",
                            modifier = Modifier.weight(1f)
                        )
                        RatioMetricBox(
                            title = "Gross Margin",
                            value = "${String.format(Locale.getDefault(), "%.1f", statement.grossMarginPercentage)}%",
                            desc = "Profitabilitas",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun FinancialRowItem(
    label: String,
    value: Double,
    isBold: Boolean = false,
    isHighlight: Boolean = false,
    isPositive: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = if (isHighlight) 13.sp else 12.sp,
            fontWeight = if (isBold || isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) Slate900 else Slate700
        )
        Text(
            text = (if (value < 0) "- " else "") + FormatUtils.formatRupiah(Math.abs(value)),
            fontSize = if (isHighlight) 14.sp else 12.sp,
            fontWeight = if (isBold || isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            color = when {
                isHighlight -> if (isPositive) EmeraldGreenDark else RoseRed
                !isPositive -> RoseRed
                else -> Slate900
            }
        )
    }
}

@Composable
fun RatioMetricBox(title: String, value: String, desc: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = title, fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CoffeeBrown)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = desc, fontSize = 9.sp, color = Slate400)
        }
    }
}

// -------------------------------------------------------------
// TAB 1: Kas & Jurnal Umum (Double-Entry)
// -------------------------------------------------------------
@Composable
fun JournalEntriesTab(
    entries: List<JournalEntryEntity>,
    onAddEntryClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Buku Jurnal Umum (Double-Entry)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "${entries.size} Transaksi Terposting",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }

            Button(
                onClick = onAddEntryClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                modifier = Modifier.testTag("add_journal_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Entri Jurnal", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada entri jurnal.", color = Slate400, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(entries) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Slate100),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Slate800)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(entry.entryNumber, fontSize = 10.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                                    }
                                    Text(entry.accountCode, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                                }
                                Text(
                                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(entry.timestamp)),
                                    fontSize = 10.sp,
                                    color = Slate400
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = entry.accountName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate900
                            )
                            Text(
                                text = entry.description,
                                fontSize = 11.sp,
                                color = Slate500
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Unit: ${entry.unitCategory} • Auth: ${entry.authorizedBy}",
                                    fontSize = 10.sp,
                                    color = Slate400
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    if (entry.debit > 0) {
                                        Text(
                                            text = "DEBET: ${FormatUtils.formatRupiah(entry.debit.toDouble())}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreenDark
                                        )
                                    }
                                    if (entry.credit > 0) {
                                        Text(
                                            text = "KREDIT: ${FormatUtils.formatRupiah(entry.credit.toDouble())}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoseRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: PO & Supplier Invoices
// -------------------------------------------------------------
@Composable
fun SupplierOrdersTab(
    orders: List<SupplierOrderEntity>,
    onSendEmail: (SupplierOrderEntity) -> Unit,
    onUpdateStatus: (SupplierOrderEntity, String) -> Unit,
    onAddPoClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Purchase Order & Supplier Invoices",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "Kirim PO & Pengadaan Bahan Baku",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }

            Button(
                onClick = onAddPoClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                modifier = Modifier.testTag("create_po_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Buat PO", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(orders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = order.poNumber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CoffeeBrown
                            )
                            val (badgeBg, badgeFg) = when (order.status) {
                                "PAID" -> Pair(EmeraldGreenLight, EmeraldGreenDark)
                                "RECEIVED" -> Pair(BilliardPurpleLight, BilliardPurple)
                                "SENT" -> Pair(AmberOrangeLight, AmberOrange)
                                else -> Pair(Slate200, Slate700)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(badgeBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(order.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = badgeFg)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = order.supplierName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Email: ${order.supplierEmail} | Unit: ${order.category}",
                            fontSize = 11.sp,
                            color = Slate500
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = order.itemsSummary,
                                fontSize = 11.sp,
                                color = Slate700
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Total Tagihan", fontSize = 10.sp, color = Slate500)
                                Text(
                                    text = FormatUtils.formatRupiah(order.totalAmount.toDouble()),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { onSendEmail(order) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.testTag("send_po_email_${order.id}")
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = "Kirim", modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Kirim Email", fontSize = 11.sp)
                                }

                                if (order.status != "PAID") {
                                    Button(
                                        onClick = {
                                            val next = if (order.status == "SENT") "RECEIVED" else "PAID"
                                            onUpdateStatus(order, next)
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenDark)
                                    ) {
                                        Text(if (order.status == "SENT") "Terima" else "Lunas", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: CRM & Promo
// -------------------------------------------------------------
@Composable
fun CustomerCrmTab(
    customers: List<CustomerProfileEntity>,
    onAddCustomerClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Insight Pelanggan & Promo Poin",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "${customers.size} Member Terdaftar",
                    fontSize = 11.sp,
                    color = Slate500
                )
            }

            Button(
                onClick = onAddCustomerClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                modifier = Modifier.testTag("add_customer_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Member Baru", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(customers) { customer ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate100),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = customer.customerName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            val tierColor = when (customer.tier) {
                                "VIP" -> GoldAccent
                                "GOLD" -> AmberOrange
                                "SILVER" -> Slate500
                                else -> CoffeeBrown
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Slate900)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "TIER ${customer.tier}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tierColor
                                )
                            }
                        }

                        Text(
                            text = "No. Telp: ${customer.phone} • Kunjungan: ${customer.visitCount}x",
                            fontSize = 11.sp,
                            color = Slate500
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White)
                                    .padding(6.dp)
                            ) {
                                Column {
                                    Text("Item Favorit", fontSize = 9.sp, color = Slate400)
                                    Text(customer.favoriteItem, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CoffeeBrown)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White)
                                    .padding(6.dp)
                            ) {
                                Column {
                                    Text("Poin Reward", fontSize = 9.sp, color = Slate400)
                                    Text("${customer.loyaltyPoints} Poin", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreenDark)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Kupon Aktif: ${customer.activeCoupons}",
                            fontSize = 10.sp,
                            color = Slate700,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 4: Pajak e-Faktur & Marketplace
// -------------------------------------------------------------
@Composable
fun TaxAndMarketplaceTab(
    taxSummary: TaxReportSummary,
    marketplaceOrders: List<MarketplaceOrderEntity>,
    onImportMarketplace: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Tax Report Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Ringkasan Pajak Terintegrasi e-Faktur",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FinancialRowItem(label = "Dasar Pengenaan Pajak (DPP)", value = taxSummary.taxableRevenue)
                    FinancialRowItem(label = "PPN Terutang (11%)", value = taxSummary.totalPpn, isPositive = false)
                    FinancialRowItem(label = "PPh Final UMKM (0.5% PP 55/2022)", value = taxSummary.totalPphFinal, isPositive = false)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Slate200)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Faktur Penjualan Terbit", fontSize = 12.sp, color = Slate700)
                        Text("${taxSummary.eFakturInvoiceCount} Invoice e-Faktur", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CoffeeBrown)
                    }
                }
            }
        }

        // Marketplace Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Integrasi Marketplace Online",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Tokopedia, Shopee, Bukalapak",
                        fontSize = 11.sp,
                        color = Slate500
                    )
                }

                Button(
                    onClick = onImportMarketplace,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                    modifier = Modifier.testTag("import_marketplace_button")
                ) {
                    Icon(Icons.Default.Store, contentDescription = "Import", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Impor Pesanan", fontSize = 12.sp)
                }
            }
        }

        items(marketplaceOrders) { order ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Slate100),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    when (order.marketplace) {
                                        "TOKOPEDIA" -> EmeraldGreenDark
                                        "SHOPEE" -> AmberOrange
                                        else -> RoseRed
                                    }
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(order.marketplace, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Text(order.orderNumber, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Pembeli: ${order.buyerName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                    Text(order.itemsSummary, fontSize = 11.sp, color = Slate500)

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("PPN: ${FormatUtils.formatRupiah(order.ppnAmount.toDouble())}", fontSize = 10.sp, color = Slate400)
                        Text(FormatUtils.formatRupiah(order.totalPrice.toDouble()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// -------------------------------------------------------------
// DIALOGS
// -------------------------------------------------------------
@Composable
fun NewJournalEntryDialog(
    userRole: String,
    onDismiss: () -> Unit,
    onConfirm: (entryNo: String, accCode: String, accName: String, desc: String, debit: Long, credit: Long, unit: String) -> Unit
) {
    var entryNo by remember { mutableStateOf("JU-${SimpleDateFormat("MMdd-HHmm", Locale.getDefault()).format(Date())}") }
    var accCode by remember { mutableStateOf("101-KAS") }
    var accName by remember { mutableStateOf("Kas Operasional") }
    var desc by remember { mutableStateOf("") }
    var debitStr by remember { mutableStateOf("0") }
    var creditStr by remember { mutableStateOf("0") }
    var unit by remember { mutableStateOf("BAR") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Buat Entri Jurnal Umum", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = entryNo,
                    onValueChange = { entryNo = it },
                    label = { Text("No. Jurnal") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = accCode,
                    onValueChange = { accCode = it },
                    label = { Text("Kode Akun (cth: 101-KAS, 401-REV)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = accName,
                    onValueChange = { accName = it },
                    label = { Text("Nama Akun") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Keterangan Transaksi") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = debitStr,
                        onValueChange = { debitStr = it },
                        label = { Text("Debet (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = creditStr,
                        onValueChange = { creditStr = it },
                        label = { Text("Kredit (Rp)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val deb = debitStr.toLongOrNull() ?: 0L
                    val cred = creditStr.toLongOrNull() ?: 0L
                    if (desc.isNotBlank()) {
                        onConfirm(entryNo, accCode, accName, desc, deb, cred, unit)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
            ) {
                Text("Simpan Jurnal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun NewSupplierPoDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, phone: String, cat: String, due: Long, items: String, total: Long, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf("BAR") }
    var items by remember { mutableStateOf("") }
    var totalStr by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Buat Purchase Order Supplier", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Supplier / PT") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Supplier (untuk kirim PO)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = items,
                    onValueChange = { items = it },
                    label = { Text("Rincian Barang & Kuantitas") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = totalStr,
                    onValueChange = { totalStr = it },
                    label = { Text("Estimasi Total Tagihan (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan / Instruksi Pengiriman") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val tot = totalStr.toLongOrNull() ?: 0L
                    if (name.isNotBlank() && email.isNotBlank()) {
                        onConfirm(name, email, phone, cat, System.currentTimeMillis() + 86400000L * 3, items, tot, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
            ) {
                Text("Buat PO")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun NewCustomerDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, email: String, favItem: String, points: Int, spent: Long) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var favItem by remember { mutableStateOf("Iced Caramel Macchiato") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daftar Member / Pelanggan Baru", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Pelanggan") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Nomor WhatsApp / HP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (Opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = favItem,
                    onValueChange = { favItem = it },
                    label = { Text("Menu / Layanan Favorit") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onConfirm(name, phone, email, favItem, 50, 50000L)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
            ) {
                Text("Daftarkan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun ImportMarketplaceDialog(
    onDismiss: () -> Unit,
    onConfirm: (marketplace: String, buyer: String, items: String, total: Long) -> Unit
) {
    var marketplace by remember { mutableStateOf("TOKOPEDIA") }
    var buyer by remember { mutableStateOf("") }
    var items by remember { mutableStateOf("2x Tumuwuh House Blend Coffee Beans 250g") }
    var totalStr by remember { mutableStateOf("180000") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Impor Transaksi Marketplace", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("TOKOPEDIA", "SHOPEE", "BUKALAPAK").forEach { mkt ->
                        val isSelected = marketplace == mkt
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CoffeeBrown else Slate100)
                                .clickable { marketplace = mkt }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mkt,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Slate700
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = buyer,
                    onValueChange = { buyer = it },
                    label = { Text("Nama Pembeli Marketplace") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = items,
                    onValueChange = { items = it },
                    label = { Text("Item Pesanan") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = totalStr,
                    onValueChange = { totalStr = it },
                    label = { Text("Total Transaksi (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val tot = totalStr.toLongOrNull() ?: 0L
                    if (buyer.isNotBlank()) {
                        onConfirm(marketplace, buyer, items, tot)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
            ) {
                Text("Impor Pesanan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
