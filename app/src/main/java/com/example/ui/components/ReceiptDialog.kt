package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionItemEntity
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.util.BluetoothPrinterManager
import com.example.util.BtPrinterStatus
import com.example.util.FormatUtils
import kotlinx.coroutines.launch

@Composable
fun ReceiptDialog(
    transaction: TransactionEntity,
    items: List<TransactionItemEntity>,
    onDismiss: () -> Unit,
    onNewTransaction: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val btStatus by BluetoothPrinterManager.connectionStatus.collectAsState()
    val connectedPrinterName by BluetoothPrinterManager.connectedDeviceName.collectAsState()
    var showBtSettings by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Close & BT Status
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
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Transaksi Berhasil",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = transaction.invoiceNumber,
                                fontSize = 12.sp,
                                color = Slate600
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bluetooth Printer Mini-Banner
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showBtSettings = true },
                    color = if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED) EmeraldGreenLight else Slate100,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED) EmeraldGreen.copy(alpha = 0.3f) else Slate200)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED) Icons.Default.BluetoothConnected else Icons.Default.Bluetooth,
                                contentDescription = null,
                                tint = if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED) EmeraldGreen else Slate600,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED)
                                    "Printer: ${connectedPrinterName ?: "Thermal BT"}"
                                else "Printer BT Belum Terhubung (Klik Hubungkan)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED) Slate900 else Slate600
                            )
                        }

                        Text(
                            text = if (btStatus == BtPrinterStatus.CONNECTED || btStatus == BtPrinterStatus.SIMULATED) "Siap Cetak" else "Atur",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Receipt Paper Container
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate200, RoundedCornerShape(12.dp)),
                    color = Slate100.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TUMUWUH CAFÉ COFFEE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            color = Slate900,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Tumuwuh POS • Bar • Billiard • GOR",
                            fontSize = 11.sp,
                            color = Slate600,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = FormatUtils.formatDateTime(transaction.timestamp),
                            fontSize = 10.sp,
                            color = Slate400,
                            fontFamily = FontFamily.Monospace
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = Slate200, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Customer info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Pelanggan: ${transaction.customerName ?: "Umum"}",
                                fontSize = 11.sp,
                                color = Slate700
                            )
                            Text(
                                text = transaction.tableOrOrderRef ?: "-",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate800
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Slate200, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Items list
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(items) { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.productName,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Slate900
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "${item.quantity}x @${FormatUtils.formatRupiah(item.unitPrice)}",
                                                fontSize = 10.sp,
                                                color = Slate600
                                            )
                                            Text(
                                                text = "(${item.category})",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PrimaryBlue
                                            )
                                        }
                                    }
                                    Text(
                                        text = FormatUtils.formatRupiah(item.totalPrice),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate900
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Slate200, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Subtotals & Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal", fontSize = 12.sp, color = Slate600)
                            Text(text = FormatUtils.formatRupiah(transaction.subtotal), fontSize = 12.sp, color = Slate800)
                        }

                        if (transaction.discountAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Diskon", fontSize = 12.sp, color = EmeraldGreen)
                                Text(text = "-${FormatUtils.formatRupiah(transaction.discountAmount)}", fontSize = 12.sp, color = EmeraldGreen)
                            }
                        }

                        if (transaction.taxAmount > 0) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Pajak", fontSize = 12.sp, color = Slate600)
                                Text(text = FormatUtils.formatRupiah(transaction.taxAmount), fontSize = 12.sp, color = Slate800)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "TOTAL", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(text = FormatUtils.formatRupiah(transaction.totalAmount), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        HorizontalDivider(color = Slate200, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(6.dp))

                        // Payment info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Metode: ${transaction.paymentMethod}", fontSize = 11.sp, color = Slate700)
                            Text(
                                text = "Diberikan: ${FormatUtils.formatRupiah(transaction.cashTendered)}",
                                fontSize = 11.sp,
                                color = Slate700
                            )
                        }
                        if (transaction.paymentMethod == "CASH") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Kembalian", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text(
                                    text = FormatUtils.formatRupiah(transaction.changeAmount),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Terima kasih atas kunjungan Anda!",
                            fontSize = 11.sp,
                            color = Slate500,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                BluetoothPrinterManager.printReceipt(transaction, items)
                                Toast.makeText(context, "Mencetak resi ke printer bluetooth...", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Cetak Resi (BT)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onNewTransaction()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1.2f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Transaksi Baru", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    if (showBtSettings) {
        BluetoothPrinterDialog(onDismiss = { showBtSettings = false })
    }
}
