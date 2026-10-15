package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Lecturer
import com.example.ui.theme.VioletContainer
import com.example.ui.theme.VioletOnContainer
import com.example.ui.theme.VioletPrimary
import com.example.ui.theme.getPastelOptionByHex

@Composable
fun LecturerListScreen(
    lecturers: List<Lecturer>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCourseClick: (Long) -> Unit
) {
    val context = LocalContext.current

    val filteredLecturers = remember(lecturers, searchQuery) {
        if (searchQuery.isBlank()) {
            lecturers
        } else {
            val q = searchQuery.trim().lowercase()
            lecturers.filter { lecturer ->
                lecturer.name.lowercase().contains(q) ||
                        lecturer.email.lowercase().contains(q) ||
                        lecturer.office.lowercase().contains(q) ||
                        lecturer.courses.any { it.code.lowercase().contains(q) || it.name.lowercase().contains(q) }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header
            Text(
                text = "Lecturers",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = VioletPrimary
            )
            Text(
                text = "Faculty directory and courses taught",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search by lecturer name, office, or course...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = VioletPrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_lecturers_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredLecturers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            color = VioletContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = VioletPrimary, modifier = Modifier.size(32.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No faculty found",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Try searching for a different name or course code",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("lecturers_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredLecturers) { lecturer ->
                        LecturerCard(
                            lecturer = lecturer,
                            onCourseClick = onCourseClick,
                            onEmailClick = { email ->
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$email")
                                    putExtra(Intent.EXTRA_SUBJECT, "Course Inquiry - Student")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LecturerCard(
    lecturer: Lecturer,
    onCourseClick: (Long) -> Unit,
    onEmailClick: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("lecturer_card_${lecturer.name.replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        modifier = Modifier.size(46.dp),
                        shape = CircleShape,
                        color = VioletContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = lecturer.initials,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = VioletOnContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = lecturer.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (lecturer.office.isNotBlank()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.MeetingRoom,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = lecturer.office,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (lecturer.email.isNotBlank()) {
                    IconButton(
                        onClick = { onEmailClick(lecturer.email) },
                        modifier = Modifier.testTag("email_lecturer_${lecturer.name.replace(" ", "_")}")
                    ) {
                        Icon(Icons.Default.Email, contentDescription = "Email", tint = VioletPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Courses Taught Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Courses Taught (${lecturer.courses.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletPrimary
                )
                Text(
                    text = "Tap to view schedule",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List of course chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                lecturer.courses.forEach { course ->
                    val pastel = getPastelOptionByHex(course.colorHex)
                    val bgColor = Color(android.graphics.Color.parseColor(pastel.bgHex))
                    val textColor = Color(android.graphics.Color.parseColor(pastel.textHex))
                    val borderColor = Color(android.graphics.Color.parseColor(pastel.borderHex))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = bgColor,
                        modifier = Modifier
                            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                            .clickable { onCourseClick(course.id) }
                            .testTag("lecturer_course_chip_${course.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = course.code,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "•  ${course.classroom}",
                                fontSize = 10.sp,
                                color = textColor.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        }
    }
}
