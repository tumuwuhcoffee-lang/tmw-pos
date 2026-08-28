package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.ShiftEntity
import com.example.data.local.entity.TransactionEntity
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.AmberOrangeLight
import com.example.ui.theme.BarCategoryColor
import com.example.ui.theme.BilliardCategoryColor
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CrimsonRedLight
import com.example.ui.theme.EmeraldGreen
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
import com.example.util.FormatUtils

// 1. OPEN SHIFT DIALOG / SCREEN
@Composable
fun OpenShiftDialog(
    onDismiss: () -> Unit,
    onConfirmOpen: (
        cashierName: String,
        initialCash: Double,
        initialBarCash: Double,
        initialBilliardCash: Double,
        initialGorCash: Double,
        notes: String
    ) -> Unit
) {
    var cashierName by remember { mutableStateOf("Kasir Utama") }
    var initialCashText by remember { mutableStateOf("500000") }
    var initialBarText by remember { mutableStateOf("200000") }
    var initialBilliardText by remember { mutableStateOf("150000") }
    var initialGorText by remember { mutableStateOf("150000") }
    var notes by remember { mutableStateOf("Shift Operasional") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = White,
            tonalElevation = 6.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBlueLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Buka Shift Kasir",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = "Input Modal Awal & Pembagian Kas",
                                    fontSize = 12.sp,
                                    color = Slate600
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                        }
                    }
                }

                item {
                    Text(text = "Nama Kasir Bertugas", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = cashierName,
                        onValueChange = { cashierName = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Slate200
                        )
                    )
                }

                item {
                    Text(text = "Total Modal Awal (Kas Fisik di Laci)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = initialCashText,
                        onValueChange = { initialCashText = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = PrimaryBlue) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Slate200
                        )
                    )
                }

                // Breakdown 3 Category allocation
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Slate100.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Alokasi Modal Awal per Pintu (Opsional)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )

                            // Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Coffee, contentDescription = null, tint = BarCategoryColor, modifier = Modifier.size(16.dp))
                                    Text(text = "Kas Bar", fontSize = 12.sp, color = Slate800)
                                }
                                OutlinedTextField(
                                    value = initialBarText,
                                    onValueChange = { initialBarText = it },
                                    modifier = Modifier.weight(1.5f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    prefix = { Text("Rp ", fontSize = 11.sp, color = Slate500) },
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                            }

                            // Billiard
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = BilliardCategoryColor, modifier = Modifier.size(16.dp))
                                    Text(text = "Kas Billiard", fontSize = 12.sp, color = Slate800)
                                }
                                OutlinedTextField(
                                    value = initialBilliardText,
                                    onValueChange = { initialBilliardText = it },
                                    modifier = Modifier.weight(1.5f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    prefix = { Text("Rp ", fontSize = 11.sp, color = Slate500) },
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                            }

                            // GOR
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.SportsTennis, contentDescription = null, tint = GorCategoryColor, modifier = Modifier.size(16.dp))
                                    Text(text = "Kas GOR", fontSize = 12.sp, color = Slate800)
                                }
                                OutlinedTextField(
                                    value = initialGorText,
                                    onValueChange = { initialGorText = it },
                                    modifier = Modifier.weight(1.5f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    prefix = { Text("Rp ", fontSize = 11.sp, color = Slate500) },
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                            }
                        }
                    }
                }

                item {
                    Text(text = "Catatan Shift", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val totalCash = initialCashText.toDoubleOrNull() ?: 0.0
                            val barCash = initialBarText.toDoubleOrNull() ?: 0.0
                            val bilCash = initialBilliardText.toDoubleOrNull() ?: 0.0
                            val gorCash = initialGorText.toDoubleOrNull() ?: 0.0
                            onConfirmOpen(cashierName, totalCash, barCash, bilCash, gorCash, notes)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Buka Kasir Sekarang", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// 2. CLOSE SHIFT DIALOG WITH OPERATIONAL REPORT & SELISIH KAS
@Composable
fun CloseShiftDialog(
    shift: ShiftEntity,
    shiftTransactions: List<TransactionEntity>,
    shiftCashflows: List<CashflowEntity>,
    onDismiss: () -> Unit,
    onConfirmClose: (actualCash: Double, notes: String) -> Unit
) {
    val paidTrxs = shiftTransactions.filter { it.paymentStatus == "PAID" }
    val cancelledTrxs = shiftTransactions.filter { it.paymentStatus == "CANCELLED" }

    val totalCashSales = paidTrxs.filter { it.paymentMethod == "CASH" }.sumOf { it.totalAmount }
    val totalQrisSales = paidTrxs.filter { it.paymentMethod == "QRIS" }.sumOf { it.totalAmount }
    val totalDebitSales = paidTrxs.filter { it.paymentMethod != "CASH" && it.paymentMethod != "QRIS" }.sumOf { it.totalAmount }
    val totalSales = paidTrxs.sumOf { it.totalAmount }

    // Category breakdown
    val barSales = paidTrxs.sumOf { it.barRevenue }
    val billiardSales = paidTrxs.sumOf { it.billiardRevenue }
    val gorSales = paidTrxs.sumOf { it.gorRevenue }

    // Other cashflows in this shift
    val otherCashIn = shiftCashflows.filter { it.type == "DEBIT" && it.category != "MODAL_AWAL" && it.category != "PENJUALAN" }.sumOf { it.amount }
    val otherCashOut = shiftCashflows.filter { it.type == "KREDIT" && it.paymentMethod == "CASH" }.sumOf { it.amount }

    val expectedCashInDrawer = shift.initialCash + totalCashSales + otherCashIn - otherCashOut

    var actualCashText by remember { mutableStateOf(expectedCashInDrawer.toLong().toString()) }
    var notes by remember { mutableStateOf("") }

    val actualCash = actualCashText.toDoubleOrNull() ?: 0.0
    val cashDifference = actualCash - expectedCashInDrawer

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp)),
            color = White,
            tonalElevation = 8.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonRedLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = CrimsonRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Tutup Shift #${shift.shiftNumber}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900
                                )
                                Text(
                                    text = "Kasir: ${shift.cashierName} • Buka: ${FormatUtils.formatTime(shift.startTime)}",
                                    fontSize = 12.sp,
                                    color = Slate600
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                        }
                    }
                }

                // Operational Summary Card
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Slate100.copy(alpha = 0.5f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "RINGKASAN OPERASIONAL SHIFT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Slate700
                            )
                            HorizontalDivider(color = Slate200, thickness = 1.dp)

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Modal Awal Kasir", fontSize = 12.sp, color = Slate600)
                                Text(text = FormatUtils.formatRupiah(shift.initialCash), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Penjualan Tunai (+)", fontSize = 12.sp, color = EmeraldGreen)
                                Text(text = "+${FormatUtils.formatRupiah(totalCashSales)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = EmeraldGreen)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "Penjualan QRIS / Non-Tunai", fontSize = 12.sp, color = PrimaryBlue)
                                Text(text = FormatUtils.formatRupiah(totalQrisSales + totalDebitSales), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                            }
                            if (otherCashOut > 0) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Pengeluaran Kas/Petty Cash (-)", fontSize = 12.sp, color = CrimsonRed)
                                    Text(text = "-${FormatUtils.formatRupiah(otherCashOut)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CrimsonRed)
                                }
                            }
                            HorizontalDivider(color = Slate200, thickness = 1.dp)

                            // 1-Pintu Breakdown
                            Text(text = "Rincian Omset 1-Pintu:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Default.Coffee, contentDescription = null, tint = BarCategoryColor, modifier = Modifier.size(14.dp))
                                    Text(text = "BAR", fontSize = 11.sp, color = Slate700)
                                }
                                Text(text = FormatUtils.formatRupiah(barSales), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate900)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Default.FitnessCenter, contentDescription = null, tint = BilliardCategoryColor, modifier = Modifier.size(14.dp))
                                    Text(text = "BILLIARD", fontSize = 11.sp, color = Slate700)
                                }
                                Text(text = FormatUtils.formatRupiah(billiardSales), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate900)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Default.SportsTennis, contentDescription = null, tint = GorCategoryColor, modifier = Modifier.size(14.dp))
                                    Text(text = "GOR", fontSize = 11.sp, color = Slate700)
                                }
                                Text(text = FormatUtils.formatRupiah(gorSales), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Slate900)
                            }

                            if (cancelledTrxs.isNotEmpty()) {
                                HorizontalDivider(color = Slate200, thickness = 1.dp)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Transaksi Dibatalkan / Void (${cancelledTrxs.size})", fontSize = 11.sp, color = CrimsonRed)
                                    Text(text = FormatUtils.formatRupiah(cancelledTrxs.sumOf { it.totalAmount }), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CrimsonRed)
                                }
                            }
                        }
                    }
                }

                // Expected Cash vs Actual Cash in Drawer
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = PrimaryBlueLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Total Kas Seharusnya di Laci", fontSize = 12.sp, color = Slate700)
                                Text(text = "(Modal + Penjualan Tunai - Kas Keluar)", fontSize = 10.sp, color = Slate500)
                            }
                            Text(
                                text = FormatUtils.formatRupiah(expectedCashInDrawer),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                        }
                    }
                }

                // Input Actual Physical Cash
                item {
                    Text(
                        text = "Input Jumlah Kas Fisik Riil di Laci",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    OutlinedTextField(
                        value = actualCashText,
                        onValueChange = { actualCashText = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = Slate900) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                // Selisih Kas Status Indicator
                item {
                    val (diffBg, diffTextColor, diffText) = when {
                        cashDifference == 0.0 -> Triple(EmeraldGreenLight, EmeraldGreen, "Kas Sesuai / Pas (Rp 0)")
                        cashDifference > 0.0 -> Triple(AmberOrangeLight, AmberOrange, "Kelebihan Kas: +${FormatUtils.formatRupiah(cashDifference)}")
                        else -> Triple(CrimsonRedLight, CrimsonRed, "Kekurangan Kas / Selisih: ${FormatUtils.formatRupiah(cashDifference)}")
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = diffBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, diffTextColor.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (cashDifference == 0.0) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = diffTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = diffText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = diffTextColor
                            )
                        }
                    }
                }

                item {
                    Text(text = "Catatan Penutupan Shift", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Catatan operasional saat tutup...", color = Slate400) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            onConfirmClose(actualCash, notes.ifBlank { "Tutup shift normal" })
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Konfirmasi & Tutup Shift", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// 3. MANAJEMEN KAS (PETTY CASH IN/OUT)
@Composable
fun PettyCashDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        type: String, // "DEBIT" (Kas Masuk) or "KREDIT" (Kas Keluar)
        amount: Double,
        category: String,
        description: String,
        businessUnit: String
    ) -> Unit
) {
    var type by remember { mutableStateOf("KREDIT") } // Default to Kas Keluar (e.g. belanja es batu, operasional)
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("OPERASIONAL") }
    var businessUnit by remember { mutableStateOf("BAR") }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(text = "Manajemen Kas (Petty Cash)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(text = "Catat Kas Masuk / Keluar saat shift", fontSize = 12.sp, color = Slate600)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                // Type Toggle (Kas Masuk vs Kas Keluar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Slate100)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (type == "DEBIT") EmeraldGreen else Color.Transparent)
                            .clickable { type = "DEBIT" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ Kas Masuk (Debit)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (type == "DEBIT") White else Slate700
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (type == "KREDIT") CrimsonRed else Color.Transparent)
                            .clickable { type = "KREDIT" }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "- Kas Keluar (Kredit)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (type == "KREDIT") White else Slate700
                        )
                    }
                }

                Text(text = "Nominal", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = Slate900) },
                    placeholder = { Text("0", color = Slate400) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Text(text = "Unit Bisnis", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("BAR", "BILLIARD", "GOR", "UMUM").forEach { unit ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (businessUnit == unit) PrimaryBlue else Slate100)
                                .clickable { businessUnit = unit }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unit,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (businessUnit == unit) White else Slate700
                            )
                        }
                    }
                }

                Text(text = "Keterangan / Keperluan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Contoh: Beli es batu kristal / token listrik", color = Slate400) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                        Text(text = "Batal", color = Slate700)
                    }

                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull() ?: 0.0
                            if (amt > 0) {
                                onConfirm(type, amt, category, description.ifBlank { "Kas Operasional" }, businessUnit)
                            }
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (type == "DEBIT") EmeraldGreen else CrimsonRed),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Simpan Kas", fontWeight = FontWeight.Bold, color = White)
                    }
                }
            }
        }
    }
}

// 4. SHIFT HISTORY DIALOG
@Composable
fun ShiftHistoryDialog(
    shifts: List<ShiftEntity>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp)),
            color = White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(text = "Riwayat Shift Kasir", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(text = "Rekap data operasional per shift", fontSize = 12.sp, color = Slate600)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (shifts.isEmpty()) {
                    Text(text = "Belum ada riwayat shift", color = Slate500, modifier = Modifier.padding(20.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(shifts) { s ->
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                color = Slate100.copy(alpha = 0.6f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Shift #${s.shiftNumber} • ${s.cashierName}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = Slate900
                                            )
                                            Text(
                                                text = "${FormatUtils.formatDate(s.startTime)} (${FormatUtils.formatTime(s.startTime)} - ${if (s.endTime != null) FormatUtils.formatTime(s.endTime) else "Aktif"})",
                                                fontSize = 11.sp,
                                                color = Slate500
                                            )
                                        }

                                        StatusBadge(
                                            text = if (s.status == "OPEN") "AKTIF" else "DITUTUP",
                                            type = if (s.status == "OPEN") StatusType.SUCCESS else StatusType.NEUTRAL
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(color = Slate200, thickness = 1.dp)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text(text = "Modal Awal", fontSize = 11.sp, color = Slate600)
                                        Text(text = FormatUtils.formatRupiah(s.initialCash), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate900)
                                    }

                                    if (s.closingCashActual != null) {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(text = "Kas Fisik Akhir", fontSize = 11.sp, color = Slate600)
                                            Text(text = FormatUtils.formatRupiah(s.closingCashActual), fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlue)
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(text = "Selisih Kas", fontSize = 11.sp, color = Slate600)
                                            val diff = s.cashDifference ?: 0.0
                                            Text(
                                                text = if (diff == 0.0) "Pas (Rp 0)" else FormatUtils.formatRupiah(diff),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (diff >= 0) EmeraldGreen else CrimsonRed
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
}

// 5. VOID / CANCEL TRANSACTIONS DIALOG
@Composable
fun VoidTransactionsDialog(
    transactions: List<TransactionEntity>,
    onDismiss: () -> Unit,
    onVoidTransaction: (Long, String) -> Unit
) {
    var selectedTrxToVoid by remember { mutableStateOf<TransactionEntity?>(null) }
    var voidReason by remember { mutableStateOf("Salah input pesanan / Batal pesanan") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp)),
            color = White,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CrimsonRedLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(text = "Transaksi Dibatalkan & Void", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(text = "Daftar pembatalan di shift ini", fontSize = 12.sp, color = Slate600)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTrxToVoid != null) {
                    // Prompt void confirmation
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CrimsonRedLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Konfirmasi Void Transaksi #${selectedTrxToVoid?.invoiceNumber}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CrimsonRed
                        )
                        Text(
                            text = "Total: ${FormatUtils.formatRupiah(selectedTrxToVoid?.totalAmount ?: 0.0)} (${selectedTrxToVoid?.paymentMethod})",
                            fontSize = 12.sp,
                            color = Slate800
                        )
                        Text(text = "Alasan Pembatalan:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = voidReason,
                            onValueChange = { voidReason = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { selectedTrxToVoid = null },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Batal", fontSize = 12.sp)
                            }
                            Button(
                                onClick = {
                                    selectedTrxToVoid?.let { onVoidTransaction(it.id, voidReason) }
                                    selectedTrxToVoid = null
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Void Transaksi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(transactions) { trx ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (trx.paymentStatus == "CANCELLED") CrimsonRedLight.copy(alpha = 0.4f) else Slate100.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (trx.paymentStatus == "CANCELLED") CrimsonRed.copy(alpha = 0.3f) else Slate200)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = trx.invoiceNumber,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Slate900
                                    )
                                    Text(
                                        text = "${trx.customerName ?: "Umum"} • ${FormatUtils.formatTime(trx.timestamp)} • ${trx.paymentMethod}",
                                        fontSize = 11.sp,
                                        color = Slate600
                                    )
                                    if (trx.paymentStatus == "CANCELLED" && trx.cancelReason != null) {
                                        Text(
                                            text = "Alasan: ${trx.cancelReason}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = CrimsonRed
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = FormatUtils.formatRupiah(trx.totalAmount),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (trx.paymentStatus == "CANCELLED") CrimsonRed else Slate900
                                    )
                                    if (trx.paymentStatus == "PAID") {
                                        OutlinedButton(
                                            onClick = { selectedTrxToVoid = trx },
                                            modifier = Modifier.height(28.dp),
                                            shape = RoundedCornerShape(6.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed)
                                        ) {
                                            Text(text = "Void", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else if (trx.paymentStatus == "CANCELLED") {
                                        StatusBadge(text = "DIBATALKAN", type = StatusType.DANGER)
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
