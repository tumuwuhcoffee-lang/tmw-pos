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
        val calendar = Calendar.getInstance()

        // Active shift
        val activeShift = ShiftEntity(
            id = 1,
            shiftNumber = 1,
            cashierName = "Budi Santoso",
            startTime = now - (3 * 3600 * 1000), // 3 hours ago
            endTime = null,
            status = "OPEN",
            initialCash = 500000.0,
            initialBarCash = 200000.0,
            initialBilliardCash = 150000.0,
            initialGorCash = 150000.0,
            notes = "Shift Pagi - Reguler"
        )

        val transactions = mutableListOf<TransactionEntity>()
        val items = mutableListOf<TransactionItemEntity>()
        val cashflows = mutableListOf<CashflowEntity>()
        val stockLogs = mutableListOf<StockInLogEntity>()

        // Initial Cashflow for Shift 1 Cash In
        cashflows.add(
            CashflowEntity(
                id = 1,
                timestamp = activeShift.startTime,
                type = "DEBIT",
                category = "MODAL_AWAL",
                businessUnit = "UMUM",
                amount = 500000.0,
                paymentMethod = "CASH",
                description = "Modal awal kasir Budi Santoso (Shift #1)",
                shiftId = 1
            )
        )

        // Generate transactions for today (Shift 1)
        val t1 = TransactionEntity(
            id = 1,
            invoiceNumber = "TRX-${System.currentTimeMillis() % 100000}-001",
            shiftId = 1,
            timestamp = now - (2 * 3600 * 1000),
            customerName = "Rian Dimas",
            tableOrOrderRef = "Meja Bar 03",
            orderType = "DINE_IN",
            paymentMethod = "CASH",
            paymentStatus = "PAID",
            subtotal = 54000.0,
            taxAmount = 0.0,
            discountAmount = 0.0,
            totalAmount = 54000.0,
            totalCostPrice = 20000.0,
            grossProfit = 34000.0,
            cashTendered = 100000.0,
            changeAmount = 46000.0,
            barRevenue = 54000.0,
            billiardRevenue = 0.0,
            gorRevenue = 0.0
        )
        transactions.add(t1)
        items.add(TransactionItemEntity(transactionId = 1, productId = 1, productName = "Espresso Single Origin", category = "BAR", unitPrice = 22000.0, costPrice = 8000.0, quantity = 1, totalPrice = 22000.0, totalCost = 8000.0))
        items.add(TransactionItemEntity(transactionId = 1, productId = 2, productName = "Iced Caramel Macchiato", category = "BAR", unitPrice = 32000.0, costPrice = 12000.0, quantity = 1, totalPrice = 32000.0, totalCost = 12000.0))
        cashflows.add(CashflowEntity(timestamp = t1.timestamp, type = "DEBIT", category = "PENJUALAN", businessUnit = "BAR", amount = 54000.0, paymentMethod = "CASH", description = "Penjualan Bar #TRX-001", referenceId = t1.invoiceNumber, shiftId = 1))

        // Transaction 2: Billiard (QRIS)
        val t2 = TransactionEntity(
            id = 2,
            invoiceNumber = "TRX-${System.currentTimeMillis() % 100000}-002",
            shiftId = 1,
            timestamp = now - (90 * 60 * 1000),
            customerName = "Komunitas 8-Ball",
            tableOrOrderRef = "Meja 9ft #02",
            orderType = "DINE_IN",
            paymentMethod = "QRIS",
            paymentStatus = "PAID",
            subtotal = 125000.0,
            taxAmount = 0.0,
            discountAmount = 0.0,
            totalAmount = 125000.0,
            totalCostPrice = 28000.0,
            grossProfit = 97000.0,
            cashTendered = 125000.0,
            changeAmount = 0.0,
            barRevenue = 0.0,
            billiardRevenue = 125000.0,
            gorRevenue = 0.0
        )
        transactions.add(t2)
        items.add(TransactionItemEntity(transactionId = 2, productId = 7, productName = "Sewa Meja 9ft Reguler (1 Jam)", category = "BILLIARD", unitPrice = 45000.0, costPrice = 5000.0, quantity = 2, totalPrice = 90000.0, totalCost = 10000.0))
        items.add(TransactionItemEntity(transactionId = 2, productId = 9, productName = "Sarung Tangan Billiard Pro", category = "BILLIARD", unitPrice = 35000.0, costPrice = 18000.0, quantity = 1, totalPrice = 35000.0, totalCost = 18000.0))
        cashflows.add(CashflowEntity(timestamp = t2.timestamp, type = "DEBIT", category = "PENJUALAN", businessUnit = "BILLIARD", amount = 125000.0, paymentMethod = "QRIS", description = "Penjualan Billiard #TRX-002", referenceId = t2.invoiceNumber, shiftId = 1))

        // Transaction 3: GOR & Bar mixed (Cash)
        val t3 = TransactionEntity(
            id = 3,
            invoiceNumber = "TRX-${System.currentTimeMillis() % 100000}-003",
            shiftId = 1,
            timestamp = now - (45 * 60 * 1000),
            customerName = "PB Tangkas",
            tableOrOrderRef = "Lap Badminton 1 & 2",
            orderType = "BOOKING",
            paymentMethod = "CASH",
            paymentStatus = "PAID",
            subtotal = 250000.0,
            taxAmount = 0.0,
            discountAmount = 0.0,
            totalAmount = 250000.0,
            totalCostPrice = 111000.0,
            grossProfit = 139000.0,
            cashTendered = 300000.0,
            changeAmount = 50000.0,
            barRevenue = 20000.0,
            billiardRevenue = 0.0,
            gorRevenue = 230000.0
        )
        transactions.add(t3)
        items.add(TransactionItemEntity(transactionId = 3, productId = 12, productName = "Sewa Lapangan Badminton (1 Jam)", category = "GOR", unitPrice = 60000.0, costPrice = 10000.0, quantity = 2, totalPrice = 120000.0, totalCost = 20000.0))
        items.add(TransactionItemEntity(transactionId = 3, productId = 14, productName = "Shuttlecock Tournament (1 Slop / 12 pcs)", category = "GOR", unitPrice = 110000.0, costPrice = 85000.0, quantity = 1, totalPrice = 110000.0, totalCost = 85000.0))
        items.add(TransactionItemEntity(transactionId = 3, productId = 15, productName = "Minuman Isotonik Pocari 500ml", category = "GOR", unitPrice = 10000.0, costPrice = 5500.0, quantity = 2, totalPrice = 20000.0, totalCost = 11000.0))
        cashflows.add(CashflowEntity(timestamp = t3.timestamp, type = "DEBIT", category = "PENJUALAN", businessUnit = "GOR", amount = 250000.0, paymentMethod = "CASH", description = "Sewa GOR PB Tangkas #TRX-003", referenceId = t3.invoiceNumber, shiftId = 1))

        // Transaction 4: Held Order (Simpan Bayar Nanti)
        val tHeld = TransactionEntity(
            id = 4,
            invoiceNumber = "HELD-001",
            shiftId = 1,
            timestamp = now - (15 * 60 * 1000),
            customerName = "Pak Andi & Kawan",
            tableOrOrderRef = "Meja Billiard VIP 01",
            orderType = "DINE_IN",
            paymentMethod = "CASH",
            paymentStatus = "HELD", // Simpan bayar nanti
            subtotal = 142000.0,
            taxAmount = 0.0,
            discountAmount = 0.0,
            totalAmount = 142000.0,
            totalCostPrice = 37000.0,
            grossProfit = 105000.0,
            cashTendered = 0.0,
            changeAmount = 0.0,
            barRevenue = 57000.0,
            billiardRevenue = 85000.0,
            gorRevenue = 0.0
        )
        transactions.add(tHeld)
        items.add(TransactionItemEntity(transactionId = 4, productId = 8, productName = "Sewa Meja VIP Room (1 Jam)", category = "BILLIARD", unitPrice = 85000.0, costPrice = 15000.0, quantity = 1, totalPrice = 85000.0, totalCost = 15000.0))
        items.add(TransactionItemEntity(transactionId = 4, productId = 2, productName = "Iced Caramel Macchiato", category = "BAR", unitPrice = 32000.0, costPrice = 12000.0, quantity = 1, totalPrice = 32000.0, totalCost = 12000.0))
        items.add(TransactionItemEntity(transactionId = 4, productId = 5, productName = "French Fries Truffle", category = "BAR", unitPrice = 27000.0, costPrice = 10000.0, quantity = 1, totalPrice = 27000.0, totalCost = 10000.0))

        // Yesterday's transactions for comparison & calendar selection
        val oneDayMillis = 24 * 3600 * 1000L
        val yesterdayTimestamp = now - oneDayMillis

        val tY1 = TransactionEntity(
            id = 10,
            invoiceNumber = "TRX-YEST-001",
            shiftId = 99,
            timestamp = yesterdayTimestamp - (4 * 3600 * 1000),
            customerName = "Pelanggan Kemarin 1",
            tableOrOrderRef = "Bar 01",
            orderType = "DINE_IN",
            paymentMethod = "CASH",
            paymentStatus = "PAID",
            subtotal = 96000.0,
            taxAmount = 0.0,
            discountAmount = 0.0,
            totalAmount = 96000.0,
            totalCostPrice = 35000.0,
            grossProfit = 61000.0,
            cashTendered = 100000.0,
            changeAmount = 4000.0,
            barRevenue = 96000.0,
            billiardRevenue = 0.0,
            gorRevenue = 0.0
        )
        val tY2 = TransactionEntity(
            id = 11,
            invoiceNumber = "TRX-YEST-002",
            shiftId = 99,
            timestamp = yesterdayTimestamp - (2 * 3600 * 1000),
            customerName = "Club Badminton Jaya",
            tableOrOrderRef = "Lap 1",
            orderType = "BOOKING",
            paymentMethod = "QRIS",
            paymentStatus = "PAID",
            subtotal = 230000.0,
            taxAmount = 0.0,
            discountAmount = 0.0,
            totalAmount = 230000.0,
            totalCostPrice = 105000.0,
            grossProfit = 125000.0,
            cashTendered = 230000.0,
            changeAmount = 0.0,
            barRevenue = 0.0,
            billiardRevenue = 0.0,
            gorRevenue = 230000.0
        )
        transactions.add(tY1)
        transactions.add(tY2)
        items.add(TransactionItemEntity(transactionId = 10, productId = 2, productName = "Iced Caramel Macchiato", category = "BAR", unitPrice = 32000.0, costPrice = 12000.0, quantity = 3, totalPrice = 96000.0, totalCost = 36000.0))
        items.add(TransactionItemEntity(transactionId = 11, productId = 12, productName = "Sewa Lapangan Badminton (1 Jam)", category = "GOR", unitPrice = 60000.0, costPrice = 10000.0, quantity = 2, totalPrice = 120000.0, totalCost = 20000.0))
        items.add(TransactionItemEntity(transactionId = 11, productId = 14, productName = "Shuttlecock Tournament (1 Slop / 12 pcs)", category = "GOR", unitPrice = 110000.0, costPrice = 85000.0, quantity = 1, totalPrice = 110000.0, totalCost = 85000.0))

        cashflows.add(CashflowEntity(timestamp = tY1.timestamp, type = "DEBIT", category = "PENJUALAN", businessUnit = "BAR", amount = 96000.0, paymentMethod = "CASH", description = "Penjualan Bar Kemarin", referenceId = tY1.invoiceNumber))
        cashflows.add(CashflowEntity(timestamp = tY2.timestamp, type = "DEBIT", category = "PENJUALAN", businessUnit = "GOR", amount = 230000.0, paymentMethod = "QRIS", description = "Sewa GOR Kemarin", referenceId = tY2.invoiceNumber))

        // Stock in logs (Bahan Baku)
        val s1 = StockInLogEntity(
            id = 1,
            productId = 1,
            productName = "Espresso Single Origin Beans",
            category = "BAR",
            timestamp = now - (5 * 3600 * 1000),
            supplierName = "PT Kopi Nusantara Mandiri",
            quantity = 50,
            unitPrice = 8000.0,
            totalCost = 400000.0,
            paymentSource = "KAS_LACI",
            notes = "Restock biji kopi arabika single origin 1kg"
        )
        val s2 = StockInLogEntity(
            id = 2,
            productId = 14,
            productName = "Shuttlecock Tournament Slop",
            category = "GOR",
            timestamp = now - (24 * 3600 * 1000),
            supplierName = "Yonex Sports Distributor",
            quantity = 10,
            unitPrice = 85000.0,
            totalCost = 850000.0,
            paymentSource = "BANK_TRANSFER",
            notes = "Pengadaan kok turnamen bulanan"
        )
        stockLogs.add(s1)
        stockLogs.add(s2)

        // Corresponding Cashflow for s1 (Expense / Kredit)
        cashflows.add(
            CashflowEntity(
                timestamp = s1.timestamp,
                type = "KREDIT",
                category = "BELANJA_STOK",
                businessUnit = "BAR",
                amount = 400000.0,
                paymentMethod = "CASH",
                description = "Belanja Bahan: Espresso Beans (50 cup)",
                referenceId = "STOCK-001",
                shiftId = 1
            )
        )
        cashflows.add(
            CashflowEntity(
                timestamp = s2.timestamp,
                type = "KREDIT",
                category = "BELANJA_STOK",
                businessUnit = "GOR",
                amount = 850000.0,
                paymentMethod = "BANK",
                description = "Pembelian Kok Turnamen (10 slop)",
                referenceId = "STOCK-002"
            )
        )

        // 4. Initial Double-Entry Journal Entries (Jurnal Umum)
        val journalEntries = mutableListOf<JournalEntryEntity>()
        journalEntries.add(
            JournalEntryEntity(
                entryNumber = "JU-2026-001",
                timestamp = now - 86400000L * 2,
                accountCode = "101-KAS",
                accountName = "Kas & Bank Operasional",
                description = "Setoran Modal Awal Pemilik",
                debit = 15000000L,
                credit = 0L,
                unitCategory = "UMUM",
                authorizedBy = "Owner"
            )
        )
        journalEntries.add(
            JournalEntryEntity(
                entryNumber = "JU-2026-001",
                timestamp = now - 86400000L * 2,
                accountCode = "301-MODAL",
                accountName = "Modal Usaha Tumuwuh",
                description = "Setoran Modal Awal Pemilik",
                debit = 0L,
                credit = 15000000L,
                unitCategory = "UMUM",
                authorizedBy = "Owner"
            )
        )
        journalEntries.add(
            JournalEntryEntity(
                entryNumber = "JU-2026-002",
                timestamp = now - 86400000L,
                accountCode = "102-KAS-BAR",
                accountName = "Kas Bar Café",
                description = "Penerimaan Kas Penjualan Bar & Kopi",
                debit = 3450000L,
                credit = 0L,
                unitCategory = "BAR",
                authorizedBy = "Supervisor"
            )
        )
        journalEntries.add(
            JournalEntryEntity(
                entryNumber = "JU-2026-002",
                timestamp = now - 86400000L,
                accountCode = "401-REV-BAR",
                accountName = "Pendapatan Penjualan Bar",
                description = "Pendapatan Penjualan Bar & Kopi",
                debit = 0L,
                credit = 3450000L,
                unitCategory = "BAR",
                authorizedBy = "Supervisor"
            )
        )
        journalEntries.add(
            JournalEntryEntity(
                entryNumber = "JU-2026-003",
                timestamp = now - 3600000L * 5,
                accountCode = "501-HPP-BAR",
                accountName = "Beban Pokok Penjualan (HPP)",
                description = "Pembelian Bahan Baku Biji Kopi Arabika",
                debit = 1250000L,
                credit = 0L,
                unitCategory = "BAR",
                authorizedBy = "Owner"
            )
        )
        journalEntries.add(
            JournalEntryEntity(
                entryNumber = "JU-2026-003",
                timestamp = now - 3600000L * 5,
                accountCode = "101-KAS",
                accountName = "Kas & Bank Operasional",
                description = "Pembayaran Tunai Biji Kopi Supplier",
                debit = 0L,
                credit = 1250000L,
                unitCategory = "BAR",
                authorizedBy = "Owner"
            )
        )

        // 5. Initial Supplier Purchase Orders (PO)
        val supplierOrders = listOf(
            SupplierOrderEntity(
                id = 1,
                poNumber = "PO-TUM-2026-081",
                supplierName = "PT Roastery Nusantara Prima",
                supplierEmail = "order@roasterynusantara.id",
                supplierPhone = "081298887722",
                category = "BAR",
                orderDate = now - 86400000L * 3,
                dueDate = now + 86400000L * 4,
                itemsSummary = "10kg Specialty Arabika Gayo (Rp 1.800.000), 20L Fresh Milk Barista (Rp 400.000)",
                totalAmount = 2200000L,
                status = "SENT",
                notes = "Harap roasting level medium roast untuk espresso blend."
            ),
            SupplierOrderEntity(
                id = 2,
                poNumber = "PO-TUM-2026-082",
                supplierName = "Billiard Pro Supply Bandung",
                supplierEmail = "sales@billiardpro.co.id",
                supplierPhone = "081322445566",
                category = "BILLIARD",
                orderDate = now - 86400000L * 2,
                dueDate = now + 86400000L * 7,
                itemsSummary = "5 Set Master Chalk Blue (Rp 250.000), 10 Tip Kamui Original (Rp 1.100.000)",
                totalAmount = 1350000L,
                status = "RECEIVED",
                notes = "Barang sudah diterima & lolos QC supervisor Billiard."
            ),
            SupplierOrderEntity(
                id = 3,
                poNumber = "PO-TUM-2026-083",
                supplierName = "Sportindo Shuttlecock Distributor",
                supplierEmail = "distributor@sportindo.com",
                supplierPhone = "081177663322",
                category = "GOR",
                orderDate = now - 86400000L,
                dueDate = now + 86400000L * 2,
                itemsSummary = "20 Slop Kok Garuda International Gold Speed 78",
                totalAmount = 2400000L,
                status = "DRAFT",
                notes = "Pengadaan rutin persiapan turnamen bulanan GOR Badminton."
            )
        )

        // 6. Initial CRM Customer Insights & Loyalty Points
        val customers = listOf(
            CustomerProfileEntity(
                id = 1,
                customerName = "Bima Arya",
                phone = "081234567890",
                email = "bima.arya@gmail.com",
                tier = "VIP",
                loyaltyPoints = 340,
                favoriteCategory = "BAR",
                favoriteItem = "Iced Caramel Macchiato",
                totalSpent = 3400000L,
                visitCount = 28,
                activeCoupons = "DISKONVIP20:Potongan 20%, NGOPIHEMAT:Diskon Rp15.000"
            ),
            CustomerProfileEntity(
                id = 2,
                customerName = "Rian Hidayat",
                phone = "081398765432",
                email = "rian.billiard@yahoo.com",
                tier = "GOLD",
                loyaltyPoints = 210,
                favoriteCategory = "BILLIARD",
                favoriteItem = "Sewa Meja VIP 9ft (3 Jam)",
                totalSpent = 2100000L,
                visitCount = 14,
                activeCoupons = "BILLIARD1JAM:Gratis 1 Jam Main"
            ),
            CustomerProfileEntity(
                id = 3,
                customerName = "Komunitas PB Tangkas",
                phone = "081809090909",
                email = "pb.tangkas@badminton.id",
                tier = "VIP",
                loyaltyPoints = 480,
                favoriteCategory = "GOR",
                favoriteItem = "Sewa Lapangan Karpet Vinyl",
                totalSpent = 4800000L,
                visitCount = 36,
                activeCoupons = "MEMBERGOR20:Cashback Poin 20%"
            ),
            CustomerProfileEntity(
                id = 4,
                customerName = "Sarah Melinda",
                phone = "081912344321",
                email = "sarah.m@gmail.com",
                tier = "SILVER",
                loyaltyPoints = 85,
                favoriteCategory = "BAR",
                favoriteItem = "Matcha Fusion Latte",
                totalSpent = 850000L,
                visitCount = 7,
                activeCoupons = "WELCOME10K:Voucher Rp10.000"
            )
        )

        // 7. Initial Marketplace Orders (Tokopedia & Shopee)
        val marketplaceOrders = listOf(
            MarketplaceOrderEntity(
                id = 1,
                marketplace = "TOKOPEDIA",
                orderNumber = "INV/20260828/TKP/889211",
                buyerName = "Dimas Wicaksono",
                orderDate = now - 3600000L * 12,
                itemsSummary = "2x Tumuwuh House Blend Coffee Beans 250g",
                totalPrice = 180000L,
                ppnAmount = 19800L,
                pphAmount = 900L,
                status = "COMPLETED",
                isEFakturIntegrated = true
            ),
            MarketplaceOrderEntity(
                id = 2,
                marketplace = "SHOPEE",
                orderNumber = "260828SHP819920",
                buyerName = "Anisa Putri",
                orderDate = now - 3600000L * 6,
                itemsSummary = "1x Cold Brew Concentrate 1L + 1x Tumbler Tumuwuh Special Edition",
                totalPrice = 245000L,
                ppnAmount = 26950L,
                pphAmount = 1225L,
                status = "PROCESSED",
                isEFakturIntegrated = true
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
