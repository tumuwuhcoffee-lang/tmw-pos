package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.CashflowEntity
import com.example.ui.components.CategoryBadge
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.components.StatusType
import com.example.ui.components.WhiteCard
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.AmberOrangeLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CrimsonRedLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
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
import com.example.ui.viewmodel.PosViewModel
import com.example.util.FormatUtils

@Composable
fun CashflowScreen(viewModel: PosViewModel) {
    val allCashflows by viewModel.allCashflows.collectAsState()
    val totalBalance by viewModel.totalRealCashBalance.collectAsState()
    val totalCashIn by viewModel.totalCashIn.collectAsState()
    val totalCashOut by viewModel.totalCashOut.collectAsState()

    var typeFilter by remember { mutableStateOf("ALL") } // ALL, DEBIT, KREDIT
    var unitFilter by remember { mutableStateOf("ALL") } // ALL, BAR, BILLIARD, GOR, UMUM
    var showAddCashflowDialog by remember { mutableStateOf(false) }

    val filteredCashflows = remember(allCashflows, typeFilter, unitFilter) {
        allCashflows.filter { cf ->
            val matchesType = when (typeFilter) {
                "DEBIT" -> cf.type == "DEBIT"
                "KREDIT" -> cf.type == "KREDIT"
                else -> true
            }
            val matchesUnit = when (unitFilter) {
                "ALL" -> true
                else -> cf.businessUnit.equals(unitFilter, ignoreCase = true)
            }
            matchesType && matchesUnit
        }.sortedByDescending { it.timestamp }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
    ) {
        // 1. Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Arus Kas (Cashflow)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    Text(
                        text = "Buku kas riil, debit/kredit & pencatatan operasional",
                        fontSize = 12.sp,
                        color = Slate600
                    )
                }

                Button(
                    onClick = { showAddCashflowDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Catat Kas", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 2. Real-time Cash Balance Hero Card
        item {
            WhiteCard(
                borderColor = PrimaryBlue.copy(alpha = 0.3f),
                elevation = 1.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "SALDO KAS RIIL SAAT INI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Slate600
                            )
                            Text(
                                text = "Total Net Kas Bersih",
                                fontSize = 12.sp,
                                color = Slate500
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = FormatUtils.formatRupiah(totalBalance),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (totalBalance >= 0) Slate900 else CrimsonRed
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Slate200, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Total In (Debit)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Column {
                                Text(text = "Total Kas Masuk", fontSize = 10.sp, color = Slate500)
                                Text(
                                    text = "+${FormatUtils.formatRupiah(totalCashIn)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreen
                                )
                            }
                        }

                        // Total Out (Kredit)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonRedLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = CrimsonRed,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Column {
                                Text(text = "Total Kas Keluar", fontSize = 10.sp, color = Slate500)
                                Text(
                                    text = "-${FormatUtils.formatRupiah(totalCashOut)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonRed
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Filter Chips (Debit vs Kredit & Unit)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Type Filter: Semua, Kas Masuk, Kas Keluar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CashflowFilterChip(
                        label = "Semua Mutasi (${allCashflows.size})",
                        isSelected = typeFilter == "ALL",
                        onClick = { typeFilter = "ALL" }
                    )
                    CashflowFilterChip(
                        label = "+ Kas Masuk",
                        isSelected = typeFilter == "DEBIT",
                        activeColor = EmeraldGreen,
                        onClick = { typeFilter = "DEBIT" }
                    )
                    CashflowFilterChip(
                        label = "- Kas Keluar",
                        isSelected = typeFilter == "KREDIT",
                        activeColor = CrimsonRed,
                        onClick = { typeFilter = "KREDIT" }
                    )
                }

                // Unit Filter: Semua, Bar, Billiard, GOR, Umum
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("ALL", "BAR", "BILLIARD", "GOR", "UMUM").forEach { u ->
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (unitFilter == u) PrimaryBlue else Slate100)
                                    .clickable { unitFilter = u }
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (u == "ALL") "Semua Unit" else u,
                                    fontSize = 11.sp,
                                    fontWeight = if (unitFilter == u) FontWeight.Bold else FontWeight.Medium,
                                    color = if (unitFilter == u) White else Slate700
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Cashflow Ledger List
        if (filteredCashflows.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Tidak ada catatan arus kas.", color = Slate500, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredCashflows) { cf ->
                val isDebit = cf.type == "DEBIT"
                WhiteCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isDebit) EmeraldGreenLight else CrimsonRedLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isDebit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = if (isDebit) EmeraldGreen else CrimsonRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = cf.description,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Slate900
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    CategoryBadge(category = cf.businessUnit)
                                    Text(
                                        text = "${FormatUtils.formatDateTime(cf.timestamp)} • ${cf.paymentMethod}",
                                        fontSize = 11.sp,
                                        color = Slate500
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (isDebit) "+${FormatUtils.formatRupiah(cf.amount)}" else "-${FormatUtils.formatRupiah(cf.amount)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDebit) EmeraldGreen else CrimsonRed
                        )
                    }
                }
            }
        }
    }

    // Add Cashflow Dialog
    if (showAddCashflowDialog) {
        AddCashflowModal(
            onDismiss = { showAddCashflowDialog = false },
            onConfirm = { type, category, businessUnit, amount, paymentMethod, desc ->
                viewModel.addPettyCash(
                    type = type,
                    amount = amount,
                    category = category,
                    description = desc,
                    businessUnit = businessUnit,
                    paymentMethod = paymentMethod
                ) {
                    showAddCashflowDialog = false
                }
            }
        )
    }
}

// ----------------------------------------------------
// SUB-COMPONENTS FOR CASHFLOW SCREEN
// ----------------------------------------------------

@Composable
private fun CashflowFilterChip(
    label: String,
    isSelected: Boolean,
    activeColor: Color = PrimaryBlue,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) activeColor else Slate100)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) White else Slate700
        )
    }
}

@Composable
private fun AddCashflowModal(
    onDismiss: () -> Unit,
    onConfirm: (
        type: String,
        category: String,
        businessUnit: String,
        amount: Double,
        paymentMethod: String,
        description: String
    ) -> Unit
) {
    var type by remember { mutableStateOf("DEBIT") } // DEBIT (Kas Masuk) or KREDIT (Kas Keluar)
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("OPERASIONAL") }
    var businessUnit by remember { mutableStateOf("UMUM") }
    var paymentMethod by remember { mutableStateOf("CASH") } // CASH or BANK
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Catat Arus Kas Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                // Type Toggle (Kas Masuk vs Kas Keluar)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate100)
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
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
                            .clip(RoundedCornerShape(6.dp))
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

                Text(text = "Nominal (Rp)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("0", color = Slate400) },
                    prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = Slate900) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Text(text = "Kategori", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("OPERASIONAL", "BELANJA_STOK", "GAJI", "MODAL_AWAL", "LAINNYA").forEach { cat ->
                        item {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (category == cat) PrimaryBlue else Slate100)
                                    .clickable { category = cat }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (category == cat) FontWeight.Bold else FontWeight.Medium,
                                    color = if (category == cat) White else Slate700
                                )
                            }
                        }
                    }
                }

                Text(text = "Unit Bisnis", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("BAR", "BILLIARD", "GOR", "UMUM").forEach { u ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (businessUnit == u) PrimaryBlue else Slate100)
                                .clickable { businessUnit = u }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = u,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (businessUnit == u) White else Slate700
                            )
                        }
                    }
                }

                Text(text = "Metode Pembayaran", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        Pair("CASH", "Tunai / Kas Laci"),
                        Pair("BANK", "Transfer Bank / QRIS")
                    ).forEach { (m, label) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (paymentMethod == m) PrimaryBlue else Slate100)
                                .clickable { paymentMethod = m }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (paymentMethod == m) White else Slate700
                            )
                        }
                    }
                }

                Text(text = "Keterangan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Contoh: Pembayaran listrik bulanan", color = Slate400) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)) {
                        Text(text = "Batal", color = Slate700)
                    }

                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull() ?: 0.0
                            if (amt > 0) {
                                onConfirm(
                                    type,
                                    category,
                                    businessUnit,
                                    amt,
                                    paymentMethod,
                                    description.ifBlank { "Transaksi Kas" }
                                )
                            }
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (type == "DEBIT") EmeraldGreen else CrimsonRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Simpan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
