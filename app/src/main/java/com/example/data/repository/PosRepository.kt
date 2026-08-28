package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SeedData
import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ShiftEntity
import com.example.data.local.entity.StockInLogEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class PosRepository(private val database: AppDatabase) {
    private val productDao = database.productDao()
    private val shiftDao = database.shiftDao()
    private val transactionDao = database.transactionDao()
    private val itemDao = database.transactionItemDao()
    private val stockLogDao = database.stockInLogDao()
    private val cashflowDao = database.cashflowDao()

    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val lowStockProducts: Flow<List<ProductEntity>> = productDao.getLowStockProducts()
    val activeShift: Flow<ShiftEntity?> = shiftDao.getActiveShift()
    val allShifts: Flow<List<ShiftEntity>> = shiftDao.getAllShifts()
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val paidTransactions: Flow<List<TransactionEntity>> = transactionDao.getPaidTransactions()
    val heldTransactions: Flow<List<TransactionEntity>> = transactionDao.getHeldTransactions()
    val allTransactionItems: Flow<List<TransactionItemEntity>> = itemDao.getAllTransactionItems()
    val allStockInLogs: Flow<List<StockInLogEntity>> = stockLogDao.getAllLogs()
    val allCashflows: Flow<List<CashflowEntity>> = cashflowDao.getAllCashflows()

    suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
        val existingProducts = productDao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            productDao.insertProducts(SeedData.initialProducts)
            val seedResult = SeedData.createInitialShiftsAndHistory()
            shiftDao.insertShift(seedResult.activeShift)
            seedResult.transactions.forEach { trx ->
                transactionDao.insertTransaction(trx)
            }
            itemDao.insertItems(seedResult.items)
            seedResult.stockLogs.forEach { log ->
                stockLogDao.insertLog(log)
            }
            seedResult.cashflows.forEach { cf ->
                cashflowDao.insertCashflow(cf)
            }
        }
    }

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> =
        if (category == "ALL" || category == "SEMUA") productDao.getAllProducts()
        else productDao.getProductsByCategory(category)

    fun getTransactionsByShift(shiftId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByShift(shiftId)

    fun getCancelledTransactionsByShift(shiftId: Long): Flow<List<TransactionEntity>> =
        transactionDao.getCancelledTransactionsByShift(shiftId)

    fun getTransactionsByDateRange(startTime: Long, endTime: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByDateRange(startTime, endTime)

    fun getItemsForTransaction(transactionId: Long): Flow<List<TransactionItemEntity>> =
        itemDao.getItemsForTransaction(transactionId)

    suspend fun getItemsForTransactionOnce(transactionId: Long): List<TransactionItemEntity> =
        itemDao.getItemsForTransactionOnce(transactionId)

    // Product actions
    suspend fun insertProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    // Shift actions
    suspend fun openShift(
        cashierName: String,
        initialCash: Double,
        initialBarCash: Double,
        initialBilliardCash: Double,
        initialGorCash: Double,
        notes: String?
    ): Long = withContext(Dispatchers.IO) {
        val shiftCount = shiftDao.getAllShifts().first().size
        val newShift = ShiftEntity(
            shiftNumber = shiftCount + 1,
            cashierName = cashierName.ifBlank { "Kasir" },
            startTime = System.currentTimeMillis(),
            status = "OPEN",
            initialCash = initialCash,
            initialBarCash = initialBarCash,
            initialBilliardCash = initialBilliardCash,
            initialGorCash = initialGorCash,
            notes = notes
        )
        val shiftId = shiftDao.insertShift(newShift)

        // Record initial cash into cashflow
        cashflowDao.insertCashflow(
            CashflowEntity(
                timestamp = System.currentTimeMillis(),
                type = "DEBIT",
                category = "MODAL_AWAL",
                businessUnit = "UMUM",
                amount = initialCash,
                paymentMethod = "CASH",
                description = "Modal Awal Shift #${shiftCount + 1} ($cashierName)",
                shiftId = shiftId
            )
        )
        shiftId
    }

    suspend fun closeShift(
        shift: ShiftEntity,
        actualCashInDrawer: Double,
        notes: String?
    ) = withContext(Dispatchers.IO) {
        val shiftTrxs = transactionDao.getTransactionsByShift(shift.id).first()
        val paidTrxs = shiftTrxs.filter { it.paymentStatus == "PAID" }
        val cashSales = paidTrxs.filter { it.paymentMethod == "CASH" }.sumOf { it.totalAmount }

        // Cash in/out from cashflow for this shift
        val shiftCashflows = cashflowDao.getAllCashflows().first().filter { it.shiftId == shift.id }
        val otherCashIn = shiftCashflows.filter { it.type == "DEBIT" && it.category != "MODAL_AWAL" && it.category != "PENJUALAN" }.sumOf { it.amount }
        val otherCashOut = shiftCashflows.filter { it.type == "KREDIT" && it.paymentMethod == "CASH" }.sumOf { it.amount }

        val expectedCash = shift.initialCash + cashSales + otherCashIn - otherCashOut
        val difference = actualCashInDrawer - expectedCash

        val updatedShift = shift.copy(
            status = "CLOSED",
            endTime = System.currentTimeMillis(),
            closingCashActual = actualCashInDrawer,
            closingCashExpected = expectedCash,
            cashDifference = difference,
            notes = notes
        )
        shiftDao.updateShift(updatedShift)
    }

    // Transaction actions
    suspend fun completeTransaction(
        transaction: TransactionEntity,
        items: List<TransactionItemEntity>
    ): Long = withContext(Dispatchers.IO) {
        val trxId = transactionDao.insertTransaction(transaction)
        val itemsWithTrxId = items.map { it.copy(transactionId = trxId) }
        itemDao.insertItems(itemsWithTrxId)

        // Decrease stock
        items.forEach { item ->
            productDao.decreaseStock(item.productId, item.quantity)
        }

        // Record in cashflow
        if (transaction.paymentStatus == "PAID") {
            val unit = when {
                transaction.barRevenue > 0 && transaction.billiardRevenue == 0.0 && transaction.gorRevenue == 0.0 -> "BAR"
                transaction.billiardRevenue > 0 && transaction.barRevenue == 0.0 && transaction.gorRevenue == 0.0 -> "BILLIARD"
                transaction.gorRevenue > 0 && transaction.barRevenue == 0.0 && transaction.billiardRevenue == 0.0 -> "GOR"
                else -> "UMUM"
            }
            cashflowDao.insertCashflow(
                CashflowEntity(
                    timestamp = transaction.timestamp,
                    type = "DEBIT",
                    category = "PENJUALAN",
                    businessUnit = unit,
                    amount = transaction.totalAmount,
                    paymentMethod = transaction.paymentMethod,
                    description = "Penjualan POS #${transaction.invoiceNumber} (${transaction.paymentMethod})",
                    referenceId = transaction.invoiceNumber,
                    shiftId = transaction.shiftId
                )
            )
        }
        trxId
    }

    suspend fun holdTransaction(
        transaction: TransactionEntity,
        items: List<TransactionItemEntity>
    ): Long = withContext(Dispatchers.IO) {
        val heldTrx = transaction.copy(paymentStatus = "HELD")
        val trxId = transactionDao.insertTransaction(heldTrx)
        val itemsWithTrxId = items.map { it.copy(transactionId = trxId) }
        itemDao.insertItems(itemsWithTrxId)
        trxId
    }

    suspend fun payHeldTransaction(
        transaction: TransactionEntity,
        paymentMethod: String,
        cashTendered: Double,
        changeAmount: Double
    ) = withContext(Dispatchers.IO) {
        val updatedTrx = transaction.copy(
            paymentStatus = "PAID",
            paymentMethod = paymentMethod,
            cashTendered = cashTendered,
            changeAmount = changeAmount,
            timestamp = System.currentTimeMillis()
        )
        transactionDao.updateTransaction(updatedTrx)

        val items = itemDao.getItemsForTransactionOnce(transaction.id)
        items.forEach { item ->
            productDao.decreaseStock(item.productId, item.quantity)
        }

        val unit = when {
            updatedTrx.barRevenue > 0 && updatedTrx.billiardRevenue == 0.0 && updatedTrx.gorRevenue == 0.0 -> "BAR"
            updatedTrx.billiardRevenue > 0 && updatedTrx.barRevenue == 0.0 && updatedTrx.gorRevenue == 0.0 -> "BILLIARD"
            updatedTrx.gorRevenue > 0 && updatedTrx.barRevenue == 0.0 && updatedTrx.billiardRevenue == 0.0 -> "GOR"
            else -> "UMUM"
        }
        cashflowDao.insertCashflow(
            CashflowEntity(
                timestamp = updatedTrx.timestamp,
                type = "DEBIT",
                category = "PENJUALAN",
                businessUnit = unit,
                amount = updatedTrx.totalAmount,
                paymentMethod = updatedTrx.paymentMethod,
                description = "Pelunasan Open Bill #${updatedTrx.invoiceNumber} (${updatedTrx.paymentMethod})",
                referenceId = updatedTrx.invoiceNumber,
                shiftId = updatedTrx.shiftId
            )
        )
    }

    suspend fun voidTransaction(
        transactionId: Long,
        reason: String
    ) = withContext(Dispatchers.IO) {
        val trx = transactionDao.getTransactionById(transactionId) ?: return@withContext
        val updatedTrx = trx.copy(
            paymentStatus = "CANCELLED",
            cancelReason = reason,
            cancelledAt = System.currentTimeMillis()
        )
        transactionDao.updateTransaction(updatedTrx)

        // Restore stock
        val items = itemDao.getItemsForTransactionOnce(transactionId)
        items.forEach { item ->
            productDao.increaseStock(item.productId, item.quantity)
        }

        // If it was paid, record compensatory cashflow (KREDIT / REFUND)
        if (trx.paymentStatus == "PAID") {
            cashflowDao.insertCashflow(
                CashflowEntity(
                    timestamp = System.currentTimeMillis(),
                    type = "KREDIT",
                    category = "OPERASIONAL",
                    businessUnit = "UMUM",
                    amount = trx.totalAmount,
                    paymentMethod = trx.paymentMethod,
                    description = "Void/Batal Transaksi #${trx.invoiceNumber} (Alasan: $reason)",
                    referenceId = trx.invoiceNumber,
                    shiftId = trx.shiftId
                )
            )
        }
    }

    suspend fun deleteHeldTransaction(transactionId: Long) = withContext(Dispatchers.IO) {
        itemDao.deleteItemsForTransaction(transactionId)
        transactionDao.deleteTransactionById(transactionId)
    }

    // Stock In / Restock actions
    suspend fun recordStockIn(
        productId: Long,
        productName: String,
        category: String,
        supplierName: String,
        quantity: Int,
        unitPrice: Double,
        totalCost: Double,
        paymentSource: String,
        notes: String?
    ) = withContext(Dispatchers.IO) {
        val log = StockInLogEntity(
            productId = productId,
            productName = productName,
            category = category,
            timestamp = System.currentTimeMillis(),
            supplierName = supplierName,
            quantity = quantity,
            unitPrice = unitPrice,
            totalCost = totalCost,
            paymentSource = paymentSource,
            notes = notes
        )
        stockLogDao.insertLog(log)

        // Increase product stock & update cost price if provided
        productDao.increaseStock(productId, quantity)

        // Auto record cashflow (KREDIT / BELANJA_STOK)
        val activeShift = shiftDao.getActiveShiftOnce()
        cashflowDao.insertCashflow(
            CashflowEntity(
                timestamp = System.currentTimeMillis(),
                type = "KREDIT",
                category = "BELANJA_STOK",
                businessUnit = category,
                amount = totalCost,
                paymentMethod = if (paymentSource == "KAS_LACI") "CASH" else "BANK",
                description = "Beli Bahan: $productName ($quantity $category) dari $supplierName",
                referenceId = "STOCK-IN-${System.currentTimeMillis() % 10000}",
                shiftId = activeShift?.id
            )
        )
    }

    // Cashflow actions
    suspend fun addCashflow(
        type: String,
        category: String,
        businessUnit: String,
        amount: Double,
        paymentMethod: String,
        description: String,
        shiftId: Long?
    ) = withContext(Dispatchers.IO) {
        cashflowDao.insertCashflow(
            CashflowEntity(
                timestamp = System.currentTimeMillis(),
                type = type,
                category = category,
                businessUnit = businessUnit,
                amount = amount,
                paymentMethod = paymentMethod,
                description = description,
                shiftId = shiftId
            )
        )
    }
}
