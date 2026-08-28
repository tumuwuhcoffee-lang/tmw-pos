package com.example.ui.components

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.White
import com.example.util.FormatUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun InteractiveCalendarView(
    startDate: Long,
    endDate: Long,
    selectedMode: String,
    onQuickFilterSelected: (String) -> Unit,
    onDateClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var displayCalendar by remember {
        mutableStateOf(Calendar.getInstance().apply { timeInMillis = startDate })
    }

    val currentMonthYear = remember(displayCalendar.timeInMillis) {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
        sdf.format(displayCalendar.time)
    }

    WhiteCard(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Quick Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QuickChip("Hari Ini", selected = selectedMode == "TODAY") {
                    onQuickFilterSelected("TODAY")
                }
                QuickChip("Kemarin", selected = selectedMode == "YESTERDAY") {
                    onQuickFilterSelected("YESTERDAY")
                }
                QuickChip("7 Hari", selected = selectedMode == "LAST_7_DAYS") {
                    onQuickFilterSelected("LAST_7_DAYS")
                }
                QuickChip("Bulan Ini", selected = selectedMode == "THIS_MONTH") {
                    onQuickFilterSelected("THIS_MONTH")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Calendar Navigation Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = currentMonthYear,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Slate900
                    )
                }

                Row {
                    IconButton(
                        onClick = {
                            displayCalendar = (displayCalendar.clone() as Calendar).apply {
                                add(Calendar.MONTH, -1)
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Bulan Sebelumnya",
                            tint = Slate600,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            displayCalendar = (displayCalendar.clone() as Calendar).apply {
                                add(Calendar.MONTH, 1)
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Bulan Selanjutnya",
                            tint = Slate600,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Days of week row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Slate500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Calendar Days Grid
            val daysInMonth = remember(displayCalendar.timeInMillis) {
                val cal = (displayCalendar.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0-indexed for Sunday
                val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

                val list = mutableListOf<CalendarDayItem?>()
                for (i in 0 until firstDayOfWeek) {
                    list.add(null)
                }
                for (d in 1..maxDays) {
                    cal.set(Calendar.DAY_OF_MONTH, d)
                    list.add(CalendarDayItem(dayNumber = d, timeMillis = cal.timeInMillis))
                }
                list
            }

            // Render 7-column grid manually or with rows
            val rows = daysInMonth.chunked(7)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rows.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (i in 0..6) {
                            val item = week.getOrNull(i)
                            if (item != null) {
                                val isStart = isSameDay(item.timeMillis, startDate)
                                val isEnd = isSameDay(item.timeMillis, endDate)
                                val isInRange = item.timeMillis in startDate..endDate

                                val isSingleSelection = isSameDay(startDate, endDate)
                                val isSelected = if (isSingleSelection) isStart else isInRange

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(
                                            if (isStart || isEnd) CircleShape
                                            else if (isInRange) RoundedCornerShape(4.dp)
                                            else RoundedCornerShape(8.dp)
                                        )
                                        .background(
                                            when {
                                                isStart || isEnd -> PrimaryBlue
                                                isInRange -> PrimaryBlueLight
                                                else -> Color.Transparent
                                            }
                                        )
                                        .clickable {
                                            onDateClicked(item.timeMillis)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${item.dayNumber}",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isStart || isEnd -> White
                                            isInRange -> PrimaryBlue
                                            else -> Slate800
                                        }
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Active Date Range Label Banner
            val isSingleDay = isSameDay(startDate, endDate)
            val dateLabel = if (isSingleDay) {
                "🗓️ Tanggal: ${FormatUtils.formatDate(startDate)}"
            } else {
                "🗓️ Rentang: ${FormatUtils.formatShortDate(startDate)} - ${FormatUtils.formatShortDate(endDate)}"
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = PrimaryBlueLight.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryBlue
                    )
                    Text(
                        text = if (isSingleDay) "1 Hari" else "Rentang Tanggal",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate700
                    )
                }
            }
        }
    }
}

private data class CalendarDayItem(
    val dayNumber: Int,
    val timeMillis: Long
)

@Composable
private fun QuickChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) PrimaryBlue else Slate100)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) White else Slate700
        )
    }
}

private fun isSameDay(time1: Long, time2: Long): Boolean {
    val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
    val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
