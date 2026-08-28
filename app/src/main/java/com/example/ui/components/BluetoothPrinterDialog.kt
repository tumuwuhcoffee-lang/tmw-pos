package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.AmberOrangeLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.CrimsonRedLight
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
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
import com.example.util.BluetoothPrinterManager
import com.example.util.BtDeviceItem
import com.example.util.BtPrinterStatus
import kotlinx.coroutines.launch

@Composable
fun BluetoothPrinterDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val status by BluetoothPrinterManager.connectionStatus.collectAsState()
    val connectedName by BluetoothPrinterManager.connectedDeviceName.collectAsState()
    val connectedAddress by BluetoothPrinterManager.connectedDeviceAddress.collectAsState()
    val pairedDevices by BluetoothPrinterManager.pairedDevices.collectAsState()
    val paperWidth by BluetoothPrinterManager.paperWidth.collectAsState()
    val autoPrint by BluetoothPrinterManager.autoPrintOnCheckout.collectAsState()

    LaunchedEffect(Unit) {
        BluetoothPrinterManager.refreshPairedDevices(context)
    }

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
                    .padding(20.dp)
            ) {
                // 1. Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlueLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (status == BtPrinterStatus.CONNECTED || status == BtPrinterStatus.SIMULATED)
                                    Icons.Default.BluetoothConnected
                                else Icons.Default.Bluetooth,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Printer Thermal Bluetooth",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900
                            )
                            Text(
                                text = "Cetak nota belanja & kasir (ESC/POS)",
                                fontSize = 11.sp,
                                color = Slate600
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = Slate600)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. Active Connection Status Card
                val (statusBg, statusBorder, statusIconColor, statusText, subText) = when (status) {
                    BtPrinterStatus.CONNECTED -> Tuple5(
                        EmeraldGreenLight,
                        EmeraldGreen.copy(alpha = 0.4f),
                        EmeraldGreen,
                        "Printer Terhubung (Hardware BT)",
                        connectedName ?: "Thermal Printer"
                    )
                    BtPrinterStatus.SIMULATED -> Tuple5(
                        EmeraldGreenLight,
                        EmeraldGreen.copy(alpha = 0.4f),
                        EmeraldGreen,
                        "Printer Terhubung (Mode Emulasi/Preview)",
                        connectedName ?: "Virtual Thermal Printer"
                    )
                    BtPrinterStatus.CONNECTING -> Tuple5(
                        AmberOrangeLight,
                        AmberOrange.copy(alpha = 0.4f),
                        AmberOrange,
                        "Menghubungkan ke Printer...",
                        "Harap tunggu sinyal bluetooth..."
                    )
                    BtPrinterStatus.DISCONNECTED -> Tuple5(
                        Slate100,
                        Slate200,
                        Slate600,
                        "Belum Terhubung ke Printer",
                        "Pilih printer bluetooth di bawah ini"
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = statusBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (status == BtPrinterStatus.CONNECTING) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = AmberOrange)
                            } else {
                                Icon(
                                    imageVector = if (status == BtPrinterStatus.CONNECTED || status == BtPrinterStatus.SIMULATED)
                                        Icons.Default.BluetoothConnected
                                    else Icons.Default.BluetoothDisabled,
                                    contentDescription = null,
                                    tint = statusIconColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = statusText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (status == BtPrinterStatus.DISCONNECTED) Slate800 else Slate900
                                )
                                Text(
                                    text = subText,
                                    fontSize = 11.sp,
                                    color = Slate600
                                )
                            }
                        }

                        if (status == BtPrinterStatus.CONNECTED || status == BtPrinterStatus.SIMULATED) {
                            OutlinedButton(
                                onClick = { BluetoothPrinterManager.disconnect() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "Putus", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Printer Settings (Paper Width & Auto Print)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Paper Width Switcher
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Slate50)
                            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Text(text = "Lebar Kertas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Slate200)
                                .padding(2.dp)
                        ) {
                            listOf(58, 80).forEach { width ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(if (paperWidth == width) PrimaryBlue else Color.Transparent)
                                        .clickable { BluetoothPrinterManager.setPaperWidth(width) }
                                        .padding(vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${width}mm",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (paperWidth == width) White else Slate700
                                    )
                                }
                            }
                        }
                    }

                    // Auto Print on Checkout Switch
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Slate50)
                            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Auto Cetak Nota", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate700)
                            Switch(
                                checked = autoPrint,
                                onCheckedChange = { BluetoothPrinterManager.setAutoPrintOnCheckout(it) },
                                modifier = Modifier.size(34.dp, 20.dp),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = White,
                                    checkedTrackColor = PrimaryBlue
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Otomatis cetak setelah bayar", fontSize = 10.sp, color = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Slate200)
                Spacer(modifier = Modifier.height(10.dp))

                // 4. Paired Device List
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Printer Bluetooth (${pairedDevices.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    IconButton(
                        onClick = { BluetoothPrinterManager.refreshPairedDevices(context) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Segarkan", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (pairedDevices.isEmpty()) {
                        item {
                            Text(
                                text = "Tidak ada printer bluetooth terpasang. Pastikan bluetooth aktif.",
                                fontSize = 11.sp,
                                color = Slate500,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    } else {
                        items(pairedDevices) { device ->
                            val isCurrent = (status == BtPrinterStatus.CONNECTED || status == BtPrinterStatus.SIMULATED) &&
                                    (device.name == connectedName || device.address == connectedAddress)

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCurrent) PrimaryBlueLight.copy(alpha = 0.5f) else Slate50,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isCurrent) PrimaryBlue else Slate200
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Print,
                                            contentDescription = null,
                                            tint = if (isCurrent) PrimaryBlue else Slate600,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Column {
                                            Text(
                                                text = device.name,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Slate900
                                            )
                                            Text(
                                                text = device.address,
                                                fontSize = 10.sp,
                                                color = Slate500
                                            )
                                        }
                                    }

                                    if (isCurrent) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(EmeraldGreen)
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(text = "Aktif", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = White)
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    val ok = BluetoothPrinterManager.connectToDevice(context, device)
                                                    if (ok) {
                                                        Toast.makeText(context, "Terhubung ke ${device.name}", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(text = "Hubungkan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. Bottom Actions (Test Print & Tutup)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                BluetoothPrinterManager.printTestReceipt()
                                Toast.makeText(context, "Test Print dikirim ke printer!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Tes Cetak", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Selesai", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)
