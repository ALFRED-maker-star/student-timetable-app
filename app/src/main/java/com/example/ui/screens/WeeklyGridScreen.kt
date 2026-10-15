package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDayOfWeek
import com.example.data.model.Course
import com.example.ui.theme.VioletPrimary
import com.example.ui.theme.getPastelOptionByHex

private const val START_HOUR = 8
private const val END_HOUR = 20
private val HOUR_HEIGHT = 64.dp
private val DAY_COLUMN_WIDTH = 110.dp
private val TIME_LABEL_WIDTH = 48.dp

@Composable
fun WeeklyGridScreen(
    courses: List<Course>,
    todayDayOfWeek: AppDayOfWeek,
    weekStartDay: String,
    onDayClick: (AppDayOfWeek) -> Unit,
    onCourseClick: (Long) -> Unit,
    onAddCourseClick: () -> Unit
) {
    val weekDays = remember(weekStartDay) {
        AppDayOfWeek.getWeekDays(weekStartDay)
    }

    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCourseClick,
                containerColor = VioletPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_add_course")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Course")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top App Bar Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ClassMate",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = VioletPrimary
                    )
                    Text(
                        text = "Weekly Schedule",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Jump to Today Pill Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = VioletPrimary.copy(alpha = 0.12f),
                    modifier = Modifier
                        .clickable { onDayClick(todayDayOfWeek) }
                        .testTag("today_quick_jump")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Today",
                            tint = VioletPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Today (${todayDayOfWeek.shortName})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VioletPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = VioletPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Hint row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tap any day header to view full daily timeline",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Horizontally Scrollable Grid Container
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState)
            ) {
                // Left Sticky-aligned Time Column
                Column(
                    modifier = Modifier
                        .width(TIME_LABEL_WIDTH)
                        .verticalScroll(verticalScrollState)
                ) {
                    // Blank top-left header corner
                    Spacer(modifier = Modifier.height(48.dp))

                    for (hour in START_HOUR until END_HOUR) {
                        Box(
                            modifier = Modifier
                                .height(HOUR_HEIGHT)
                                .fillMaxWidth()
                                .padding(end = 4.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text(
                                text = String.format("%02d:00", hour),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Days & Events Grid
                Column {
                    // Horizontally scrollable day headers
                    Row(
                        modifier = Modifier
                            .height(48.dp)
                            .padding(bottom = 4.dp)
                    ) {
                        weekDays.forEach { day ->
                            val isToday = day == todayDayOfWeek
                            DayHeaderCell(
                                day = day,
                                isToday = isToday,
                                onClick = { onDayClick(day) }
                            )
                        }
                    }

                    // Schedule Grid with Event Blocks
                    Box(
                        modifier = Modifier
                            .verticalScroll(verticalScrollState)
                    ) {
                        // Background grid lines
                        Column {
                            for (hour in START_HOUR until END_HOUR) {
                                Box(
                                    modifier = Modifier
                                        .height(HOUR_HEIGHT)
                                        .width(DAY_COLUMN_WIDTH * weekDays.size)
                                        .border(
                                            width = 0.5.dp,
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                                        )
                                )
                            }
                        }

                        // Day Column Separators
                        Row {
                            weekDays.forEach { _ ->
                                Box(
                                    modifier = Modifier
                                        .width(DAY_COLUMN_WIDTH)
                                        .height(HOUR_HEIGHT * (END_HOUR - START_HOUR))
                                        .border(
                                            width = 0.5.dp,
                                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                        )
                                )
                            }
                        }

                        // Render Course Event Blocks
                        weekDays.forEachIndexed { dayIndex, day ->
                            val dayCourses = courses.filter { course -> course.days.contains(day) }
                            dayCourses.forEach { course ->
                                val startTotalMinutes = course.startMinutesOfDay
                                val endTotalMinutes = course.endMinutesOfDay
                                val startSlotMinutes = START_HOUR * 60
                                val totalOffsetMinutes = startTotalMinutes - startSlotMinutes

                                if (totalOffsetMinutes >= 0 && startTotalMinutes < END_HOUR * 60) {
                                    val topOffsetDp = (totalOffsetMinutes * HOUR_HEIGHT.value / 60).dp
                                    val blockHeightDp =
                                        (course.durationMinutes * HOUR_HEIGHT.value / 60).dp.coerceAtLeast(36.dp)

                                    val pastelOption = getPastelOptionByHex(course.colorHex)
                                    val bgColor = Color(android.graphics.Color.parseColor(pastelOption.bgHex))
                                    val textColor = Color(android.graphics.Color.parseColor(pastelOption.textHex))
                                    val borderColor = Color(android.graphics.Color.parseColor(pastelOption.borderHex))

                                    Box(
                                        modifier = Modifier
                                            .offset(
                                                x = DAY_COLUMN_WIDTH * dayIndex + 2.dp,
                                                y = topOffsetDp + 1.dp
                                            )
                                            .width(DAY_COLUMN_WIDTH - 4.dp)
                                            .height(blockHeightDp - 2.dp)
                                            .clip(RoundedCornerShape(10.dp)) // 10px rounded event blocks per spec
                                            .background(bgColor)
                                            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
                                            .clickable { onCourseClick(course.id) }
                                            .padding(horizontal = 6.dp, vertical = 4.dp)
                                            .testTag("course_block_${course.id}")
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Column {
                                                Text(
                                                    text = course.code,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textColor,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = course.classroom,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = textColor.copy(alpha = 0.85f),
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Text(
                                                text = "${course.formattedStartTime()} - ${course.formattedEndTime()}",
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = textColor.copy(alpha = 0.9f),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayHeaderCell(
    day: AppDayOfWeek,
    isToday: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(DAY_COLUMN_WIDTH)
            .height(44.dp)
            .padding(horizontal = 2.dp)
            .clickable(onClick = onClick)
            .testTag("day_header_${day.shortName}"),
        shape = RoundedCornerShape(10.dp),
        color = if (isToday) VioletPrimary else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (isToday) 2.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.shortName.uppercase(),
                fontSize = 12.sp,
                fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.Bold,
                color = if (isToday) Color.White else MaterialTheme.colorScheme.onSurface
            )
            if (isToday) {
                Text(
                    text = "TODAY",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f)
                )
            } else {
                Text(
                    text = day.fullName,
                    fontSize = 8.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
