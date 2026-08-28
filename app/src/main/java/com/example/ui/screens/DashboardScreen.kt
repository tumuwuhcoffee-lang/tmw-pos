package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CategoryBadge
import com.example.ui.components.InteractiveCalendarView
import com.example.ui.components.ProductSalesDetailDialog
import com.example.ui.components.StatCard
import com.example.ui.components.WhiteCard
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.AmberOrangeLight
import com.example.ui.theme.BarCategoryColor
import com.example.ui.theme.BilliardCategoryColor
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CrimsonRedLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.GorCategoryColor
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.ui.viewmodel.CategoryRevenue
import com.example.ui.viewmodel.PosViewModel
import com.example.util.FormatUtils

@Composable
fun DashboardScreen(viewModel: PosViewModel) {
    val startDate by viewModel.dashboardStartDate.collectAsState()
    val endDate by viewModel.dashboardEndDate.collectAsState()
    val dateMode by viewModel.dateSelectionMode.collectAsState()

    val transactions by viewModel.dashboardTransactions.collectAsState()
    val prevRevenue by viewModel.previousPeriodRevenue.collectAsState()
    val activeShift by viewModel.activeShift.collectAsState()
    val cashflowEntries by viewModel.allCashflows.collectAsState()

    val barStat by viewModel.barRevenueStat.collectAsState()
    val billiardStat by viewModel.billiardRevenueStat.collectAsState()
    val gorStat by viewModel.gorRevenueStat.collectAsState()

    val top5Products by viewModel.topSellingProducts.collectAsState()
    val allSoldProducts by viewModel.allSoldProductsInPeriod.collectAsState()

    var showProductDetailDialog by remember { mutableStateOf(false) }
    var showCalendarPicker by remember { mutableStateOf(false) }

    // Computed Aggregates
    val totalRevenue = transactions.sumOf { it.totalAmount }
    val totalCost = transactions.sumOf { it.totalCostPrice }
    val grossProfit = totalRevenue - totalCost
    val profitMargin = if (totalRevenue > 0) (grossProfit / totalRevenue) * 100 else 0.0

    // Selisih Omset
    val revenueDifference = totalRevenue - prevRevenue
    val diffPercent = if (prevRevenue > 0) ((revenueDifference / prevRevenue) * 100) else 0.0
    val isPositiveDiff = revenueDifference >= 0

    // Cash Flow Net Calculation
    val totalDebit = cashflowEntries.filter { it.type == "DEBIT" }.sumOf { it.amount }
    val totalCredit = cashflowEntries.filter { it.type == "CREDIT" }.sumOf { it.amount }
    val cashflowBalance = totalDebit - totalCredit

    // Payment Methods Split
    val cashTotal = transactions.filter { it.paymentMethod == "CASH" }.sumOf { it.totalAmount }
    val qrisTotal = transactions.filter { it.paymentMethod == "QRIS" }.sumOf { it.totalAmount }
    val debitTotal = transactions.filter { it.paymentMethod != "CASH" && it.paymentMethod != "QRIS" }.sumOf { it.totalAmount }

    val dateRangeLabel = remember(startDate, endDate) {
        val startCal = java.util.Calendar.getInstance().apply { timeInMillis = startDate }
        val endCal = java.util.Calendar.getInstance().apply { timeInMillis = endDate }
        val isSingleDay = startCal.get(java.util.Calendar.YEAR) == endCal.get(java.util.Calendar.YEAR) &&
                startCal.get(java.util.Calendar.DAY_OF_YEAR) == endCal.get(java.util.Calendar.DAY_OF_YEAR)

        if (isSingleDay) {
            FormatUtils.formatDate(startDate)
        } else {
            "${FormatUtils.formatShortDate(startDate)} - ${FormatUtils.formatShortDate(endDate)}"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // 1. High Density Tumuwuh POS Brand Header + Live Shift Badge
        item {
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
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Slate900)
                            .border(1.dp, Slate700, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tumuwuh_logo),
                            contentDescription = "Tumuwuh POS Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp))
                        )
                    }
                    Column {
                        Text(
                            text = "Tumuwuh POS",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Café, Billiard & GOR Management",
                            fontSize = 11.sp,
                            color = Slate500
                        )
                    }
                }

                // Shift Active Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (activeShift != null) EmeraldGreenLight else AmberOrangeLight)
                        .border(
                            1.dp,
                            if (activeShift != null) EmeraldGreen.copy(alpha = 0.3f) else AmberOrange.copy(alpha = 0.3f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (activeShift != null) EmeraldGreen else AmberOrange)
                    )
                    Text(
                        text = if (activeShift != null) "SHIFT ACTIVE" else "SHIFT CLOSED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeShift != null) EmeraldGreenDark else AmberOrange,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // 2. Total Omset Hero Header + Calendar Icon Trigger
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Omset ($dateRangeLabel)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate500,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = FormatUtils.formatRupiah(totalRevenue),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            color = Slate900
                        )
                        if (prevRevenue > 0) {
                            Text(
                                text = "${if (isPositiveDiff) "+" else ""}${String.format("%.1f", diffPercent)}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPositiveDiff) EmeraldGreen else CrimsonRed,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (showCalendarPicker) PrimaryBlueLight else Slate100,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (showCalendarPicker) PrimaryBlue else Slate200
                    ),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { showCalendarPicker = !showCalendarPicker }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Kalender Filter",
                            tint = if (showCalendarPicker) PrimaryBlue else Slate700,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Interactive Calendar View (Expandable)
        item {
            InteractiveCalendarView(
                startDate = startDate,
                endDate = endDate,
                selectedMode = dateMode,
                onQuickFilterSelected = { mode -> viewModel.setQuickDateFilter(mode) },
                onDateClicked = { dateMillis -> viewModel.handleCalendarDateClick(dateMillis) }
            )
        }

        // 3. High Density Split Grid: [Selisih Kemarin / Periode Sebelumnya] & [Cash Flow Net Dark Card]
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: Selisih Kemarin
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Slate50,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SELISIH OMSET",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate500,
                            letterSpacing = 0.5.sp
                        )
                        Column {
                            Text(
                                text = "${if (isPositiveDiff) "+" else ""}${FormatUtils.formatCompactRupiah(revenueDifference)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = if (isPositiveDiff) "Tren Meningkat" else "Tren Menurun",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isPositiveDiff) EmeraldGreen else CrimsonRed
                            )
                        }
                    }
                }

                // Card 2: Cash Flow Net (Dark Theme Card)
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Slate900
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CASH FLOW NET",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 0.5.sp
                        )
                        Column {
                            Text(
                                text = FormatUtils.formatCompactRupiah(cashflowBalance),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                            Text(
                                text = "D: ${FormatUtils.formatCompactRupiah(totalDebit)} | K: ${FormatUtils.formatCompactRupiah(totalCredit)}",
                                fontSize = 10.sp,
                                color = Slate400
                            )
                        }
                    }
                }
            }
        }

        // 4. Financial KPI Sub-Grid: Total HPP & Gross Profit
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total HPP (Modal)",
                    value = FormatUtils.formatRupiah(totalCost),
                    subtitle = "Beban Pokok",
                    icon = Icons.Default.ReceiptLong,
                    accentColor = AmberOrange,
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "Keuntungan Bersih",
                    value = FormatUtils.formatRupiah(grossProfit),
                    subtitle = "Margin: ${String.format("%.1f", profitMargin)}%",
                    icon = Icons.Default.Percent,
                    accentColor = EmeraldGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 5. High Density Category Distribution & Progress Bars (BAR, BILLIARD, GOR)
        item {
            WhiteCard(
                shape = RoundedCornerShape(20.dp),
                borderColor = Slate200
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "DISTRIBUSI KATEGORI & METODE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        letterSpacing = 0.5.sp
                    )

                    // BAR
                    CategoryDensityRow(
                        name = "BAR (Coffee & Drinks)",
                        revenue = barStat.totalRevenue,
                        totalRevenue = totalRevenue,
                        color = BarCategoryColor,
                        cashRevenue = barStat.cashRevenue,
                        qrisRevenue = barStat.qrisRevenue + barStat.debitRevenue
                    )

                    HorizontalDivider(color = Slate100, thickness = 1.dp)

                    // BILLIARD
                    CategoryDensityRow(
                        name = "BILLIARD (Table Rental)",
                        revenue = billiardStat.totalRevenue,
                        totalRevenue = totalRevenue,
                        color = BilliardCategoryColor,
                        cashRevenue = billiardStat.cashRevenue,
                        qrisRevenue = billiardStat.qrisRevenue + billiardStat.debitRevenue
                    )

                    HorizontalDivider(color = Slate100, thickness = 1.dp)

                    // GOR
                    CategoryDensityRow(
                        name = "GOR (Sport Hall)",
                        revenue = gorStat.totalRevenue,
                        totalRevenue = totalRevenue,
                        color = GorCategoryColor,
                        cashRevenue = gorStat.cashRevenue,
                        qrisRevenue = gorStat.qrisRevenue + gorStat.debitRevenue
                    )
                }
            }
        }

        // 6. Payment Methods Breakdown (Cash vs QRIS vs Debit)
        item {
            WhiteCard(
                shape = RoundedCornerShape(20.dp),
                borderColor = Slate200
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "METODE PEMBAYARAN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate500,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PaymentMethodStatItem(
                            label = "Tunai (Cash)",
                            amount = cashTotal,
                            color = EmeraldGreen,
                            icon = Icons.Default.Payments,
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodStatItem(
                            label = "QRIS Barcode",
                            amount = qrisTotal,
                            color = PrimaryBlue,
                            icon = Icons.Default.QrCode2,
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodStatItem(
                            label = "Kartu Debit",
                            amount = debitTotal,
                            color = AmberOrange,
                            icon = Icons.Default.PointOfSale,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 7. High Density Best-Selling Products (Produk Terlaris 01 - 05)
        item {
            WhiteCard(
                shape = RoundedCornerShape(20.dp),
                borderColor = Slate200
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRODUK TERLARIS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate500,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = "Lihat Semua",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue,
                            modifier = Modifier.clickable { showProductDetailDialog = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (top5Products.isEmpty()) {
                        Text(
                            text = "Belum ada transaksi di tanggal ini.",
                            fontSize = 12.sp,
                            color = Slate500,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            top5Products.forEachIndexed { index, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Slate50)
                                        .border(1.dp, Slate100, RoundedCornerShape(14.dp))
                                        .clickable { showProductDetailDialog = true }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Slate200.copy(alpha = 0.7f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = String.format("%02d", index + 1),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate700
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = item.productName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate900
                                            )
                                            Text(
                                                text = "${item.category} • ${item.totalQtySold} Terjual",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Slate500
                                            )
                                        }
                                    }

                                    Text(
                                        text = FormatUtils.formatRupiah(item.totalRevenue),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate900
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialog for viewing all products sales detail
    if (showProductDetailDialog) {
        ProductSalesDetailDialog(
            products = allSoldProducts,
            dateRangeLabel = dateRangeLabel,
            onDismiss = { showProductDetailDialog = false }
        )
    }
}

// ----------------------------------------------------
// SUB-COMPONENTS FOR HIGH DENSITY DASHBOARD
// ----------------------------------------------------

@Composable
private fun CategoryDensityRow(
    name: String,
    revenue: Double,
    totalRevenue: Double,
    color: Color,
    cashRevenue: Double,
    qrisRevenue: Double
) {
    val percentage = if (totalRevenue > 0) (revenue / totalRevenue).toFloat() else 0f
    val cashPct = if (revenue > 0) (cashRevenue / revenue * 100).toInt() else 0
    val qrisPct = if (revenue > 0) (qrisRevenue / revenue * 100).toInt() else 0

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(
                    text = name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate800
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = FormatUtils.formatCompactRupiah(revenue),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "Cash: $cashPct% | QRIS: $qrisPct%",
                    fontSize = 9.sp,
                    color = Slate400
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Slate100)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage.coerceIn(0f, 1f))
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun PaymentMethodStatItem(
    label: String,
    amount: Double,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, color = Slate600)
        Text(
            text = FormatUtils.formatCompactRupiah(amount),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Slate900
        )
    }
}
