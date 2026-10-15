package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppDayOfWeek
import com.example.data.model.Course
import com.example.ui.theme.VioletContainer
import com.example.ui.theme.VioletOnContainer
import com.example.ui.theme.VioletPrimary
import com.example.ui.theme.getPastelOptionByHex
import java.util.Calendar

@Composable
fun DailyViewScreen(
    selectedDay: AppDayOfWeek,
    todayDayOfWeek: AppDayOfWeek,
    weekStartDay: String,
    courses: List<Course>,
    onDaySelected: (AppDayOfWeek) -> Unit,
    onCourseClick: (Long) -> Unit,
    onGetDirectionsClick: (Course) -> Unit,
    onNotifyMeClick: (Course) -> Unit,
    onAddCourseClick: () -> Unit
) {
    val weekDays = remember(weekStartDay) {
        AppDayOfWeek.getWeekDays(weekStartDay)
    }

    // Filter and sort courses for this selected day
    val dayCourses = remember(courses, selectedDay) {
        courses.filter { it.days.contains(selectedDay) }
            .sortedBy { it.startMinutesOfDay }
    }

    val isToday = selectedDay == todayDayOfWeek

    // Current time calculation
    val now = Calendar.getInstance()
    val currentHour = now.get(Calendar.HOUR_OF_DAY)
    val currentMinute = now.get(Calendar.MINUTE)
    val currentTotalMinutes = currentHour * 60 + currentMinute
    val currentTimeString = String.format("%02d:%02d", currentHour, currentMinute)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCourseClick,
                containerColor = VioletPrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("fab_daily_add")
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = selectedDay.fullName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = if (isToday) "Today • ${dayCourses.size} classes" else "${dayCourses.size} classes scheduled",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isToday) VioletPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                    )
                }

                if (isToday) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = VioletContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(VioletPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Now: $currentTimeString",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = VioletOnContainer
                            )
                        }
                    }
                }
            }

            // Horizontally Scrollable Day Switcher Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                weekDays.forEach { day ->
                    val isSelected = day == selectedDay
                    val isCurrentDay = day == todayDayOfWeek

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when {
                            isSelected -> VioletPrimary
                            isCurrentDay -> VioletContainer
                            else -> MaterialTheme.colorScheme.surface
                        },
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .clickable { onDaySelected(day) }
                            .testTag("daily_tab_${day.shortName}")
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = day.shortName.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isSelected -> Color.White
                                    isCurrentDay -> VioletOnContainer
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> Color.White
                                            isCurrentDay -> VioletPrimary
                                            else -> Color.Transparent
                                        }
                                    )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timeline List
            if (dayCourses.isEmpty()) {
                // Empty state for day
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = VioletContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = VioletPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Classes on ${selectedDay.fullName}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Enjoy your free time, review study notes, or add an extra lab session.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onAddCourseClick,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                            modifier = Modifier.testTag("daily_empty_add_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Course for ${selectedDay.shortName}")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("daily_timeline_list"),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Current time indicator line if today and before first class
                        if (isToday && currentTotalMinutes < dayCourses.first().startMinutesOfDay) {
                            CurrentTimeIndicator(timeString = currentTimeString)
                        }
                    }

                    items(dayCourses) { course ->
                        val isClassUpcoming = isToday &&
                                currentTotalMinutes in (course.startMinutesOfDay - 30)..course.startMinutesOfDay
                        val isClassNow = isToday &&
                                currentTotalMinutes in course.startMinutesOfDay..course.endMinutesOfDay

                        Column {
                            // Check if current time falls right before or during this course
                            if (isToday && currentTotalMinutes in (course.startMinutesOfDay - 15) until course.startMinutesOfDay) {
                                CurrentTimeIndicator(timeString = currentTimeString)
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            DailyCourseCard(
                                course = course,
                                isClassNow = isClassNow,
                                isClassUpcoming = isClassUpcoming,
                                onCardClick = { onCourseClick(course.id) },
                                onDirectionsClick = { onGetDirectionsClick(course) },
                                onNotifyClick = { onNotifyMeClick(course) }
                            )

                            if (isToday && isClassNow) {
                                Spacer(modifier = Modifier.height(8.dp))
                                CurrentTimeIndicator(timeString = "$currentTimeString (In Progress)")
                            }
                        }
                    }

                    item {
                        // If today and after all classes
                        if (isToday && currentTotalMinutes > dayCourses.last().endMinutesOfDay) {
                            CurrentTimeIndicator(timeString = "$currentTimeString (Classes Finished)")
                        }
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentTimeIndicator(timeString: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFEF4444)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = timeString,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            color = Color(0xFFEF4444),
            thickness = 2.dp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DailyCourseCard(
    course: Course,
    isClassNow: Boolean,
    isClassUpcoming: Boolean,
    onCardClick: () -> Unit,
    onDirectionsClick: () -> Unit,
    onNotifyClick: () -> Unit
) {
    val pastel = getPastelOptionByHex(course.colorHex)
    val bgColor = Color(android.graphics.Color.parseColor(pastel.bgHex))
    val textColor = Color(android.graphics.Color.parseColor(pastel.textHex))
    val borderColor = Color(android.graphics.Color.parseColor(pastel.borderHex))

    Card(
        shape = RoundedCornerShape(10.dp), // Rounded 10px event blocks per spec
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isClassNow) 2.dp else 1.dp,
                color = if (isClassNow) VioletPrimary else borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onCardClick)
            .testTag("daily_course_card_${course.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Top Row: Code, Badge & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = course.code,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.7f)
                    ) {
                        Text(
                            text = course.recurrence,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Time Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.65f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = textColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = course.formattedTimeRange(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Course Name
            Text(
                text = course.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Info Rows: Lecturer & Room
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    // Lecturer
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Lecturer",
                            modifier = Modifier.size(15.dp),
                            tint = textColor.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = course.lecturer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor.copy(alpha = 0.9f)
                        )
                    }

                    // Room
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Room",
                            modifier = Modifier.size(15.dp),
                            tint = textColor.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = course.classroom,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    }
                }

                // Action Buttons: Get Directions & Notify Me
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Get Directions Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .clickable(onClick = onDirectionsClick)
                            .testTag("action_directions_${course.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DirectionsWalk,
                                contentDescription = "Get Directions",
                                tint = VioletPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Directions",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = VioletPrimary
                            )
                        }
                    }

                    // Notify Me Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = VioletPrimary,
                        modifier = Modifier
                            .clickable(onClick = onNotifyClick)
                            .testTag("action_notify_${course.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Notify me",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Notify",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
