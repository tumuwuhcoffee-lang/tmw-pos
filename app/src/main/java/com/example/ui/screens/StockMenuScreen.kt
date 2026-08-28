package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.StockInLogEntity
import com.example.ui.components.CategoryBadge
import com.example.ui.components.WhiteCard
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
import com.example.util.FormatUtils

@Composable
fun StockMenuScreen(viewModel: PosViewModel) {
    val context = LocalContext.current
    val allProducts by viewModel.allProducts.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val allStockInLogs by viewModel.allStockInLogs.collectAsState()
    val filteredStockInLogs by viewModel.filteredStockInLogs.collectAsState()
    val stockInFilterMode by viewModel.stockInDateFilterMode.collectAsState()
    val stockInSearchQuery by viewModel.stockInSearchQuery.collectAsState()
    val stockInCategoryFilter by viewModel.stockInCategoryFilter.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Katalog Menu, 1 = Beli Bahan Baku & Riwayat
    var categoryFilter by remember { mutableStateOf("SEMUA") }
    var searchQuery by remember { mutableStateOf("") }

    var productToEdit by remember { mutableStateOf<ProductEntity?>(null) }
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showStockInDialog by remember { mutableStateOf(false) }

    val filteredProducts = remember(allProducts, categoryFilter, searchQuery) {
        allProducts.filter { product ->
            val matchesCategory = if (categoryFilter == "SEMUA") true else product.category.equals(categoryFilter, ignoreCase = true)
            val matchesSearch = if (searchQuery.isBlank()) true else product.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // 1. Header with Title & Tab Switcher
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Stok & Bahan Baku",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Katalog harga jual, HPP, & pengadaan bahan baku",
                            fontSize = 12.sp,
                            color = Slate600
                        )
                    }

                    if (selectedTab == 0) {
                        Button(
                            onClick = { showAddProductDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("add_menu_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Menu Baru", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { showStockInDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("buy_raw_material_button")
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Beli Bahan Baku", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tab Switcher (Katalog Menu vs Riwayat Pembelian Bahan)
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = White,
                    contentColor = PrimaryBlue,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = PrimaryBlue,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "Katalog Menu (${allProducts.size})",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "Riwayat Beli Bahan (${allStockInLogs.size})",
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }
        }

        // 2. Tab Content
        if (selectedTab == 0) {
            // TAB 1: PRODUCT CATALOG
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
            ) {
                // Low Stock Alert Banner (if any)
                if (lowStockProducts.isNotEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = CrimsonRedLight,
                            border = BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = CrimsonRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "${lowStockProducts.size} Produk Stok Menipis (<= 5 Pcs)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                    Text(
                                        text = lowStockProducts.joinToString(", ") { "${it.name} (${it.stock})" },
                                        fontSize = 11.sp,
                                        color = Slate700,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                // Search Bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari nama menu atau produk...", fontSize = 12.sp, color = Slate400) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(18.dp))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = White,
                            unfocusedContainerColor = White,
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Slate200
                        )
                    )
                }

                // Category Filter Chips
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("SEMUA", "BAR", "BILLIARD", "GOR").forEach { cat ->
                            item {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (categoryFilter == cat) PrimaryBlue else Slate100)
                                        .clickable { categoryFilter = cat }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (cat == "SEMUA") "Semua" else cat,
                                        fontSize = 11.sp,
                                        fontWeight = if (categoryFilter == cat) FontWeight.Bold else FontWeight.Medium,
                                        color = if (categoryFilter == cat) White else Slate700
                                    )
                                }
                            }
                        }
                    }
                }

                // Products list
                items(filteredProducts) { product ->
                    WhiteCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    CategoryBadge(category = product.category)
                                    Text(
                                        text = product.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Slate900
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Jual: ${FormatUtils.formatRupiah(product.price)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimaryBlue
                                    )
                                    Text(
                                        text = "• HPP: ${FormatUtils.formatRupiah(product.costPrice)}",
                                        fontSize = 11.sp,
                                        color = Slate500
                                    )
                                    Text(
                                        text = "• Margin: +${FormatUtils.formatRupiah(product.price - product.costPrice)}",
                                        fontSize = 11.sp,
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (product.stock <= 5) CrimsonRedLight else Slate100)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Stok: ${product.stock} ${product.unit}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (product.stock <= 5) CrimsonRed else Slate800
                                    )
                                }

                                IconButton(onClick = { productToEdit = product }) {
                                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Slate600, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // TAB 2: STOCK IN LOGS (RIWAYAT PEMBELIAN BAHAN BAKU DENGAN FILTER TANGGAL SEPERTI DASHBOARD)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
            ) {
                // 1. Date Filter Bar (Hari Ini, Kemarin, 7 Hari, Bulan Ini, Semua)
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = White,
                        shadowElevation = 1.dp
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Filter Periode Pembelian",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Slate800
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(PrimaryBlueLight)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${filteredStockInLogs.size} Pengadaan",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryBlue
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Date filter chips
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    Pair("ALL", "Semua Waktu"),
                                    Pair("TODAY", "Hari Ini"),
                                    Pair("YESTERDAY", "Kemarin"),
                                    Pair("LAST_7_DAYS", "7 Hari"),
                                    Pair("THIS_MONTH", "Bulan Ini")
                                ).forEach { (modeKey, label) ->
                                    item {
                                        val isSel = stockInFilterMode == modeKey
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSel) PrimaryBlue else Slate100)
                                                .clickable { viewModel.setStockInDateFilter(modeKey) }
                                                .padding(horizontal = 10.dp, vertical = 5.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSel) White else Slate700
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Summary KPI Cards for Selected Date Filter
                item {
                    val totalCostInScope = filteredStockInLogs.sumOf { it.totalCost }
                    val totalQtyInScope = filteredStockInLogs.sumOf { it.quantity }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(12.dp),
                            color = EmeraldGreenLight,
                            border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Total Belanja Bahan", fontSize = 11.sp, color = Slate700)
                                Text(
                                    text = FormatUtils.formatRupiah(totalCostInScope),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreen
                                )
                                Text(
                                    text = "Otomatis tercatat kas keluar",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryBlueLight,
                            border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = "Volume Barang", fontSize = 11.sp, color = Slate700)
                                Text(
                                    text = "$totalQtyInScope Unit",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "${filteredStockInLogs.size} Kali Restock",
                                    fontSize = 10.sp,
                                    color = Slate500
                                )
                            }
                        }
                    }
                }

                // 3. Search & Category Filter for History
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = stockInSearchQuery,
                            onValueChange = { viewModel.setStockInSearchQuery(it) },
                            placeholder = { Text("Cari nama bahan / supplier...", fontSize = 11.sp, color = Slate400) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(16.dp))
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = White,
                                unfocusedContainerColor = White,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = Slate200
                            )
                        )
                    }
                }

                // Category Chips
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("SEMUA", "BAR", "BILLIARD", "GOR", "OPERASIONAL").forEach { cat ->
                            item {
                                val isSel = stockInCategoryFilter == cat
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryBlue else Slate100)
                                        .clickable { viewModel.setStockInCategoryFilter(cat) }
                                        .padding(horizontal = 10.dp, vertical = 5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (cat == "SEMUA") "Semua Unit" else cat,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) White else Slate700
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Stock In Logs List
                if (filteredStockInLogs.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            color = White,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Slate200)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, tint = Slate400, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tidak Ada Riwayat Pembelian",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Slate700
                                )
                                Text(
                                    text = "Belum ada data belanja bahan baku pada periode filter ini.",
                                    fontSize = 11.sp,
                                    color = Slate500
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { showStockInDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Input Pembelian Baru", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(filteredStockInLogs) { log ->
                        WhiteCard {
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
                                        CategoryBadge(category = log.category)
                                        Text(
                                            text = log.productName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Slate900
                                        )
                                    }

                                    // Total Cost
                                    Text(
                                        text = FormatUtils.formatRupiah(log.totalCost),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = CrimsonRed
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Details row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Store, contentDescription = null, tint = Slate400, modifier = Modifier.size(13.dp))
                                            Text(
                                                text = "Supplier: ${log.supplierName}",
                                                fontSize = 11.sp,
                                                color = Slate700,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = "${log.quantity} ${log.unit}  @${FormatUtils.formatRupiah(log.unitPrice)} • ${if (log.paymentSource == "KAS_LACI") "Kas Laci" else "Transfer Bank"}",
                                            fontSize = 11.sp,
                                            color = Slate600
                                        )
                                    }

                                    // Print Proof Button
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.printStockInReceipt(log)
                                            Toast.makeText(context, "Mencetak bukti pengadaan via Bluetooth...", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.4f))
                                    ) {
                                        Icon(imageVector = Icons.Default.Print, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Cetak Nota", fontSize = 11.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (!log.notes.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Catatan: ${log.notes}",
                                        fontSize = 10.sp,
                                        color = Slate500,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = log.batchNumber.ifBlank { "PO-${log.id}" },
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Slate400
                                    )
                                    Text(
                                        text = FormatUtils.formatDateTime(log.timestamp),
                                        fontSize = 10.sp,
                                        color = Slate400
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ----------------------------------------------------
    // DIALOGS
    // ----------------------------------------------------
    // Add / Edit Product Dialog
    if (showAddProductDialog || productToEdit != null) {
        ProductDialog(
            product = productToEdit,
            onDismiss = {
                showAddProductDialog = false
                productToEdit = null
            },
            onSave = { updatedProd ->
                if (productToEdit == null) {
                    viewModel.addProduct(updatedProd) {
                        showAddProductDialog = false
                    }
                } else {
                    viewModel.updateProduct(updatedProd) {
                        productToEdit = null
                    }
                }
            },
            onDelete = { prod ->
                viewModel.deleteProduct(prod) {
                    productToEdit = null
                }
            }
        )
    }

    // Comprehensive Stock In / Buy Raw Materials Dialog
    if (showStockInDialog) {
        StockInDialog(
            products = allProducts,
            onDismiss = { showStockInDialog = false },
            onConfirmStockIn = { productId, prodName, cat, supplier, qty, unit, unitPrice, totalCost, paymentSrc, notes, batchNo, addToCatalog, sellPrice ->
                viewModel.recordStockIn(
                    productId = productId,
                    productName = prodName,
                    category = cat,
                    supplierName = supplier,
                    quantity = qty,
                    unit = unit,
                    unitPrice = unitPrice,
                    totalCost = totalCost,
                    paymentSource = paymentSrc,
                    notes = notes,
                    batchNumber = batchNo,
                    addToCatalog = addToCatalog,
                    catalogSellingPrice = sellPrice
                ) {
                    showStockInDialog = false
                    Toast.makeText(context, "Pembelian $prodName berhasil dicatat & masuk stok!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

// ----------------------------------------------------
// PRODUCT ADD / EDIT DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDialog(
    product: ProductEntity?,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit,
    onDelete: ((ProductEntity) -> Unit)? = null
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "BAR") }
    var priceText by remember { mutableStateOf(product?.price?.toLong()?.toString() ?: "15000") }
    var costPriceText by remember { mutableStateOf(product?.costPrice?.toLong()?.toString() ?: "8000") }
    var stockText by remember { mutableStateOf(product?.stock?.toString() ?: "20") }
    var unit by remember { mutableStateOf(product?.unit ?: "Pcs") }

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
                    Text(
                        text = if (product == null) "Tambah Menu Baru" else "Edit Menu & Harga",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                // Category selector
                Text(text = "Unit Kategori", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Slate100)
                        .padding(3.dp)
                ) {
                    listOf("BAR", "BILLIARD", "GOR").forEach { cat ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (category == cat) PrimaryBlue else Color.Transparent)
                                .clickable { category = cat }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cat,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (category == cat) White else Slate700
                            )
                        }
                    }
                }

                // Name
                Text(text = "Nama Menu / Item", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Contoh: Es Kopi Susu Aren", fontSize = 12.sp, color = Slate400) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Price & HPP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Harga Jual (Rp)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "HPP Modal (Rp)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = costPriceText,
                            onValueChange = { costPriceText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }

                // Stock & Unit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Stok Awal", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = stockText,
                            onValueChange = { stockText = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Satuan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = unit,
                            onValueChange = { unit = it },
                            placeholder = { Text("Pcs / Cup / Jam", fontSize = 12.sp) },
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (product != null && onDelete != null) {
                        OutlinedButton(
                            onClick = { onDelete(product) },
                            modifier = Modifier.weight(0.8f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Hapus", modifier = Modifier.size(16.dp))
                        }
                    }

                    Button(
                        onClick = {
                            val price = priceText.toDoubleOrNull() ?: 0.0
                            val cost = costPriceText.toDoubleOrNull() ?: 0.0
                            val stock = stockText.toIntOrNull() ?: 0
                            val newProd = (product ?: ProductEntity(name = name, category = category, price = price)).copy(
                                name = name,
                                category = category,
                                price = price,
                                costPrice = cost,
                                stock = stock,
                                unit = unit
                            )
                            onSave(newProd)
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Simpan Menu", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// COMPREHENSIVE STOCK IN / RAW MATERIAL BUY DIALOG
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockInDialog(
    products: List<ProductEntity>,
    onDismiss: () -> Unit,
    onConfirmStockIn: (
        productId: Long?,
        productName: String,
        category: String,
        supplierName: String,
        quantity: Int,
        unit: String,
        unitPrice: Double,
        totalCost: Double,
        paymentSource: String,
        notes: String?,
        batchNumber: String,
        addToCatalog: Boolean,
        catalogSellingPrice: Double
    ) -> Unit
) {
    // 0 = Input Bebas Bahan Baku Baru (Custom), 1 = Pilih dari Katalog Menu Terdaftar
    var modeTab by remember { mutableStateOf(0) }

    // Custom Raw Material Fields
    var customItemName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("BAR") }
    var customUnit by remember { mutableStateOf("kg") }
    var supplierName by remember { mutableStateOf("Supplier Utama") }
    var quantityText by remember { mutableStateOf("5") }
    var unitPriceText by remember { mutableStateOf("50000") }
    var paymentSource by remember { mutableStateOf("KAS_LACI") } // KAS_LACI or BANK_TRANSFER
    var notes by remember { mutableStateOf("Pengadaan bahan baku operasional") }
    var addToCatalog by remember { mutableStateOf(false) }
    var sellingPriceText by remember { mutableStateOf("75000") }

    // Existing Product Selector
    var selectedProduct by remember { mutableStateOf(products.firstOrNull()) }

    val qty = quantityText.toIntOrNull() ?: 0
    val unitPrice = unitPriceText.toDoubleOrNull() ?: 0.0
    val totalCost = qty * unitPrice
    val sellingPrice = sellingPriceText.toDoubleOrNull() ?: 0.0

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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(text = "Beli Bahan Baku / Pengadaan", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text(text = "Pendataan mandiri barang masuk & auto kas keluar", fontSize = 11.sp, color = Slate600)
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                        }
                    }
                }

                // Mode Tabs (Input Bebas vs Pilih Menu)
                item {
                    TabRow(
                        selectedTabIndex = modeTab,
                        containerColor = Slate100,
                        contentColor = PrimaryBlue,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[modeTab]),
                                color = PrimaryBlue,
                                height = 3.dp
                            )
                        },
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        Tab(
                            selected = modeTab == 0,
                            onClick = { modeTab = 0 },
                            text = {
                                Text(
                                    text = "Bahan Baku Bebas (Baru)",
                                    fontSize = 11.sp,
                                    fontWeight = if (modeTab == 0) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                        Tab(
                            selected = modeTab == 1,
                            onClick = { modeTab = 1 },
                            text = {
                                Text(
                                    text = "Restock Menu yang Ada",
                                    fontSize = 11.sp,
                                    fontWeight = if (modeTab == 1) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }

                if (modeTab == 0) {
                    // MODE 0: INPUT BEBAS BAHAN BAKU BARU
                    item {
                        Text(text = "Nama Bahan Baku / Barang Masuk", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = customItemName,
                            onValueChange = { customItemName = it },
                            placeholder = { Text("Contoh: Biji Kopi Arabika Gayo 1kg / Susu UHT 1L", fontSize = 12.sp, color = Slate400) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }

                    // Category Selector
                    item {
                        Text(text = "Alokasi Unit Usaha", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Slate100)
                                .padding(3.dp)
                        ) {
                            listOf("BAR", "BILLIARD", "GOR", "OPERASIONAL").forEach { cat ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (selectedCategory == cat) PrimaryBlue else Color.Transparent)
                                        .clickable { selectedCategory = cat }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedCategory == cat) White else Slate700
                                    )
                                }
                            }
                        }
                    }

                    // Satuan Unit Selector
                    item {
                        Text(text = "Satuan Barang Masuk", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("kg", "liter", "pack", "dus", "pcs", "ikat", "botol", "kaleng", "roll").forEach { u ->
                                item {
                                    val isSel = customUnit.equals(u, ignoreCase = true)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSel) PrimaryBlue else Slate100)
                                            .clickable { customUnit = u }
                                            .padding(horizontal = 10.dp, vertical = 5.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = u,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSel) White else Slate700
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // MODE 1: PILIH DARI PRODUK YANG SUDAH ADA
                    item {
                        Text(text = "Pilih Produk dari Katalog", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(products) { prod ->
                                val isSel = selectedProduct?.id == prod.id
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSel) PrimaryBlue else Slate100)
                                        .clickable {
                                            selectedProduct = prod
                                            unitPriceText = prod.costPrice.toLong().toString()
                                            selectedCategory = prod.category
                                            customUnit = prod.unit
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = prod.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSel) White else Slate800
                                    )
                                }
                            }
                        }
                    }
                }

                // Supplier Name
                item {
                    Text(text = "Nama Supplier / Toko Pembelian", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = supplierName,
                        onValueChange = { supplierName = it },
                        placeholder = { Text("Contoh: Toko Kopi Jaya / Distributor Sembako", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Quantity & Unit Price
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Jumlah Beli (Qty)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                            OutlinedTextField(
                                value = quantityText,
                                onValueChange = { quantityText = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Harga Satuan (Rp)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                            OutlinedTextField(
                                value = unitPriceText,
                                onValueChange = { unitPriceText = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                        }
                    }
                }

                // Payment Source
                item {
                    Text(text = "Sumber Dana Pembayaran", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Slate100)
                            .padding(3.dp)
                    ) {
                        listOf(
                            Pair("KAS_LACI", "Kas Laci (Cash)"),
                            Pair("BANK_TRANSFER", "Rekening Bank")
                        ).forEach { (source, label) ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (paymentSource == source) PrimaryBlue else Color.Transparent)
                                    .clickable { paymentSource = source }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (paymentSource == source) White else Slate700
                                )
                            }
                        }
                    }
                }

                // Notes
                item {
                    Text(text = "Catatan / Keterangan Pembelian", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Option: Also add to Sales Catalog (Only in mode 0)
                if (modeTab == 0) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Slate50,
                            border = BorderStroke(1.dp, Slate200)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = addToCatalog,
                                        onCheckedChange = { addToCatalog = it },
                                        colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tambahkan juga sebagai Menu Jual di POS",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate800
                                    )
                                }

                                if (addToCatalog) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Harga Jual Menu (Rp)", fontSize = 11.sp, color = Slate700)
                                    OutlinedTextField(
                                        value = sellingPriceText,
                                        onValueChange = { sellingPriceText = it },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }
                }

                // Total Cost Banner
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldGreenLight,
                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Total Biaya Pengadaan:", fontSize = 11.sp, color = Slate700)
                                Text(
                                    text = "$qty ${if (modeTab == 0) customUnit else (selectedProduct?.unit ?: customUnit)} @${FormatUtils.formatRupiah(unitPrice)}",
                                    fontSize = 11.sp,
                                    color = Slate600
                                )
                            }
                            Text(
                                text = FormatUtils.formatRupiah(totalCost),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }

                // Confirm Button
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            val finalItemName = if (modeTab == 0) customItemName.ifBlank { "Bahan Baku #${System.currentTimeMillis() % 1000}" } else (selectedProduct?.name ?: "Barang")
                            val finalCategory = if (modeTab == 0) selectedCategory else (selectedProduct?.category ?: "BAR")
                            val finalUnit = if (modeTab == 0) customUnit else (selectedProduct?.unit ?: "Pcs")
                            val finalProductId = if (modeTab == 0) null else selectedProduct?.id
                            val batchNumber = "PO-RAW-${System.currentTimeMillis() % 1000000}"

                            onConfirmStockIn(
                                finalProductId,
                                finalItemName,
                                finalCategory,
                                supplierName,
                                qty,
                                finalUnit,
                                unitPrice,
                                totalCost,
                                paymentSource,
                                notes,
                                batchNumber,
                                addToCatalog && modeTab == 0,
                                sellingPrice
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("confirm_stock_in_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Konfirmasi Beli & Catat Kas Keluar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
