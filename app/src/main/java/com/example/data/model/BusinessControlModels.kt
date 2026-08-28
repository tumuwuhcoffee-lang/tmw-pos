package com.example.data.model

data class FinancialStatementSummary(
    val totalRevenue: Double,
    val totalHpp: Double,
    val grossProfit: Double,
    val totalOperationalExpenses: Double,
    val netProfit: Double,
    val grossMarginPercentage: Double,
    val netMarginPercentage: Double,
    
    // Balance Sheet (Neraca)
    val totalCashAndBank: Double,
    val totalAccountsReceivable: Double, // Piutang
    val totalInventoryValue: Double,     // Persediaan Barang
    val totalCurrentAssets: Double,
    val totalAccountsPayable: Double,   // Hutang Supplier (PO pending)
    val ownerEquity: Double,
    val retainedEarnings: Double,
    val totalLiabilitiesAndEquity: Double,
    
    // Financial Ratios
    val currentRatio: Double,
    val quickRatio: Double,
    val inventoryTurnover: Double
)

data class TaxReportSummary(
    val taxableRevenue: Double,
    val ppnRate: Double = 11.0, // 11%
    val totalPpn: Double,
    val pphFinalRate: Double = 0.5, // 0.5% UMKM PP 55/2022
    val totalPphFinal: Double,
    val eFakturInvoiceCount: Int
)

data class BusinessNotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "INFO" // ALERT, SUCCESS, WARNING, INFO
)
