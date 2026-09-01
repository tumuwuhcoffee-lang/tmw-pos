package com.example.data.local

import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.CustomerProfileEntity
import com.example.data.local.entity.JournalEntryEntity
import com.example.data.local.entity.MarketplaceOrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.ShiftEntity
import com.example.data.local.entity.StockInLogEntity
import com.example.data.local.entity.SupplierOrderEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity
import java.util.Calendar

object SeedData {
    val initialProducts = listOf(
        // BAR Products
        ProductEntity(
            id = 1,
            name = "Espresso Single Origin",
            category = "BAR",
            subCategory = "Coffee",
            price = 22000.0,
            costPrice = 8000.0,
            stock = 85,
            lowStockThreshold = 15,
            unit = "Cup",
            sku = "BAR-ESP-01"
        ),
        ProductEntity(
            id = 2,
            name = "Iced Caramel Macchiato",
            category = "BAR",
            subCategory = "Coffee",
            price = 32000.0,
            costPrice = 12000.0,
            stock = 60,
            lowStockThreshold = 10,
            unit = "Cup",
            sku = "BAR-MAC-02"
        ),
        ProductEntity(
            id = 3,
            name = "Matcha Fusion Latte",
            category = "BAR",
            subCategory = "Non-Coffee",
            price = 28000.0,
            costPrice = 11000.0,
            stock = 45,
            lowStockThreshold = 10,
            unit = "Cup",
            sku = "BAR-MAT-03"
        ),
        ProductEntity(
            id = 4,
            name = "Croissant Butter Almond",
            category = "BAR",
            subCategory = "Bakery",
            price = 25000.0,
            costPrice = 13000.0,
            stock = 18,
            lowStockThreshold = 5,
            unit = "Pcs",
            sku = "BAR-CRS-04"
        ),
        ProductEntity(
            id = 5,
            name = "French Fries Truffle",
            category = "BAR",
            subCategory = "Snack",
            price = 27000.0,
            costPrice = 10000.0,
            stock = 30,
            lowStockThreshold = 8,
            unit = "Porsi",
            sku = "BAR-FFR-05"
        ),
        ProductEntity(
            id = 6,
            name = "Mineral Water 600ml (Bar)",
            category = "BAR",
            subCategory = "Drinks",
            price = 8000.0,
            costPrice = 3000.0,
            stock = 120,
            lowStockThreshold = 20,
            unit = "Botol",
            sku = "BAR-WTR-06"
        ),

        // BILLIARD Products
        ProductEntity(
            id = 7,
            name = "Sewa Meja 9ft Reguler (1 Jam)",
            category = "BILLIARD",
            subCategory = "Rental",
            price = 45000.0,
            costPrice = 5000.0,
            stock = 999,
            lowStockThreshold = 10,
            unit = "Jam",
            sku = "BIL-REG-01"
        ),
        ProductEntity(
            id = 8,
            name = "Sewa Meja VIP Room (1 Jam)",
            category = "BILLIARD",
            subCategory = "Rental VIP",
            price = 85000.0,
            costPrice = 15000.0,
            stock = 999,
            lowStockThreshold = 10,
            unit = "Jam",
            sku = "BIL-VIP-02"
        ),
        ProductEntity(
            id = 9,
            name = "Sarung Tangan Billiard Pro",
            category = "BILLIARD",
            subCategory = "Aksesoris",
            price = 35000.0,
            costPrice = 18000.0,
            stock = 14,
            lowStockThreshold = 5,
            unit = "Pcs",
            sku = "BIL-GLV-03"
        ),
        ProductEntity(
            id = 10,
            name = "Kapur Chalk Master Blue",
            category = "BILLIARD",
            subCategory = "Aksesoris",
            price = 15000.0,
            costPrice = 6000.0,
            stock = 4, // low stock trigger
            lowStockThreshold = 5,
            unit = "Pcs",
            sku = "BIL-CHK-04"
        ),
        ProductEntity(
            id = 11,
            name = "Snack Ring Kacang Kulit (Billiard)",
            category = "BILLIARD",
            subCategory = "Snack",
            price = 12000.0,
            costPrice = 6000.0,
            stock = 25,
            lowStockThreshold = 5,
            unit = "Bungkus",
            sku = "BIL-SNC-05"
        ),

        // GOR (Gedung Olahraga) Products
        ProductEntity(
            id = 12,
            name = "Sewa Lapangan Badminton (1 Jam)",
            category = "GOR",
            subCategory = "Sewa Lapangan",
            price = 60000.0,
            costPrice = 10000.0,
            stock = 999,
            lowStockThreshold = 10,
            unit = "Jam",
            sku = "GOR-BDM-01"
        ),
        ProductEntity(
            id = 13,
            name = "Sewa Lapangan Futsal Interlock (1 Jam)",
            category = "GOR",
            subCategory = "Sewa Lapangan",
            price = 150000.0,
            costPrice = 30000.0,
            stock = 999,
            lowStockThreshold = 10,
            unit = "Jam",
            sku = "GOR-FUT-02"
        ),
        ProductEntity(
            id = 14,
            name = "Shuttlecock Tournament (1 Slop / 12 pcs)",
            category = "GOR",
            subCategory = "Perlengkapan",
            price = 110000.0,
            costPrice = 85000.0,
            stock = 8,
            lowStockThreshold = 5,
            unit = "Slop",
            sku = "GOR-SHU-03"
        ),
        ProductEntity(
            id = 15,
            name = "Minuman Isotonik Pocari 500ml",
            category = "GOR",
            subCategory = "Minuman GOR",
            price = 10000.0,
            costPrice = 5500.0,
            stock = 75,
            lowStockThreshold = 15,
            unit = "Botol",
            sku = "GOR-ISO-04"
        ),
        ProductEntity(
            id = 16,
            name = "Sewa Raket Badminton Yonex",
            category = "GOR",
            subCategory = "Sewa Alat",
            price = 20000.0,
            costPrice = 2000.0,
            stock = 12,
            lowStockThreshold = 3,
            unit = "Pcs/Sesi",
            sku = "GOR-RKT-05"
        )
    )

    fun createInitialShiftsAndHistory(): InitialSeedResult {
        val now = System.currentTimeMillis()

        // Active clean shift with 0 initial transactions
        val activeShift = ShiftEntity(
            id = 1,
            shiftNumber = 1,
            cashierName = "Kasir Tumuwuh",
            startTime = now,
            endTime = null,
            status = "OPEN",
            initialCash = 0.0,
            initialBarCash = 0.0,
            initialBilliardCash = 0.0,
            initialGorCash = 0.0,
            notes = "Shift Aktif - Bersih"
        )

        val transactions = emptyList<TransactionEntity>()
        val items = emptyList<TransactionItemEntity>()
        val cashflows = emptyList<CashflowEntity>()
        val stockLogs = emptyList<StockInLogEntity>()
        val journalEntries = emptyList<JournalEntryEntity>()
        val supplierOrders = emptyList<SupplierOrderEntity>()
        val marketplaceOrders = emptyList<MarketplaceOrderEntity>()

        // Clean initial customer list
        val customers = listOf(
            CustomerProfileEntity(
                id = 1,
                customerName = "Pelanggan Umum",
                phone = "081234567890",
                email = "customer@tumuwuh.id",
                tier = "REGULER",
                loyaltyPoints = 0,
                favoriteCategory = "BAR",
                favoriteItem = "Espresso Single Origin",
                totalSpent = 0L,
                visitCount = 0,
                activeCoupons = ""
            )
        )

        return InitialSeedResult(
            activeShift = activeShift,
            transactions = transactions,
            items = items,
            cashflows = cashflows,
            stockLogs = stockLogs,
            journalEntries = journalEntries,
            supplierOrders = supplierOrders,
            customerProfiles = customers,
            marketplaceOrders = marketplaceOrders
        )
    }
}

data class InitialSeedResult(
    val activeShift: ShiftEntity,
    val transactions: List<TransactionEntity>,
    val items: List<TransactionItemEntity>,
    val cashflows: List<CashflowEntity>,
    val stockLogs: List<StockInLogEntity>,
    val journalEntries: List<JournalEntryEntity>,
    val supplierOrders: List<SupplierOrderEntity>,
    val customerProfiles: List<CustomerProfileEntity>,
    val marketplaceOrders: List<MarketplaceOrderEntity>
)
