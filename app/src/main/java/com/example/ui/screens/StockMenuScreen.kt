package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    val allProducts by viewModel.allProducts.collectAsState()
    val lowStockProducts by viewModel.lowStockProducts.collectAsState()
    val stockInLogs by viewModel.allStockInLogs.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0 = Katalog Produk, 1 = Pembelian Bahan Baku (Restock)
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
        // 1. Header
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
                            text = "Manajemen Stok & Menu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )
                        Text(
                            text = "Katalog harga jual, HPP modal & restock bahan",
                            fontSize = 12.sp,
                            color = Slate600
                        )
                    }

                    if (selectedTab == 0) {
                        Button(
                            onClick = { showAddProductDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
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
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Beli Bahan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tab Switcher (Katalog Menu vs Pembelian Bahan)
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
                                text = "Beli Bahan & Restock (${stockInLogs.size})",
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
                            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.3f))
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
                                        text = lowStockProducts.joinToString(", ") { it.name },
                                        fontSize = 11.sp,
                                        color = Slate700,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                // Search & Filter
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

                // Category Chips
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
                                // Stock Indicator Badge
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (product.stock <= 5) CrimsonRedLight else Slate100)
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Stok: ${product.stock}",
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
            // TAB 2: STOCK IN LOGS (PEMBELIAN BAHAN BAKU)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
            ) {
                item {
                    // Restock Summary Info Banner
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldGreenLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.25f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Total Pengadaan Stok", fontSize = 11.sp, color = Slate700)
                                Text(
                                    text = FormatUtils.formatRupiah(stockInLogs.sumOf { it.totalCost }),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreen
                                )
                            }

                            Button(
                                onClick = { showStockInDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Restock", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                if (stockInLogs.isEmpty()) {
                    item {
                        Text(text = "Belum ada riwayat pembelian bahan baku", color = Slate500, modifier = Modifier.padding(20.dp))
                    }
                } else {
                    items(stockInLogs) { log ->
                        WhiteCard {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = log.productName,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Slate900
                                        )
                                        Text(
                                            text = "Supplier: ${log.supplierName} • ${FormatUtils.formatDateTime(log.timestamp)}",
                                            fontSize = 11.sp,
                                            color = Slate500
                                        )
                                    }

                                    Text(
                                        text = FormatUtils.formatRupiah(log.totalCost),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                HorizontalDivider(color = Slate200, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        CategoryBadge(category = log.category)
                                        Text(
                                            text = "+${log.quantity} Pcs (@${FormatUtils.formatRupiah(log.unitPrice)})",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Slate700
                                        )
                                    }

                                    Text(
                                        text = "Bayar: ${if (log.paymentSource == "KAS_LACI") "Kas Laci" else "Transfer Bank"}",
                                        fontSize = 11.sp,
                                        color = Slate600
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        AddEditProductDialog(
            product = null,
            onDismiss = { showAddProductDialog = false },
            onSave = { prod ->
                viewModel.addProduct(prod) { showAddProductDialog = false }
            },
            onDelete = null
        )
    }

    // Edit Product Dialog
    productToEdit?.let { prod ->
        AddEditProductDialog(
            product = prod,
            onDismiss = { productToEdit = null },
            onSave = { updated ->
                viewModel.updateProduct(updated) { productToEdit = null }
            },
            onDelete = {
                viewModel.deleteProduct(prod) { productToEdit = null }
            }
        )
    }

    // Stock In / Restock Dialog
    if (showStockInDialog) {
        StockInDialog(
            products = allProducts,
            onDismiss = { showStockInDialog = false },
            onConfirmStockIn = { productId, prodName, cat, supplier, qty, unitPrice, totalCost, source, notes ->
                viewModel.recordStockIn(productId, prodName, cat, supplier, qty, unitPrice, totalCost, source, notes) {
                    showStockInDialog = false
                }
            }
        )
    }
}

// ----------------------------------------------------
// DIALOGS FOR STOCK & MENU
// ----------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditProductDialog(
    product: ProductEntity?,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit,
    onDelete: (() -> Unit)?
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "BAR") }
    var priceText by remember { mutableStateOf(if (product != null) product.price.toLong().toString() else "") }
    var costPriceText by remember { mutableStateOf(if (product != null) product.costPrice.toLong().toString() else "") }
    var stockText by remember { mutableStateOf(if (product != null) product.stock.toString() else "50") }

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
                    Text(
                        text = if (product == null) "Tambah Menu Baru" else "Edit Menu",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                Text(text = "Nama Menu / Item", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Contoh: Kopi Susu Aren / Meja Billiard", color = Slate400) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Text(text = "Kategori Pintu", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("BAR", "BILLIARD", "GOR").forEach { cat ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (category == cat) PrimaryBlue else Slate100)
                                .clickable { category = cat }
                                .padding(vertical = 8.dp),
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Harga Jual (Rp)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            placeholder = { Text("0", color = Slate400) },
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
                            placeholder = { Text("0", color = Slate400) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }

                Text(text = "Stok Awal (Pcs)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it },
                    placeholder = { Text("0", color = Slate400) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (onDelete != null) {
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CrimsonRedLight)
                        ) {
                            Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = CrimsonRed)
                        }
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Batal", color = Slate700)
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
                                stock = stock
                            )
                            onSave(newProd)
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Simpan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockInDialog(
    products: List<ProductEntity>,
    onDismiss: () -> Unit,
    onConfirmStockIn: (
        productId: Long,
        productName: String,
        category: String,
        supplierName: String,
        quantity: Int,
        unitPrice: Double,
        totalCost: Double,
        paymentSource: String,
        notes: String
    ) -> Unit
) {
    var selectedProduct by remember { mutableStateOf(products.firstOrNull()) }
    var supplierName by remember { mutableStateOf("Supplier Langganan") }
    var quantityText by remember { mutableStateOf("10") }
    var unitPriceText by remember {
        mutableStateOf(selectedProduct?.costPrice?.toLong()?.toString() ?: "10000")
    }
    var paymentSource by remember { mutableStateOf("KAS_LACI") } // KAS_LACI or BANK_TRANSFER
    var notes by remember { mutableStateOf("Restock bahan baku operasional") }

    val qty = quantityText.toIntOrNull() ?: 0
    val unitPrice = unitPriceText.toDoubleOrNull() ?: 0.0
    val totalCost = qty * unitPrice

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
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingCartCheckout, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(text = "Pembelian Bahan Baku", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text(text = "Auto restock & catat kas keluar", fontSize = 11.sp, color = Slate600)
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                        }
                    }
                }

                item {
                    Text(text = "Pilih Produk / Menu Bahan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
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

                item {
                    Text(text = "Nama Supplier / Toko", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    OutlinedTextField(
                        value = supplierName,
                        onValueChange = { supplierName = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

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
                            Text(text = "Harga Beli Satuan (Rp)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
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

                // Total Cost Banner
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldGreenLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Total Biaya Belanja:", fontSize = 12.sp, color = Slate700)
                            Text(
                                text = FormatUtils.formatRupiah(totalCost),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            selectedProduct?.let { prod ->
                                onConfirmStockIn(
                                    prod.id,
                                    prod.name,
                                    prod.category,
                                    supplierName,
                                    qty,
                                    unitPrice,
                                    totalCost,
                                    paymentSource,
                                    notes
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Konfirmasi Pembelian & Masuk Stok", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
