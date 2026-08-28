package com.example.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.CoffeeBrown
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.ui.viewmodel.PosViewModel
import com.example.util.WebDashboardHtmlGenerator
import java.io.File
import java.io.FileOutputStream

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebDashboardScreen(
    viewModel: PosViewModel,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val transactions by viewModel.allTransactions.collectAsState()
    val transactionItems by viewModel.allTransactionItems.collectAsState()
    val cashflows by viewModel.allCashflows.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val financialSummary by viewModel.financialStatement.collectAsState()
    val taxSummary by viewModel.taxReportSummary.collectAsState()
    val cloudStatus by viewModel.cloudSyncStatus.collectAsState()

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var isReloading by remember { mutableStateOf(false) }

    val htmlContent = remember(transactions, transactionItems, cashflows, products, financialSummary, taxSummary) {
        WebDashboardHtmlGenerator.generateDashboardHtml(
            transactions = transactions,
            transactionItems = transactionItems,
            cashflows = cashflows,
            products = products,
            financialSummary = financialSummary,
            taxSummary = taxSummary
        )
    }

    Scaffold(
        topBar = {
            Surface(
                color = Slate900,
                shadowElevation = 4.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (onBack != null) {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali",
                                        tint = White
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(GoldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Web",
                                    tint = CoffeeBrown,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Portal Website Dashboard",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = White
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(EmeraldGreen)
                                    )
                                }
                                Text(
                                    text = "Penjualan Ril, Pemasukan, Pengeluaran & Arus Kas Live",
                                    fontSize = 10.sp,
                                    color = Slate400
                                )
                            }
                        }

                        // Action Buttons on Top Bar
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. Refresh Button
                            IconButton(
                                onClick = {
                                    isReloading = true
                                    webViewInstance?.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                                    viewModel.syncOnlineCloudDatabase()
                                    isReloading = false
                                    Toast.makeText(context, "Website Dashboard Diperbarui Real-time", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("refresh_web_dashboard_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Refresh",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // 2. Share / Export HTML
                            IconButton(
                                onClick = {
                                    shareHtmlFile(context, htmlContent)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("share_web_dashboard_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share HTML",
                                    tint = White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // 3. Open in Chrome / External Browser
                            IconButton(
                                onClick = {
                                    openInExternalBrowser(context, htmlContent)
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("open_external_browser_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = "Buka di Browser",
                                    tint = White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Status Bar Subtitle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Slate700)
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌐 URL: ${cloudStatus.webDashboardUrl}",
                            fontSize = 9.sp,
                            color = Slate200,
                            maxLines = 1
                        )
                        Text(
                            text = "STATUS: ONLINE 24/7",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreenLight
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate100)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("web_dashboard_webview"),
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        settings.setSupportZoom(true)
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        webViewInstance = this
                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                    }
                },
                update = { webView ->
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }
            )

            if (isReloading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GoldAccent)
                }
            }
        }
    }
}

private fun shareHtmlFile(context: Context, htmlContent: String) {
    try {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, "tumuwuh_web_dashboard.html")
        FileOutputStream(file).use { out ->
            out.write(htmlContent.toByteArray(Charsets.UTF_8))
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Laporan Website Dashboard Tumuwuh POS (Penjualan Ril, Pemasukan, Pengeluaran & Cashflow):\n\n" +
                        "Akses Real-time: https://tumuwuh-pos.web.app/dashboard/control-center"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Bagikan Portal Website Dashboard")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "Gagal membagikan file dashboard: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun openInExternalBrowser(context: Context, htmlContent: String) {
    try {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, "tumuwuh_dashboard_live.html")
        FileOutputStream(file).use { out ->
            out.write(htmlContent.toByteArray(Charsets.UTF_8))
        }

        val uri = Uri.parse("https://tumuwuh-pos.web.app/dashboard/control-center")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Membuka portal web: https://tumuwuh-pos.web.app", Toast.LENGTH_SHORT).show()
    }
}
