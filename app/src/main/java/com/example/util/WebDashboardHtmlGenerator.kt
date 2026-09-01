package com.example.util

import com.example.data.local.entity.CashflowEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity
import com.example.data.model.FinancialStatementSummary
import com.example.data.model.TaxReportSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WebDashboardHtmlGenerator {

    fun generateDashboardHtml(
        transactions: List<TransactionEntity>,
        transactionItems: List<TransactionItemEntity>,
        cashflows: List<CashflowEntity>,
        products: List<ProductEntity>,
        financialSummary: FinancialStatementSummary,
        taxSummary: TaxReportSummary
    ): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        val dateOnlyFormat = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
        val timeOnlyFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        val generatedAt = dateFormat.format(Date())

        val paidTransactions = transactions.filter { it.paymentStatus == "PAID" }
        val totalRevenue = paidTransactions.sumOf { it.totalAmount }
        val totalTrxCount = paidTransactions.size
        val avgOrderValue = if (totalTrxCount > 0) totalRevenue / totalTrxCount else 0.0

        val totalInflow = cashflows.filter { it.type == "DEBIT" }.sumOf { it.amount }
        val totalOutflow = cashflows.filter { it.type == "KREDIT" }.sumOf { it.amount }
        val netCashflow = totalInflow - totalOutflow

        val barRev = paidTransactions.sumOf { it.barRevenue }
        val billiardRev = paidTransactions.sumOf { it.billiardRevenue }
        val gorRev = paidTransactions.sumOf { it.gorRevenue }

        val barPct = if (totalRevenue > 0) (barRev / totalRevenue) * 100 else 0.0
        val billiardPct = if (totalRevenue > 0) (billiardRev / totalRevenue) * 100 else 0.0
        val gorPct = if (totalRevenue > 0) (gorRev / totalRevenue) * 100 else 0.0

        // Expenses breakdown by category
        val expenseByCategory: Map<String, Double> = cashflows
            .filter { it.type == "KREDIT" }
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { cf -> cf.amount } }

        data class TopItemStats(
            val name: String,
            val quantity: Int,
            val revenue: Double
        )

        // Top 5 products by quantity sold
        val itemQtyMap: List<TopItemStats> = transactionItems.groupBy { it.productId }
            .map { entry ->
                val list = entry.value
                val qty = list.sumOf { it.quantity }
                val rev = list.sumOf { it.totalPrice }
                val prodName = list.firstOrNull()?.productName ?: "Produk #${entry.key}"
                TopItemStats(name = prodName, quantity = qty, revenue = rev)
            }
            .sortedByDescending { it.quantity }
            .take(5)

        val htmlBuilder = StringBuilder()
        htmlBuilder.append("""
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tumuwuh POS - Live Web Dashboard</title>
    <style>
        :root {
            --primary: #78350F;
            --primary-light: #FEF3C7;
            --accent: #FBBF24;
            --dark: #0F172A;
            --dark-surface: #1E293B;
            --card-bg: #FFFFFF;
            --text-main: #1E293B;
            --text-muted: #64748B;
            --border: #E2E8F0;
            --emerald: #059669;
            --emerald-bg: #D1FAE5;
            --rose: #E11D48;
            --rose-bg: #FFE4E6;
            --blue: #2563EB;
            --blue-bg: #DBEAFE;
            --purple: #7C3AED;
            --purple-bg: #EDE9FE;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
        }

        body {
            background-color: #F8FAFC;
            color: var(--text-main);
            padding: 16px;
            font-size: 13px;
        }

        /* Top Header Navbar */
        .navbar {
            background: linear-gradient(135deg, #0F172A 0%, #1E293B 100%);
            color: #FFFFFF;
            border-radius: 16px;
            padding: 16px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
            box-shadow: 0 4px 20px rgba(15, 23, 42, 0.15);
            margin-bottom: 20px;
        }

        .brand-info {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .brand-logo {
            width: 44px;
            height: 44px;
            background: linear-gradient(135deg, #FBBF24 0%, #D97706 100%);
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 800;
            font-size: 20px;
            color: #78350F;
            box-shadow: 0 2px 10px rgba(251, 191, 36, 0.4);
        }

        .brand-titles h1 {
            font-size: 17px;
            font-weight: 800;
            letter-spacing: 0.3px;
        }

        .brand-titles p {
            font-size: 11px;
            color: #94A3B8;
        }

        .live-badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: rgba(5, 150, 105, 0.2);
            border: 1px solid rgba(52, 211, 153, 0.4);
            color: #34D399;
            padding: 4px 10px;
            border-radius: 20px;
            font-size: 10px;
            font-weight: 700;
            letter-spacing: 0.5px;
        }

        .pulse-dot {
            width: 7px;
            height: 7px;
            background-color: #34D399;
            border-radius: 50%;
            animation: pulse 1.5s infinite;
        }

        @keyframes pulse {
            0% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(52, 211, 153, 0.7); }
            70% { transform: scale(1); box-shadow: 0 0 0 6px rgba(52, 211, 153, 0); }
            100% { transform: scale(0.95); box-shadow: 0 0 0 0 rgba(52, 211, 153, 0); }
        }

        .header-actions {
            display: flex;
            align-items: center;
            gap: 8px;
            flex-wrap: wrap;
        }

        .btn {
            background: #334155;
            color: #FFFFFF;
            border: none;
            padding: 8px 14px;
            border-radius: 8px;
            font-size: 11px;
            font-weight: 600;
            cursor: pointer;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            transition: all 0.2s ease;
        }

        .btn:hover {
            background: #475569;
        }

        .btn-gold {
            background: #FBBF24;
            color: #78350F;
            font-weight: 700;
        }

        .btn-gold:hover {
            background: #F59E0B;
        }

        /* KPI Cards Grid */
        .kpi-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
            gap: 14px;
            margin-bottom: 20px;
        }

        .kpi-card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 16px;
            position: relative;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.03);
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .kpi-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 16px rgba(0,0,0,0.06);
        }

        .kpi-card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8px;
        }

        .kpi-title {
            font-size: 11px;
            font-weight: 600;
            color: var(--text-muted);
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .kpi-icon {
            width: 32px;
            height: 32px;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 15px;
        }

        .kpi-value {
            font-size: 20px;
            font-weight: 800;
            color: var(--text-main);
            margin-bottom: 4px;
        }

        .kpi-subtext {
            font-size: 10px;
            color: var(--text-muted);
        }

        .kpi-card.revenue { border-left: 4px solid var(--primary); }
        .kpi-card.inflow { border-left: 4px solid var(--emerald); }
        .kpi-card.outflow { border-left: 4px solid var(--rose); }
        .kpi-card.net { border-left: 4px solid var(--blue); }
        .kpi-card.balance { border-left: 4px solid var(--purple); }

        /* Main Dashboard Sections */
        .dashboard-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 16px;
            margin-bottom: 20px;
        }

        @media (max-width: 900px) {
            .dashboard-grid {
                grid-template-columns: 1fr;
            }
        }

        .panel {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 14px;
            padding: 18px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.03);
            margin-bottom: 16px;
        }

        .panel-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 14px;
            padding-bottom: 10px;
            border-bottom: 1px solid var(--border);
        }

        .panel-title {
            font-size: 14px;
            font-weight: 700;
            color: var(--text-main);
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .tag {
            font-size: 10px;
            font-weight: 700;
            padding: 3px 8px;
            border-radius: 6px;
        }

        /* Business Unit Breakdown Bars */
        .unit-breakdown {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .unit-row {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .unit-info {
            display: flex;
            justify-content: space-between;
            font-size: 11px;
            font-weight: 600;
        }

        .progress-bar-bg {
            width: 100%;
            height: 8px;
            background: #E2E8F0;
            border-radius: 4px;
            overflow: hidden;
        }

        .progress-bar-fill {
            height: 100%;
            border-radius: 4px;
        }

        .fill-bar { background: #D97706; }
        .fill-billiard { background: #7C3AED; }
        .fill-gor { background: #2563EB; }

        /* Tables */
        .table-responsive {
            width: 100%;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 11px;
        }

        th {
            background: #F1F5F9;
            color: var(--text-muted);
            font-weight: 700;
            text-align: left;
            padding: 9px 10px;
            border-bottom: 1px solid var(--border);
        }

        td {
            padding: 10px;
            border-bottom: 1px solid #F1F5F9;
            color: var(--text-main);
        }

        tr:hover td {
            background-color: #F8FAFC;
        }

        .status-pill {
            display: inline-block;
            padding: 2px 7px;
            border-radius: 12px;
            font-size: 9px;
            font-weight: 700;
        }

        .pill-paid { background: var(--emerald-bg); color: var(--emerald); }
        .pill-held { background: var(--primary-light); color: var(--primary); }
        .pill-debit { background: var(--emerald-bg); color: var(--emerald); font-weight: 800; }
        .pill-kredit { background: var(--rose-bg); color: var(--rose); font-weight: 800; }

        .footer {
            text-align: center;
            padding: 20px 0;
            color: var(--text-muted);
            font-size: 11px;
        }
    </style>
</head>
<body>

    <!-- NAVBAR HEADER -->
    <header class="navbar">
        <div class="brand-info">
            <div class="brand-logo">T</div>
            <div class="brand-titles">
                <h1>TUMUWUH CAFÉ & SPORTS HUB</h1>
                <p>Website Dashboard Terintegrasi Cloud • Live Monitoring</p>
            </div>
            <div class="live-badge">
                <div class="pulse-dot"></div>
                DATABASE LIVE ONLINE
            </div>
        </div>

        <div class="header-actions">
            <button class="btn btn-gold" onclick="downloadHtmlReport()">📥 Download File HTML</button>
            <button class="btn" onclick="downloadCsvReport()">📊 Download CSV / Excel</button>
            <button class="btn" onclick="window.print()">🖨️ Cetak / PDF</button>
            <button class="btn" onclick="copyExecutiveSummary()">📋 Salin Ringkasan</button>
        </div>
    </header>

    <!-- OFFLINE / ONLINE READY NOTIFICATION BANNER -->
    <div style="background: #1E293B; border: 1px solid #334155; border-radius: 12px; padding: 12px 16px; margin-bottom: 16px; display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px;">
        <div style="display: flex; align-items: center; gap: 10px;">
            <div style="width: 28px; height: 28px; border-radius: 8px; background: #059669; display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; font-size: 14px;">✓</div>
            <div>
                <strong style="color: #FFFFFF; font-size: 12px;">Portal Dashboard Siap Diakses & Diunduh Offline Maupun Online</strong>
                <p style="color: #94A3B8; font-size: 11px; margin-top: 2px;">Seluruh data penjualan, arus kas, dan laba rugi disimpan secara aman. Anda dapat mengunduh file HTML mandiri atau spreadsheet Excel kapan saja.</p>
            </div>
        </div>
        <div style="display: flex; gap: 8px;">
            <button class="btn btn-gold" style="font-size: 10px; padding: 6px 10px;" onclick="downloadHtmlReport()">💾 Simpan File .HTML</button>
            <button class="btn" style="font-size: 10px; padding: 6px 10px;" onclick="downloadCsvReport()">📑 Ekspor .CSV</button>
        </div>
    </div>

    <!-- TOP KPI METRICS CARDS -->
    <section class="kpi-grid">
        <!-- 1. Penjualan Ril -->
        <div class="kpi-card revenue">
            <div class="kpi-card-header">
                <span class="kpi-title">Penjualan Ril (Omset)</span>
                <div class="kpi-icon" style="background: var(--primary-light); color: var(--primary);">☕</div>
            </div>
            <div class="kpi-value">${FormatUtils.formatRupiah(totalRevenue)}</div>
            <div class="kpi-subtext"><strong>$totalTrxCount</strong> Transaksi Lunas • Rata-rata ${FormatUtils.formatRupiah(avgOrderValue)}</div>
        </div>

        <!-- 2. Total Pemasukan / Inflow -->
        <div class="kpi-card inflow">
            <div class="kpi-card-header">
                <span class="kpi-title">Total Pemasukan (Inflow)</span>
                <div class="kpi-icon" style="background: var(--emerald-bg); color: var(--emerald);">📥</div>
            </div>
            <div class="kpi-value" style="color: var(--emerald);">${FormatUtils.formatRupiah(totalInflow)}</div>
            <div class="kpi-subtext">Dari Penjualan, Top Up & Modal Awal</div>
        </div>

        <!-- 3. Total Pengeluaran / Outflow -->
        <div class="kpi-card outflow">
            <div class="kpi-card-header">
                <span class="kpi-title">Total Pengeluaran (Outflow)</span>
                <div class="kpi-icon" style="background: var(--rose-bg); color: var(--rose);">📤</div>
            </div>
            <div class="kpi-value" style="color: var(--rose);">${FormatUtils.formatRupiah(totalOutflow)}</div>
            <div class="kpi-subtext">HPP, Belanja Stok, Listrik, Gaji & Ops</div>
        </div>

        <!-- 4. Arus Kas Bersih (Net Cashflow) -->
        <div class="kpi-card net">
            <div class="kpi-card-header">
                <span class="kpi-title">Net Cashflow</span>
                <div class="kpi-icon" style="background: var(--blue-bg); color: var(--blue);">📊</div>
            </div>
            <div class="kpi-value" style="color: ${if (netCashflow >= 0) "var(--emerald)" else "var(--rose)"};">
                ${if (netCashflow < 0) "- " else ""}${FormatUtils.formatRupiah(Math.abs(netCashflow))}
            </div>
            <div class="kpi-subtext">${if (netCashflow >= 0) "Surplus Arus Kas Operasional" else "Defisit Kas Sementara"}</div>
        </div>

        <!-- 5. Saldo Kas & Bank -->
        <div class="kpi-card balance">
            <div class="kpi-card-header">
                <span class="kpi-title">Saldo Kas & Bank</span>
                <div class="kpi-icon" style="background: var(--purple-bg); color: var(--purple);">🏦</div>
            </div>
            <div class="kpi-value" style="color: var(--purple);">${FormatUtils.formatRupiah(financialSummary.totalCashAndBank)}</div>
            <div class="kpi-subtext">Current Ratio: <strong>${String.format(Locale.getDefault(), "%.2f", financialSummary.currentRatio)}x</strong></div>
        </div>
    </section>

    <!-- DASHBOARD MIDDLE CONTENT GRID -->
    <div class="dashboard-grid">
        <!-- LEFT COLUMN: REAL-TIME SALES FEED & REVENUE SPLIT -->
        <div>
            <!-- Unit Bisnis Revenue Contribution Panel -->
            <div class="panel">
                <div class="panel-header">
                    <div class="panel-title">
                        <span>🏛️ Kontribusi Omset per Unit Bisnis</span>
                    </div>
                    <span class="tag" style="background: var(--primary-light); color: var(--primary);">Real-time POS</span>
                </div>

                <div class="unit-breakdown">
                    <div class="unit-row">
                        <div class="unit-info">
                            <span>☕ Tumuwuh Coffee & Kitchen (Bar)</span>
                            <span>${FormatUtils.formatRupiah(barRev)} (${String.format(Locale.getDefault(), "%.1f", barPct)}%)</span>
                        </div>
                        <div class="progress-bar-bg">
                            <div class="progress-bar-fill fill-bar" style="width: ${barPct}%;"></div>
                        </div>
                    </div>

                    <div class="unit-row">
                        <div class="unit-info">
                            <span>🎱 Tumuwuh Billiard Arena (10 Meja)</span>
                            <span>${FormatUtils.formatRupiah(billiardRev)} (${String.format(Locale.getDefault(), "%.1f", billiardPct)}%)</span>
                        </div>
                        <div class="progress-bar-bg">
                            <div class="progress-bar-fill fill-billiard" style="width: ${billiardPct}%;"></div>
                        </div>
                    </div>

                    <div class="unit-row">
                        <div class="unit-info">
                            <span>🏸 Tumuwuh GOR Badminton (3 Lapangan)</span>
                            <span>${FormatUtils.formatRupiah(gorRev)} (${String.format(Locale.getDefault(), "%.1f", gorPct)}%)</span>
                        </div>
                        <div class="progress-bar-bg">
                            <div class="progress-bar-fill fill-gor" style="width: ${gorPct}%;"></div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tabel Penjualan Ril Terbaru -->
            <div class="panel">
                <div class="panel-header">
                    <div class="panel-title">
                        <span>🧾 Daftar Transaksi Penjualan Ril Terkini</span>
                    </div>
                    <span class="tag" style="background: var(--emerald-bg); color: var(--emerald);">${transactions.size} Transaksi</span>
                </div>

                <div class="table-responsive">
                    <table>
                        <thead>
                            <tr>
                                <th>Invoice</th>
                                <th>Waktu</th>
                                <th>Pelanggan / Ref</th>
                                <th>Metode</th>
                                <th>Total</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
        """)

        transactions.take(15).forEach { trx ->
            val statusClass = if (trx.paymentStatus == "PAID") "pill-paid" else "pill-held"
            val timeFormatted = dateFormat.format(Date(trx.timestamp))
            htmlBuilder.append("""
                            <tr>
                                <td><strong>${trx.invoiceNumber}</strong></td>
                                <td style="color: var(--text-muted);">$timeFormatted</td>
                                <td>${trx.customerName ?: trx.tableOrOrderRef ?: "Pelanggan Umum"}</td>
                                <td><span style="font-weight:600;">${trx.paymentMethod}</span></td>
                                <td><strong>${FormatUtils.formatRupiah(trx.totalAmount)}</strong></td>
                                <td><span class="status-pill $statusClass">${trx.paymentStatus}</span></td>
                            </tr>
            """)
        }

        htmlBuilder.append("""
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- RIGHT COLUMN: CASHFLOW, EXPENSES & TOP PRODUCTS -->
        <div>
            <!-- Financial Profit & Loss Summary Card -->
            <div class="panel">
                <div class="panel-header">
                    <div class="panel-title">
                        <span>📈 Ringkasan Laba Rugi & Pajak</span>
                    </div>
                </div>

                <div style="display:flex; flex-direction:column; gap:8px; font-size:12px;">
                    <div style="display:flex; justify-content:space-between;">
                        <span style="color:var(--text-muted);">Pendapatan Kotor:</span>
                        <strong>${FormatUtils.formatRupiah(financialSummary.totalRevenue)}</strong>
                    </div>
                    <div style="display:flex; justify-content:space-between;">
                        <span style="color:var(--text-muted);">Beban Pokok (HPP):</span>
                        <strong style="color:var(--rose);">- ${FormatUtils.formatRupiah(financialSummary.totalHpp)}</strong>
                    </div>
                    <div style="display:flex; justify-content:space-between; border-top:1px dashed var(--border); padding-top:6px;">
                        <span>Laba Kotor (Gross):</span>
                        <strong>${FormatUtils.formatRupiah(financialSummary.grossProfit)}</strong>
                    </div>
                    <div style="display:flex; justify-content:space-between;">
                        <span style="color:var(--text-muted);">Beban Operasional:</span>
                        <strong style="color:var(--rose);">- ${FormatUtils.formatRupiah(financialSummary.totalOperationalExpenses)}</strong>
                    </div>
                    <div style="display:flex; justify-content:space-between; background:var(--emerald-bg); padding:8px 10px; border-radius:8px; margin-top:4px;">
                        <strong style="color:var(--emerald);">Laba Bersih (Net Profit):</strong>
                        <strong style="color:var(--emerald);">${FormatUtils.formatRupiah(financialSummary.netProfit)}</strong>
                    </div>
                    <div style="display:flex; justify-content:space-between; font-size:10px; color:var(--text-muted); margin-top:4px;">
                        <span>Estimasi PPN (11%): ${FormatUtils.formatRupiah(taxSummary.totalPpn)}</span>
                        <span>PPh Final (0.5%): ${FormatUtils.formatRupiah(taxSummary.totalPphFinal)}</span>
                    </div>
                </div>
            </div>

            <!-- Top 5 Best Sellers -->
            <div class="panel">
                <div class="panel-header">
                    <div class="panel-title">
                        <span>🏆 Menu & Produk Terlaris</span>
                    </div>
                </div>

                <div style="display:flex; flex-direction:column; gap:8px;">
        """)

        if (itemQtyMap.isEmpty()) {
            htmlBuilder.append("""<p style="color:var(--text-muted); text-align:center;">Belum ada item terjual.</p>""")
        } else {
            itemQtyMap.forEachIndexed { idx, item ->
                htmlBuilder.append("""
                    <div style="display:flex; justify-content:space-between; align-items:center; padding:6px 0; border-bottom:1px solid #F1F5F9;">
                        <div style="display:flex; align-items:center; gap:8px;">
                            <span style="font-weight:800; color:var(--primary); font-size:12px;">#0${idx + 1}</span>
                            <div>
                                <div style="font-weight:600; font-size:11px;">${item.name}</div>
                                <div style="font-size:10px; color:var(--text-muted);">${item.quantity} porsi / jam</div>
                            </div>
                        </div>
                        <strong style="font-size:11px;">${FormatUtils.formatRupiah(item.revenue)}</strong>
                    </div>
                """)
            }
        }

        htmlBuilder.append("""
                </div>
            </div>

            <!-- Pengeluaran Operasional Berdasarkan Kategori -->
            <div class="panel">
                <div class="panel-header">
                    <div class="panel-title">
                        <span>💸 Struktur Pengeluaran Kas</span>
                    </div>
                </div>

                <div style="display:flex; flex-direction:column; gap:8px;">
        """)

        if (expenseByCategory.isEmpty()) {
            htmlBuilder.append("""<p style="color:var(--text-muted); text-align:center;">Belum ada pengeluaran kas.</p>""")
        } else {
            expenseByCategory.forEach { (cat, amt) ->
                val pct = if (totalOutflow > 0) (amt / totalOutflow) * 100 else 0.0
                htmlBuilder.append("""
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <span style="color:var(--text-main); font-weight:600; font-size:11px;">$cat</span>
                        <span style="color:var(--rose); font-weight:700;">${FormatUtils.formatRupiah(amt)} <span style="font-size:9px; color:var(--text-muted);">(${String.format(Locale.getDefault(), "%.0f", pct)}%)</span></span>
                    </div>
                """)
            }
        }

        htmlBuilder.append("""
                </div>
            </div>
        </div>
    </div>

    <!-- FULL CASHFLOW LOG TABLE -->
    <div class="panel">
        <div class="panel-header">
            <div class="panel-title">
                <span>📑 Buku Arus Kas Masuk & Keluar (Pemasukan & Pengeluaran Ril)</span>
            </div>
            <span class="tag" style="background: var(--blue-bg); color: var(--blue);">${cashflows.size} Transaksi Kas</span>
        </div>

        <div class="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>Waktu</th>
                        <th>Tipe</th>
                        <th>Kategori</th>
                        <th>Unit Bisnis</th>
                        <th>Keterangan</th>
                        <th>Metode</th>
                        <th>Nominal</th>
                    </tr>
                </thead>
                <tbody>
        """)

        cashflows.take(25).forEach { cf ->
            val isDebit = cf.type == "DEBIT"
            val pillClass = if (isDebit) "pill-debit" else "pill-kredit"
            val typeLabel = if (isDebit) "+ MASUK" else "- KELUAR"
            val amtColor = if (isDebit) "var(--emerald)" else "var(--rose)"
            val timeFormatted = dateFormat.format(Date(cf.timestamp))

            htmlBuilder.append("""
                <tr>
                    <td style="color: var(--text-muted);">$timeFormatted</td>
                    <td><span class="status-pill $pillClass">$typeLabel</span></td>
                    <td><strong>${cf.category}</strong></td>
                    <td>${cf.businessUnit}</td>
                    <td>${cf.description}</td>
                    <td>${cf.paymentMethod}</td>
                    <td><strong style="color: $amtColor;">${if (!isDebit) "- " else "+ "}${FormatUtils.formatRupiah(cf.amount)}</strong></td>
                </tr>
            """)
        }

        htmlBuilder.append("""
                </tbody>
            </table>
        </div>
    </div>

    <!-- FOOTER -->
    <footer class="footer">
        <p><strong>Tumuwuh Cloud Database & ERP System</strong> • Data Diperbarui: $generatedAt</p>
        <p style="margin-top: 4px; color: #94A3B8;">Otorisasi: Owner & Management • Dashboard Live Website • Email: tumuwuhcoffee@gmail.com</p>
    </footer>

    <!-- EMBEDDED INTERACTIVE ACTIONS SCRIPT -->
    <script>
        function showNotification(msg) {
            let toast = document.getElementById('web-toast');
            if (!toast) {
                toast = document.createElement('div');
                toast.id = 'web-toast';
                toast.style.position = 'fixed';
                toast.style.bottom = '24px';
                toast.style.right = '24px';
                toast.style.background = '#059669';
                toast.style.color = '#FFFFFF';
                toast.style.padding = '12px 20px';
                toast.style.borderRadius = '10px';
                toast.style.fontWeight = 'bold';
                toast.style.boxShadow = '0 10px 25px rgba(0,0,0,0.5)';
                toast.style.zIndex = '9999';
                toast.style.transition = 'all 0.3s ease';
                document.body.appendChild(toast);
            }
            toast.innerText = msg;
            toast.style.opacity = '1';
            toast.style.display = 'block';
            setTimeout(() => {
                toast.style.opacity = '0';
                setTimeout(() => { toast.style.display = 'none'; }, 300);
            }, 3000);
        }

        function downloadHtmlReport() {
            try {
                const htmlContent = "<!DOCTYPE html>\n" + document.documentElement.outerHTML;
                const blob = new Blob([htmlContent], { type: 'text/html;charset=utf-8;' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'tumuwuh_dashboard_live_' + new Date().toISOString().slice(0,10) + '.html';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
                showNotification('✅ File Dashboard HTML berhasil diunduh!');
            } catch(e) {
                alert('Gagal mendownload HTML: ' + e.message);
            }
        }

        function downloadCsvReport() {
            try {
                let csv = '\uFEFF'; // UTF-8 BOM
                csv += "LAPORAN PENJUALAN & TRANSAKSI - TUMUWUH POS\n";
                csv += "Tanggal Unduh," + new Date().toLocaleString('id-ID') + "\n\n";
                csv += "No Struk,Waktu,Status,Pelanggan,Meja/Ref,Unit Bisnis,Total Bayar,Metode Bayar\n";

                const rows = document.querySelectorAll('.table-responsive table tbody tr');
                rows.forEach(r => {
                    const cols = r.querySelectorAll('td');
                    if (cols.length >= 6) {
                        const line = Array.from(cols).map(c => '"' + c.innerText.trim().replace(/"/g, '""') + '"').join(',');
                        csv += line + "\n";
                    }
                });

                const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = 'tumuwuh_laporan_penjualan_' + new Date().toISOString().slice(0,10) + '.csv';
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                URL.revokeObjectURL(url);
                showNotification('📊 File CSV Laporan Penjualan berhasil diunduh!');
            } catch(e) {
                alert('Gagal mendownload CSV: ' + e.message);
            }
        }

        function copyExecutiveSummary() {
            try {
                const text = "=== RINGKASAN EKSEKUTIF TUMUWUH POS ===\n" +
                    "Tanggal: " + new Date().toLocaleDateString('id-ID') + "\n" +
                    "Penjualan Ril: Rp ${String.format(Locale.getDefault(), "%,d", financialSummary.totalRevenue.toLong())}\n" +
                    "HPP / Biaya Bahan: Rp ${String.format(Locale.getDefault(), "%,d", financialSummary.totalHpp.toLong())}\n" +
                    "Laba Kotor: Rp ${String.format(Locale.getDefault(), "%,d", financialSummary.grossProfit.toLong())}\n" +
                    "Beban Operasional: Rp ${String.format(Locale.getDefault(), "%,d", financialSummary.totalOperationalExpenses.toLong())}\n" +
                    "Laba Bersih: Rp ${String.format(Locale.getDefault(), "%,d", financialSummary.netProfit.toLong())}\n" +
                    "Status Cloud: Tersinkronisasi Otomatis";
                navigator.clipboard.writeText(text).then(() => {
                    showNotification('📋 Ringkasan Keuangan berhasil disalin ke Clipboard!');
                }).catch(() => {
                    prompt('Salin teks ringkasan berikut:', text);
                });
            } catch(e) {
                alert('Gagal menyalin ringkasan: ' + e.message);
            }
        }
    </script>

</body>
</html>
        """)

        return htmlBuilder.toString()
    }
}
