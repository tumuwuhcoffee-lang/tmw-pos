package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ShiftEntity
import com.example.data.local.entity.StockInLogEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity
import com.example.data.repository.PosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class CartItem(
    val product: ProductEntity,
    val quantity: Int = 1,
    val notes: String = ""
) {
    val totalPrice: Double get() = product.price * quantity
    val totalCost: Double get() = product.costPrice * quantity
}

data class CategoryRevenue(
    val category: String, // "BAR", "BILLIARD", "GOR"
    val totalRevenue: Double = 0.0,
    val totalCost: Double = 0.0,
    val grossProfit: Double = 0.0,
    val cashRevenue: Double = 0.0,
    val qrisRevenue: Double = 0.0,
    val debitRevenue: Double = 0.0,
    val transactionCount: Int = 0
)

data class ProductSaleStat(
    val productId: Long,
    val productName: String,
    val category: String,
    val totalQtySold: Int,
    val totalRevenue: Double,
    val totalProfit: Double
)

data class DailyTrendItem(
    val dayLabel: String,
    val dateTimestamp: Long,
    val barRevenue: Double,
    val billiardRevenue: Double,
    val gorRevenue: Double,
    val totalRevenue: Double
)

class PosViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PosRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PosRepository(db)
        viewModelScope.launch {
            repository.initializeDatabaseIfEmpty()
        }
    }

    // Active Shift
    val activeShift: StateFlow<ShiftEntity?> = repository.activeShift
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allShifts: StateFlow<List<ShiftEntity>> = repository.allShifts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Products
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lowStockProducts: StateFlow<List<ProductEntity>> = repository.lowStockProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Transactions & Cashflow
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paidTransactions: StateFlow<List<TransactionEntity>> = repository.paidTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val heldTransactions: StateFlow<List<TransactionEntity>> = repository.heldTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTransactionItems: StateFlow<List<TransactionItemEntity>> = repository.allTransactionItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStockInLogs: StateFlow<List<StockInLogEntity>> = repository.allStockInLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCashflows: StateFlow<List<CashflowEntity>> = repository.allCashflows
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Navigation Tab (0 = POS Kasir, 1 = Dashboard, 2 = Stock Menu, 3 = Cashflow)
    private val _currentNavTab = MutableStateFlow(0)
    val currentNavTab: StateFlow<Int> = _currentNavTab.asStateFlow()

    fun setNavTab(tabIndex: Int) {
        _currentNavTab.value = tabIndex
    }

    // POS Screen State
    private val _posCategoryFilter = MutableStateFlow("SEMUA")
    val posCategoryFilter: StateFlow<String> = _posCategoryFilter.asStateFlow()

    private val _posSearchQuery = MutableStateFlow("")
    val posSearchQuery: StateFlow<String> = _posSearchQuery.asStateFlow()

    fun setPosCategoryFilter(cat: String) {
        _posCategoryFilter.value = cat
    }

    fun setPosSearchQuery(query: String) {
        _posSearchQuery.value = query
    }

    // Active Cart
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _customerName = MutableStateFlow("")
    val customerName: StateFlow<String> = _customerName.asStateFlow()

    private val _tableOrRef = MutableStateFlow("")
    val tableOrRef: StateFlow<String> = _tableOrRef.asStateFlow()

    private val _orderType = MutableStateFlow("DINE_IN") // DINE_IN, TAKEAWAY, BOOKING
    val orderType: StateFlow<String> = _orderType.asStateFlow()

    private val _taxRatePercent = MutableStateFlow(0) // 0% or 10% (PB1) / 11% (PPN)
    val taxRatePercent: StateFlow<Int> = _taxRatePercent.asStateFlow()

    private val _discountAmount = MutableStateFlow(0.0)
    val discountAmount: StateFlow<Double> = _discountAmount.asStateFlow()

    fun setCustomerName(name: String) { _customerName.value = name }
    fun setTableOrRef(ref: String) { _tableOrRef.value = ref }
    fun setOrderType(type: String) { _orderType.value = type }
    fun setTaxRatePercent(rate: Int) { _taxRatePercent.value = rate }
    fun setDiscountAmount(amount: Double) { _discountAmount.value = amount }

    fun addToCart(product: ProductEntity, quantity: Int = 1, notes: String = "") {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(CartItem(product = product, quantity = quantity, notes = notes))
        }
        _cartItems.value = current
    }

    fun updateCartItemQuantity(productId: Long, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val existing = current[index]
            val newQty = existing.quantity + delta
            if (newQty > 0) {
                current[index] = existing.copy(quantity = newQty)
            } else {
                current.removeAt(index)
            }
            _cartItems.value = current
        }
    }

    fun removeCartItem(productId: Long) {
        _cartItems.value = _cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _customerName.value = ""
        _tableOrRef.value = ""
        _discountAmount.value = 0.0
    }

    // Computed Cart Totals
    val cartSubtotal: Double
        get() = _cartItems.value.sumOf { it.totalPrice }

    val cartTaxAmount: Double
        get() = (cartSubtotal - _discountAmount.value).coerceAtLeast(0.0) * (_taxRatePercent.value / 100.0)

    val cartTotalAmount: Double
        get() = (cartSubtotal - _discountAmount.value + cartTaxAmount).coerceAtLeast(0.0)

    val cartTotalCost: Double
        get() = _cartItems.value.sumOf { it.totalCost }

    val cartBarRevenue: Double
        get() = _cartItems.value.filter { it.product.category == "BAR" }.sumOf { it.totalPrice }

    val cartBilliardRevenue: Double
        get() = _cartItems.value.filter { it.product.category == "BILLIARD" }.sumOf { it.totalPrice }

    val cartGorRevenue: Double
        get() = _cartItems.value.filter { it.product.category == "GOR" }.sumOf { it.totalPrice }

    // Last completed transaction for receipt modal
    private val _lastCompletedTransaction = MutableStateFlow<TransactionEntity?>(null)
    val lastCompletedTransaction: StateFlow<TransactionEntity?> = _lastCompletedTransaction.asStateFlow()

    private val _lastCompletedItems = MutableStateFlow<List<TransactionItemEntity>>(emptyList())
    val lastCompletedItems: StateFlow<List<TransactionItemEntity>> = _lastCompletedItems.asStateFlow()

    fun dismissReceipt() {
        _lastCompletedTransaction.value = null
        _lastCompletedItems.value = emptyList()
    }

    // Process Direct Payment
    fun processPayment(
        paymentMethod: String,
        cashTendered: Double,
        onSuccess: () -> Unit
    ) {
        val shift = activeShift.value ?: return
        val items = _cartItems.value
        if (items.isEmpty()) return

        val total = cartTotalAmount
        val change = (cashTendered - total).coerceAtLeast(0.0)
        val invoiceNo = "TRX-${System.currentTimeMillis() % 1000000}"

        val trx = TransactionEntity(
            invoiceNumber = invoiceNo,
            shiftId = shift.id,
            timestamp = System.currentTimeMillis(),
            customerName = _customerName.value.ifBlank { "Pelanggan Umum" },
            tableOrOrderRef = _tableOrRef.value.ifBlank { "Kasir" },
            orderType = _orderType.value,
            paymentMethod = paymentMethod,
            paymentStatus = "PAID",
            subtotal = cartSubtotal,
            taxAmount = cartTaxAmount,
            discountAmount = _discountAmount.value,
            totalAmount = total,
            totalCostPrice = cartTotalCost,
            grossProfit = total - cartTotalCost,
            cashTendered = if (paymentMethod == "CASH") cashTendered else total,
            changeAmount = if (paymentMethod == "CASH") change else 0.0,
            barRevenue = cartBarRevenue,
            billiardRevenue = cartBilliardRevenue,
            gorRevenue = cartGorRevenue
        )

        val trxItems = items.map {
            TransactionItemEntity(
                transactionId = 0,
                productId = it.product.id,
                productName = it.product.name,
                category = it.product.category,
                unitPrice = it.product.price,
                costPrice = it.product.costPrice,
                quantity = it.quantity,
                totalPrice = it.totalPrice,
                totalCost = it.totalCost,
                notes = it.notes
            )
        }

        viewModelScope.launch {
            val trxId = repository.completeTransaction(trx, trxItems)
            val savedTrx = trx.copy(id = trxId)
            _lastCompletedTransaction.value = savedTrx
            _lastCompletedItems.value = trxItems.map { it.copy(transactionId = trxId) }
            clearCart()
            onSuccess()
        }
    }

    // Simpan untuk Bayar Nanti (Hold Order)
    fun holdOrder(
        customerName: String,
        tableRef: String,
        onSuccess: () -> Unit
    ) {
        val shift = activeShift.value ?: return
        val items = _cartItems.value
        if (items.isEmpty()) return

        val invoiceNo = "HELD-${System.currentTimeMillis() % 100000}"
        val trx = TransactionEntity(
            invoiceNumber = invoiceNo,
            shiftId = shift.id,
            timestamp = System.currentTimeMillis(),
            customerName = customerName.ifBlank { "Open Bill" },
            tableOrOrderRef = tableRef.ifBlank { "Meja / Pesanan" },
            orderType = _orderType.value,
            paymentMethod = "CASH",
            paymentStatus = "HELD",
            subtotal = cartSubtotal,
            taxAmount = cartTaxAmount,
            discountAmount = _discountAmount.value,
            totalAmount = cartTotalAmount,
            totalCostPrice = cartTotalCost,
            grossProfit = cartTotalAmount - cartTotalCost,
            cashTendered = 0.0,
            changeAmount = 0.0,
            barRevenue = cartBarRevenue,
            billiardRevenue = cartBilliardRevenue,
            gorRevenue = cartGorRevenue
        )

        val trxItems = items.map {
            TransactionItemEntity(
                transactionId = 0,
                productId = it.product.id,
                productName = it.product.name,
                category = it.product.category,
                unitPrice = it.product.price,
                costPrice = it.product.costPrice,
                quantity = it.quantity,
                totalPrice = it.totalPrice,
                totalCost = it.totalCost,
                notes = it.notes
            )
        }

        viewModelScope.launch {
            repository.holdTransaction(trx, trxItems)
            clearCart()
            onSuccess()
        }
    }

    // Resume Held Bill back into active cart
    fun resumeHeldBill(trx: TransactionEntity) {
        viewModelScope.launch {
            val items = repository.getItemsForTransactionOnce(trx.id)
            val products = allProducts.value
            val cartList = items.mapNotNull { item ->
                val prod = products.find { it.id == item.productId } ?: ProductEntity(
                    id = item.productId,
                    name = item.productName,
                    category = item.category,
                    price = item.unitPrice,
                    costPrice = item.costPrice,
                    stock = 99
                )
                CartItem(product = prod, quantity = item.quantity, notes = item.notes ?: "")
            }
            _cartItems.value = cartList
            _customerName.value = trx.customerName ?: ""
            _tableOrRef.value = trx.tableOrOrderRef ?: ""
            _orderType.value = trx.orderType
            _discountAmount.value = trx.discountAmount

            // Remove held bill from database so it doesn't duplicate
            repository.deleteHeldTransaction(trx.id)
        }
    }

    // Direct pay held bill
    fun payHeldBillDirectly(
        trx: TransactionEntity,
        paymentMethod: String,
        cashTendered: Double,
        changeAmount: Double,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.payHeldTransaction(trx, paymentMethod, cashTendered, changeAmount)
            val items = repository.getItemsForTransactionOnce(trx.id)
            _lastCompletedTransaction.value = trx.copy(
                paymentStatus = "PAID",
                paymentMethod = paymentMethod,
                cashTendered = cashTendered,
                changeAmount = changeAmount
            )
            _lastCompletedItems.value = items
            onSuccess()
        }
    }

    fun deleteHeldBill(trxId: Long) {
        viewModelScope.launch {
            repository.deleteHeldTransaction(trxId)
        }
    }

    // Void / Cancel Transaction
    fun voidTransaction(trxId: Long, reason: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.voidTransaction(trxId, reason)
            onSuccess()
        }
    }

    // Shift Operations
    fun openShift(
        cashierName: String,
        initialCash: Double,
        initialBarCash: Double,
        initialBilliardCash: Double,
        initialGorCash: Double,
        notes: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.openShift(
                cashierName,
                initialCash,
                initialBarCash,
                initialBilliardCash,
                initialGorCash,
                notes
            )
            onSuccess()
        }
    }

    fun closeShift(
        shift: ShiftEntity,
        actualCash: Double,
        notes: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.closeShift(shift, actualCash, notes)
            onSuccess()
        }
    }

    fun addPettyCash(
        type: String, // "DEBIT" or "KREDIT"
        amount: Double,
        category: String,
        description: String,
        businessUnit: String,
        paymentMethod: String = "CASH",
        onSuccess: () -> Unit
    ) {
        val shift = activeShift.value
        viewModelScope.launch {
            repository.addCashflow(
                type = type,
                category = category,
                businessUnit = businessUnit,
                amount = amount,
                paymentMethod = paymentMethod,
                description = description,
                shiftId = shift?.id
            )
            onSuccess()
        }
    }

    // Stock Management Operations
    fun addProduct(product: ProductEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.insertProduct(product)
            onSuccess()
        }
    }

    fun updateProduct(product: ProductEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateProduct(product)
            onSuccess()
        }
    }

    fun deleteProduct(product: ProductEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            onSuccess()
        }
    }

    fun recordStockIn(
        productId: Long,
        productName: String,
        category: String,
        supplierName: String,
        quantity: Int,
        unitPrice: Double,
        totalCost: Double,
        paymentSource: String,
        notes: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.recordStockIn(
                productId = productId,
                productName = productName,
                category = category,
                supplierName = supplierName,
                quantity = quantity,
                unitPrice = unitPrice,
                totalCost = totalCost,
                paymentSource = paymentSource,
                notes = notes
            )
            onSuccess()
        }
    }

    // ----------------------------------------------------
    // DASHBOARD & CALENDAR SELECTION LOGIC
    // ----------------------------------------------------
    // Date Range in Dashboard
    private val _dashboardStartDate = MutableStateFlow(getStartOfToday())
    val dashboardStartDate: StateFlow<Long> = _dashboardStartDate.asStateFlow()

    private val _dashboardEndDate = MutableStateFlow(getEndOfToday())
    val dashboardEndDate: StateFlow<Long> = _dashboardEndDate.asStateFlow()

    private val _dateSelectionMode = MutableStateFlow("TODAY") // TODAY, YESTERDAY, LAST_7_DAYS, THIS_MONTH, CUSTOM
    val dateSelectionMode: StateFlow<String> = _dateSelectionMode.asStateFlow()

    fun setQuickDateFilter(mode: String) {
        _dateSelectionMode.value = mode
        when (mode) {
            "TODAY" -> {
                _dashboardStartDate.value = getStartOfToday()
                _dashboardEndDate.value = getEndOfToday()
            }
            "YESTERDAY" -> {
                val cal = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                _dashboardStartDate.value = cal.timeInMillis
                cal.set(Calendar.HOUR_OF_DAY, 23)
                cal.set(Calendar.MINUTE, 59)
                cal.set(Calendar.SECOND, 59)
                _dashboardEndDate.value = cal.timeInMillis
            }
            "LAST_7_DAYS" -> {
                val cal = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -6)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                _dashboardStartDate.value = cal.timeInMillis
                _dashboardEndDate.value = getEndOfToday()
            }
            "THIS_MONTH" -> {
                val cal = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                _dashboardStartDate.value = cal.timeInMillis
                _dashboardEndDate.value = getEndOfToday()
            }
        }
    }

    // Interactive calendar selection: single tap vs range tap
    fun handleCalendarDateClick(dateMillis: Long) {
        val cal = Calendar.getInstance().apply { timeInMillis = dateMillis }
        val startOfClickedDay = cal.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfClickedDay = cal.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        // If in custom mode and start date is set to single day, clicking another date sets range!
        if (_dateSelectionMode.value == "CUSTOM" && _dashboardStartDate.value == _dashboardEndDate.value - (86399000L)) {
            if (startOfClickedDay > _dashboardStartDate.value) {
                // Set Range from existing start to clicked end
                _dashboardEndDate.value = endOfClickedDay
                return
            } else if (startOfClickedDay < _dashboardStartDate.value) {
                // Set Range from clicked start to existing end
                _dashboardEndDate.value = _dashboardStartDate.value + 86399000L
                _dashboardStartDate.value = startOfClickedDay
                return
            }
        }

        // Single date selection (e.g. tapping yesterday or specific day)
        _dateSelectionMode.value = "CUSTOM"
        _dashboardStartDate.value = startOfClickedDay
        _dashboardEndDate.value = endOfClickedDay
    }

    fun setCustomDateRange(startMillis: Long, endMillis: Long) {
        _dateSelectionMode.value = "CUSTOM"
        _dashboardStartDate.value = startMillis
        _dashboardEndDate.value = endMillis
    }

    // Filtered Transactions for selected Date Range in Dashboard
    val dashboardTransactions: StateFlow<List<TransactionEntity>> = combine(
        paidTransactions,
        _dashboardStartDate,
        _dashboardEndDate
    ) { trxs, start, end ->
        trxs.filter { it.timestamp in start..end }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selisih Omset Calculation (Compare selected period with previous matching period)
    val previousPeriodRevenue: StateFlow<Double> = combine(
        paidTransactions,
        _dashboardStartDate,
        _dashboardEndDate
    ) { trxs, start, end ->
        val duration = end - start
        val prevStart = start - duration
        val prevEnd = start - 1
        trxs.filter { it.timestamp in prevStart..prevEnd }.sumOf { it.totalAmount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Category Revenue Split (BAR, BILLIARD, GOR) - 1 PINTU
    val barRevenueStat: StateFlow<CategoryRevenue> = dashboardTransactions.combine(allTransactionItems) { trxs, items ->
        calculateCategoryStat("BAR", trxs, items)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryRevenue("BAR"))

    val billiardRevenueStat: StateFlow<CategoryRevenue> = dashboardTransactions.combine(allTransactionItems) { trxs, items ->
        calculateCategoryStat("BILLIARD", trxs, items)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryRevenue("BILLIARD"))

    val gorRevenueStat: StateFlow<CategoryRevenue> = dashboardTransactions.combine(allTransactionItems) { trxs, items ->
        calculateCategoryStat("GOR", trxs, items)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoryRevenue("GOR"))

    // Top 5 Best Selling Products for Dashboard
    val topSellingProducts: StateFlow<List<ProductSaleStat>> = combine(
        dashboardTransactions,
        allTransactionItems
    ) { trxs, items ->
        val trxIds = trxs.map { it.id }.toSet()
        val periodItems = items.filter { it.transactionId in trxIds }
        periodItems.groupBy { it.productId }
            .map { (prodId, prodItems) ->
                val first = prodItems.first()
                val qty = prodItems.sumOf { it.quantity }
                val revenue = prodItems.sumOf { it.totalPrice }
                val cost = prodItems.sumOf { it.totalCost }
                ProductSaleStat(
                    productId = prodId,
                    productName = first.productName,
                    category = first.category,
                    totalQtySold = qty,
                    totalRevenue = revenue,
                    totalProfit = revenue - cost
                )
            }
            .sortedByDescending { it.totalRevenue }
            .take(5)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All Sold Products detail for selected period (Clickable modal)
    val allSoldProductsInPeriod: StateFlow<List<ProductSaleStat>> = combine(
        dashboardTransactions,
        allTransactionItems
    ) { trxs, items ->
        val trxIds = trxs.map { it.id }.toSet()
        val periodItems = items.filter { it.transactionId in trxIds }
        periodItems.groupBy { it.productId }
            .map { (prodId, prodItems) ->
                val first = prodItems.first()
                val qty = prodItems.sumOf { it.quantity }
                val revenue = prodItems.sumOf { it.totalPrice }
                val cost = prodItems.sumOf { it.totalCost }
                ProductSaleStat(
                    productId = prodId,
                    productName = first.productName,
                    category = first.category,
                    totalQtySold = qty,
                    totalRevenue = revenue,
                    totalProfit = revenue - cost
                )
            }
            .sortedByDescending { it.totalRevenue }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cashflow Totals
    val totalRealCashBalance: StateFlow<Double> = allCashflows.combine(activeShift) { cashflows, _ ->
        val debit = cashflows.filter { it.type == "DEBIT" }.sumOf { it.amount }
        val kredit = cashflows.filter { it.type == "KREDIT" }.sumOf { it.amount }
        debit - kredit
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalCashIn: StateFlow<Double> = allCashflows.combine(activeShift) { cashflows, _ ->
        cashflows.filter { it.type == "DEBIT" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalCashOut: StateFlow<Double> = allCashflows.combine(activeShift) { cashflows, _ ->
        cashflows.filter { it.type == "KREDIT" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private fun calculateCategoryStat(
        category: String,
        trxs: List<TransactionEntity>,
        allItems: List<TransactionItemEntity>
    ): CategoryRevenue {
        var totalRev = 0.0
        var totalCost = 0.0
        var cashRev = 0.0
        var qrisRev = 0.0
        var debitRev = 0.0
        var count = 0

        trxs.forEach { trx ->
            val trxItems = allItems.filter { it.transactionId == trx.id && it.category == category }
            if (trxItems.isNotEmpty()) {
                val catAmount = trxItems.sumOf { it.totalPrice }
                val catCost = trxItems.sumOf { it.totalCost }
                totalRev += catAmount
                totalCost += catCost
                count++

                when (trx.paymentMethod) {
                    "CASH" -> cashRev += catAmount
                    "QRIS" -> qrisRev += catAmount
                    else -> debitRev += catAmount
                }
            }
        }

        return CategoryRevenue(
            category = category,
            totalRevenue = totalRev,
            totalCost = totalCost,
            grossProfit = totalRev - totalCost,
            cashRevenue = cashRev,
            qrisRevenue = qrisRev,
            debitRevenue = debitRev,
            transactionCount = count
        )
    }

    companion object {
        fun getStartOfToday(): Long {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        }

        fun getEndOfToday(): Long {
            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 23)
                set(Calendar.MINUTE, 59)
                set(Calendar.SECOND, 59)
                set(Calendar.MILLISECOND, 999)
            }.timeInMillis
        }
    }
}
