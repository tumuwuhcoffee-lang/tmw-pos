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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsTennis
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ShiftEntity
import com.example.data.local.entity.TransactionEntity
import com.example.ui.components.CategoryBadge
import com.example.ui.components.CloseShiftDialog
import com.example.ui.components.HeldBillsDrawerDialog
import com.example.ui.components.OpenShiftDialog
import com.example.ui.components.PettyCashDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.components.SaveHoldBillDialog
import com.example.ui.components.ShiftHistoryDialog
import com.example.ui.components.StatusBadge
import com.example.ui.components.StatusType
import com.example.ui.components.VoidTransactionsDialog
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
import com.example.ui.viewmodel.CartItem
import com.example.ui.viewmodel.PosViewModel
import com.example.util.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(viewModel: PosViewModel) {
    val activeShift by viewModel.activeShift.collectAsState()
    val allShifts by viewModel.allShifts.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val categoryFilter by viewModel.posCategoryFilter.collectAsState()
    val searchQuery by viewModel.posSearchQuery.collectAsState()

    val cartItems by viewModel.cartItems.collectAsState()
    val customerName by viewModel.customerName.collectAsState()
    val tableOrRef by viewModel.tableOrRef.collectAsState()
    val orderType by viewModel.orderType.collectAsState()
    val taxRate by viewModel.taxRatePercent.collectAsState()
    val discountAmount by viewModel.discountAmount.collectAsState()

    val heldBills by viewModel.heldTransactions.collectAsState()
    val allTransactions by viewModel.allTransactions.collectAsState()
    val allCashflows by viewModel.allCashflows.collectAsState()

    val lastReceiptTrx by viewModel.lastCompletedTransaction.collectAsState()
    val lastReceiptItems by viewModel.lastCompletedItems.collectAsState()

    // Dialog & Sheet States
    var showOpenShiftDialog by remember { mutableStateOf(false) }
    var showCloseShiftDialog by remember { mutableStateOf(false) }
    var showPettyCashDialog by remember { mutableStateOf(false) }
    var showShiftHistoryDialog by remember { mutableStateOf(false) }
    var showVoidDialog by remember { mutableStateOf(false) }
    var showSaveHoldDialog by remember { mutableStateOf(false) }
    var showHeldBillsDialog by remember { mutableStateOf(false) }
    var showPaymentSheet by remember { mutableStateOf(false) }
    var showCartSheet by remember { mutableStateOf(false) }
    var showMoreMenu by remember { mutableStateOf(false) }

    // Direct payment for a held bill
    var directPayBillTarget by remember { mutableStateOf<TransactionEntity?>(null) }

    // Filter products
    val filteredProducts = remember(allProducts, categoryFilter, searchQuery) {
        allProducts.filter { product ->
            val matchesCategory = when (categoryFilter) {
                "SEMUA" -> true
                else -> product.category.equals(categoryFilter, ignoreCase = true)
            }
            val matchesQuery = if (searchQuery.isBlank()) true
            else product.name.contains(searchQuery, ignoreCase = true) || product.category.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Slate50)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. TOP APP BAR / SHIFT BAR
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Shift info / Status
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (activeShift != null) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldGreenLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "Shift #${activeShift?.shiftNumber}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Slate900
                                        )
                                        StatusBadge(text = "AKTIF", type = StatusType.SUCCESS)
                                    }
                                    Text(
                                        text = "${activeShift?.cashierName} • Kas: ${FormatUtils.formatRupiah(activeShift?.initialCash ?: 0.0)}",
                                        fontSize = 11.sp,
                                        color = Slate600
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(CrimsonRedLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = CrimsonRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "Shift Belum Dibuka",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                    Text(
                                        text = "Buka shift untuk mulai kasir",
                                        fontSize = 11.sp,
                                        color = Slate600
                                    )
                                }
                            }
                        }

                        // Right Action Buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Held Bills button with Badge
                            if (heldBills.isNotEmpty()) {
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { showHeldBillsDialog = true },
                                    color = AmberOrangeLight,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, AmberOrange.copy(alpha = 0.4f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.BookmarkBorder,
                                            contentDescription = null,
                                            tint = AmberOrange,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "${heldBills.size} Open Bill",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberOrange
                                        )
                                    }
                                }
                            }

                            if (activeShift != null) {
                                IconButton(onClick = { showPettyCashDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = "Petty Cash",
                                        tint = PrimaryBlue
                                    )
                                }
                                IconButton(onClick = { showCloseShiftDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Tutup Shift",
                                        tint = CrimsonRed
                                    )
                                }
                            } else {
                                Button(
                                    onClick = { showOpenShiftDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Buka Shift", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Box {
                                IconButton(onClick = { showMoreMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Menu Lainnya",
                                        tint = Slate700
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMoreMenu,
                                    onDismissRequest = { showMoreMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Riwayat Shift") },
                                        onClick = {
                                            showMoreMenu = false
                                            showShiftHistoryDialog = true
                                        },
                                        leadingIcon = { Icon(Icons.Default.History, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Transaksi Batal / Void") },
                                        onClick = {
                                            showMoreMenu = false
                                            showVoidDialog = true
                                        },
                                        leadingIcon = { Icon(Icons.Default.Cancel, contentDescription = null, tint = CrimsonRed) }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. SEARCH BAR & CATEGORY FILTER
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setPosSearchQuery(it) },
                        placeholder = { Text("Cari produk atau menu...", color = Slate400, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Slate400, modifier = Modifier.size(20.dp))
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setPosSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Slate500, modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Slate100,
                            unfocusedContainerColor = Slate100,
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category Chips: SEMUA, BAR, BILLIARD, GOR
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            CategoryFilterChip(
                                label = "Semua Menu",
                                isSelected = categoryFilter == "SEMUA",
                                onClick = { viewModel.setPosCategoryFilter("SEMUA") }
                            )
                        }
                        item {
                            CategoryFilterChip(
                                label = "Bar ☕",
                                isSelected = categoryFilter == "BAR",
                                activeColor = BarCategoryColor,
                                onClick = { viewModel.setPosCategoryFilter("BAR") }
                            )
                        }
                        item {
                            CategoryFilterChip(
                                label = "Billiard 🎱",
                                isSelected = categoryFilter == "BILLIARD",
                                activeColor = BilliardCategoryColor,
                                onClick = { viewModel.setPosCategoryFilter("BILLIARD") }
                            )
                        }
                        item {
                            CategoryFilterChip(
                                label = "GOR 🏸",
                                isSelected = categoryFilter == "GOR",
                                activeColor = GorCategoryColor,
                                onClick = { viewModel.setPosCategoryFilter("GOR") }
                            )
                        }
                    }
                }
            }

            // 3. PRODUCT CATALOG GRID
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada produk ditemukan",
                            color = Slate500,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 120.dp, top = 8.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductCard(
                                product = product,
                                onAddToCart = {
                                    if (activeShift == null) {
                                        showOpenShiftDialog = true
                                    } else {
                                        viewModel.addToCart(product)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // 4. FLOATING BOTTOM CART BAR & "SIMPAN (BAYAR NANTI)" BUTTON
        if (cartItems.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                color = Slate900,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Cart Summary & Click to open cart sheet
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showCartSheet = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = PrimaryBlue) {
                                    Text(text = "${cartItems.sumOf { it.quantity }}", color = White)
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(Slate800),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Keranjang",
                                    tint = White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Total Pesanan",
                                fontSize = 11.sp,
                                color = Slate400
                            )
                            Text(
                                text = FormatUtils.formatRupiah(viewModel.cartTotalAmount),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }

                    // Right Buttons: "Simpan (Bayar Nanti)" & "Bayar"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // User specific requirement:
                        // "simpan saja di pojok kanan dekat keranjang di kasir pos"
                        Button(
                            onClick = { showSaveHoldDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Simpan",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }

                        Button(
                            onClick = { showPaymentSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Bayar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                }
            }
        }
    }

    // ----------------------------------------------------
    // BOTTOM SHEETS & MODALS
    // ----------------------------------------------------

    // 1. Full Cart Drawer Bottom Sheet
    if (showCartSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCartSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = White
        ) {
            CartSheetContent(
                cartItems = cartItems,
                customerName = customerName,
                tableOrRef = tableOrRef,
                orderType = orderType,
                taxRate = taxRate,
                discountAmount = discountAmount,
                subtotal = viewModel.cartSubtotal,
                taxAmount = viewModel.cartTaxAmount,
                totalAmount = viewModel.cartTotalAmount,
                onCustomerNameChange = { viewModel.setCustomerName(it) },
                onTableOrRefChange = { viewModel.setTableOrRef(it) },
                onOrderTypeChange = { viewModel.setOrderType(it) },
                onTaxRateChange = { viewModel.setTaxRatePercent(it) },
                onDiscountChange = { viewModel.setDiscountAmount(it) },
                onUpdateQty = { id, delta -> viewModel.updateCartItemQuantity(id, delta) },
                onRemoveItem = { id -> viewModel.removeCartItem(id) },
                onClearCart = { viewModel.clearCart() },
                onHoldCart = {
                    showCartSheet = false
                    showSaveHoldDialog = true
                },
                onProceedPay = {
                    showCartSheet = false
                    showPaymentSheet = true
                }
            )
        }
    }

    // 2. Checkout Payment Sheet
    if (showPaymentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaymentSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = White
        ) {
            PaymentSheetContent(
                totalAmount = viewModel.cartTotalAmount,
                onDismiss = { showPaymentSheet = false },
                onConfirmPayment = { method, tendered ->
                    viewModel.processPayment(method, tendered) {
                        showPaymentSheet = false
                    }
                }
            )
        }
    }

    // 3. Direct Pay for Held Bill Dialog
    directPayBillTarget?.let { heldBill ->
        ModalBottomSheet(
            onDismissRequest = { directPayBillTarget = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = White
        ) {
            PaymentSheetContent(
                totalAmount = heldBill.totalAmount,
                customerTitle = "${heldBill.customerName} (${heldBill.tableOrOrderRef})",
                onDismiss = { directPayBillTarget = null },
                onConfirmPayment = { method, tendered ->
                    val change = (tendered - heldBill.totalAmount).coerceAtLeast(0.0)
                    viewModel.payHeldBillDirectly(heldBill, method, tendered, change) {
                        directPayBillTarget = null
                    }
                }
            )
        }
    }

    // 4. Save Hold Bill Dialog
    if (showSaveHoldDialog) {
        SaveHoldBillDialog(
            initialCustomerName = customerName,
            initialTableRef = tableOrRef,
            totalAmount = viewModel.cartTotalAmount,
            itemCount = cartItems.sumOf { it.quantity },
            onDismiss = { showSaveHoldDialog = false },
            onConfirmHold = { name, table ->
                viewModel.holdOrder(name, table) {
                    showSaveHoldDialog = false
                }
            }
        )
    }

    // 5. Held Bills Drawer Dialog
    if (showHeldBillsDialog) {
        HeldBillsDrawerDialog(
            heldBills = heldBills,
            onDismiss = { showHeldBillsDialog = false },
            onResumeBill = { bill -> viewModel.resumeHeldBill(bill) },
            onDirectPayBill = { bill -> directPayBillTarget = bill },
            onDeleteBill = { id -> viewModel.deleteHeldBill(id) }
        )
    }

    // 6. Open Shift Dialog
    if (showOpenShiftDialog) {
        OpenShiftDialog(
            onDismiss = { showOpenShiftDialog = false },
            onConfirmOpen = { name, initial, bar, bil, gor, notes ->
                viewModel.openShift(name, initial, bar, bil, gor, notes) {
                    showOpenShiftDialog = false
                }
            }
        )
    }

    // 7. Close Shift Dialog
    if (showCloseShiftDialog && activeShift != null) {
        CloseShiftDialog(
            shift = activeShift!!,
            shiftTransactions = allTransactions.filter { it.shiftId == activeShift?.id },
            shiftCashflows = allCashflows.filter { it.shiftId == activeShift?.id },
            onDismiss = { showCloseShiftDialog = false },
            onConfirmClose = { actual, notes ->
                viewModel.closeShift(activeShift!!, actual, notes) {
                    showCloseShiftDialog = false
                }
            }
        )
    }

    // 8. Petty Cash Dialog
    if (showPettyCashDialog) {
        PettyCashDialog(
            onDismiss = { showPettyCashDialog = false },
            onConfirm = { type, amount, cat, desc, unit ->
                viewModel.addPettyCash(type, amount, cat, desc, unit) {
                    showPettyCashDialog = false
                }
            }
        )
    }

    // 9. Shift History Dialog
    if (showShiftHistoryDialog) {
        ShiftHistoryDialog(
            shifts = allShifts,
            onDismiss = { showShiftHistoryDialog = false }
        )
    }

    // 10. Void Transactions Dialog
    if (showVoidDialog) {
        val currentShiftTrxs = allTransactions.filter { it.shiftId == activeShift?.id }
        VoidTransactionsDialog(
            transactions = currentShiftTrxs,
            onDismiss = { showVoidDialog = false },
            onVoidTransaction = { trxId, reason ->
                viewModel.voidTransaction(trxId, reason) {}
            }
        )
    }

    // 11. Receipt Dialog (When transaction finishes)
    if (lastReceiptTrx != null) {
        ReceiptDialog(
            transaction = lastReceiptTrx!!,
            items = lastReceiptItems,
            onDismiss = { viewModel.dismissReceipt() },
            onNewTransaction = { viewModel.dismissReceipt() }
        )
    }
}

// ----------------------------------------------------
// SUB-COMPONENTS FOR POS SCREEN
// ----------------------------------------------------

@Composable
private fun CategoryFilterChip(
    label: String,
    isSelected: Boolean,
    activeColor: Color = PrimaryBlue,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) activeColor else Slate100)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) White else Slate700
        )
    }
}

@Composable
private fun ProductCard(
    product: ProductEntity,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAddToCart),
        shape = RoundedCornerShape(16.dp),
        color = White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryBadge(category = product.category)
                Text(
                    text = "Stok: ${product.stock}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (product.stock <= 5) CrimsonRed else Slate500
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Slate900
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = FormatUtils.formatRupiah(product.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = PrimaryBlue
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CartSheetContent(
    cartItems: List<CartItem>,
    customerName: String,
    tableOrRef: String,
    orderType: String,
    taxRate: Int,
    discountAmount: Double,
    subtotal: Double,
    taxAmount: Double,
    totalAmount: Double,
    onCustomerNameChange: (String) -> Unit,
    onTableOrRefChange: (String) -> Unit,
    onOrderTypeChange: (String) -> Unit,
    onTaxRateChange: (Int) -> Unit,
    onDiscountChange: (Double) -> Unit,
    onUpdateQty: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit,
    onClearCart: () -> Unit,
    onHoldCart: () -> Unit,
    onProceedPay: () -> Unit
) {
    var discountInput by remember { mutableStateOf(if (discountAmount > 0) discountAmount.toLong().toString() else "") }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Keranjang Pesanan (${cartItems.sumOf { it.quantity }} Item)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Text(
                    text = "Kosongkan",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CrimsonRed,
                    modifier = Modifier.clickable { onClearCart() }
                )
            }
        }

        // Customer Info & Order Type
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = onCustomerNameChange,
                    placeholder = { Text("Nama Pelanggan", fontSize = 12.sp, color = Slate400) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = tableOrRef,
                    onValueChange = onTableOrRefChange,
                    placeholder = { Text("No. Meja / Lapangan", fontSize = 12.sp, color = Slate400) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }

        // Order Type Selector
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate100)
                    .padding(3.dp)
            ) {
                listOf(
                    Pair("DINE_IN", "Dine In / Main"),
                    Pair("TAKEAWAY", "Bungkus / Luar"),
                    Pair("BOOKING", "Booking Jadwal")
                ).forEach { (type, label) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (orderType == type) PrimaryBlue else Color.Transparent)
                            .clickable { onOrderTypeChange(type) }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (orderType == type) White else Slate700
                        )
                    }
                }
            }
        }

        // Items list
        items(cartItems) { item ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Slate100.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Slate900
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            CategoryBadge(category = item.product.category)
                            Text(
                                text = "@${FormatUtils.formatRupiah(item.product.price)}",
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                    }

                    // Stepper (- Qty +)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = { onUpdateQty(item.product.id, -1) },
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(White)
                                .border(1.dp, Slate300, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "-", modifier = Modifier.size(14.dp), tint = Slate700)
                        }

                        Text(
                            text = "${item.quantity}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Slate900,
                            modifier = Modifier.width(20.dp),
                            textAlign = TextAlign.Center
                        )

                        IconButton(
                            onClick = { onUpdateQty(item.product.id, 1) },
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueLight)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "+", modifier = Modifier.size(14.dp), tint = PrimaryBlue)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = FormatUtils.formatRupiah(item.totalPrice),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Slate900
                    )
                }
            }
        }

        // Summary calculations
        item {
            HorizontalDivider(color = Slate200, thickness = 1.dp)
            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Subtotal", fontSize = 12.sp, color = Slate600)
                Text(text = FormatUtils.formatRupiah(subtotal), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
            }

            // Discount Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Diskon (Rp)", fontSize = 12.sp, color = Slate600)
                OutlinedTextField(
                    value = discountInput,
                    onValueChange = {
                        discountInput = it
                        onDiscountChange(it.toDoubleOrNull() ?: 0.0)
                    },
                    modifier = Modifier.width(120.dp).height(44.dp),
                    placeholder = { Text("0", fontSize = 11.sp) },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            // PB1 Tax Toggle (0% or 10%)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Pajak Resto (PB1 10%)", fontSize = 12.sp, color = Slate600)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (taxRate == 0) PrimaryBlue else Slate100)
                            .clickable { onTaxRateChange(0) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "0%", fontSize = 11.sp, color = if (taxRate == 0) White else Slate700)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (taxRate == 10) PrimaryBlue else Slate100)
                            .clickable { onTaxRateChange(10) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "10%", fontSize = 11.sp, color = if (taxRate == 10) White else Slate700)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = Slate200, thickness = 1.dp)
            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "TOTAL PEMBAYARAN", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                Text(text = FormatUtils.formatRupiah(totalAmount), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
            }
        }

        // Action Buttons inside Cart Sheet: "Simpan (Bayar Nanti)" & "Bayar Sekarang"
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onHoldCart,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberOrange)
                ) {
                    Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Simpan (Bayar Nanti)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onProceedPay,
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Icon(imageVector = Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Bayar Sekarang", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PaymentSheetContent(
    totalAmount: Double,
    customerTitle: String? = null,
    onDismiss: () -> Unit,
    onConfirmPayment: (paymentMethod: String, cashTendered: Double) -> Unit
) {
    var selectedMethod by remember { mutableStateOf("CASH") } // CASH, QRIS, DEBIT
    var cashTenderedText by remember { mutableStateOf(totalAmount.toLong().toString()) }

    val tendered = cashTenderedText.toDoubleOrNull() ?: 0.0
    val change = (tendered - totalAmount).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Pembayaran Transaksi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                if (customerTitle != null) {
                    Text(text = customerTitle, fontSize = 12.sp, color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                }
            }
            IconButton(onClick = onDismiss) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
            }
        }

        // Total Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = PrimaryBlueLight,
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Total Tagihan", fontSize = 12.sp, color = Slate600)
                Text(
                    text = FormatUtils.formatRupiah(totalAmount),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }
        }

        // Payment Method Selectors: CASH, QRIS, DEBIT
        Text(text = "Pilih Metode Pembayaran", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("CASH", "Tunai / Cash", Icons.Default.Payment),
                Triple("QRIS", "QRIS Barcode", Icons.Default.QrCode2),
                Triple("DEBIT", "Kartu Debit", Icons.Default.Payment)
            ).forEach { (method, label, icon) ->
                val isSelected = selectedMethod == method
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedMethod = method },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) PrimaryBlue else Slate100,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PrimaryBlue else Slate200)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) White else Slate700,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) White else Slate700
                        )
                    }
                }
            }
        }

        // If Cash: show Tendered Input & Quick Buttons & Change calculation
        if (selectedMethod == "CASH") {
            Text(text = "Uang Diterima", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
            OutlinedTextField(
                value = cashTenderedText,
                onValueChange = { cashTenderedText = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("Rp ", fontWeight = FontWeight.Bold, color = Slate900) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Quick Money Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickMoneyChip("Uang Pas") { cashTenderedText = totalAmount.toLong().toString() }
                QuickMoneyChip("50 Ribu") { cashTenderedText = "50000" }
                QuickMoneyChip("100 Ribu") { cashTenderedText = "100000" }
                QuickMoneyChip("200 Ribu") { cashTenderedText = "200000" }
            }

            // Change Info Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = EmeraldGreenLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Kembalian", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Slate800)
                    Text(
                        text = FormatUtils.formatRupiah(change),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen
                    )
                }
            }
        } else if (selectedMethod == "QRIS") {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Slate100,
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.QrCode2, contentDescription = null, tint = Slate800, modifier = Modifier.size(60.dp))
                    Text(text = "Scan QRIS Kasir Statis / Dinamis", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Slate900)
                    Text(text = "Pastikan notifikasi dana masuk sebelum konfirmasi", fontSize = 11.sp, color = Slate600)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Confirm Button
        Button(
            onClick = {
                val finalTendered = if (selectedMethod == "CASH") tendered else totalAmount
                onConfirmPayment(selectedMethod, finalTendered)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Selesaikan Transaksi & Cetak Resi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuickMoneyChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Slate100)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
    }
}
