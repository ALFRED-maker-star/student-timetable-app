package com.example.ui.screens

import android.app.TimePickerDialog
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.AppDayOfWeek
import com.example.data.model.Course
import com.example.ui.theme.PASTEL_PALETTE
import com.example.ui.theme.PastelColorOption
import com.example.ui.theme.VioletContainer
import com.example.ui.theme.VioletOnContainer
import com.example.ui.theme.VioletPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCourseScreen(
    courseToEdit: Course?,
    existingLecturers: List<String>,
    onSaveCourse: (Course) -> Unit,
    onDeleteCourse: ((Long) -> Unit)?,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(courseToEdit?.name ?: "") }
    var code by remember { mutableStateOf(courseToEdit?.code ?: "") }
    var lecturer by remember { mutableStateOf(courseToEdit?.lecturer ?: "") }
    var lecturerEmail by remember { mutableStateOf(courseToEdit?.lecturerEmail ?: "") }
    var lecturerOffice by remember { mutableStateOf(courseToEdit?.lecturerOffice ?: "") }
    var classroom by remember { mutableStateOf(courseToEdit?.classroom ?: "") }
    var selectedDays by remember {
        mutableStateOf(courseToEdit?.days?.toSet() ?: setOf(AppDayOfWeek.MON, AppDayOfWeek.WED))
    }
    var startHour by remember { mutableIntStateOf(courseToEdit?.startHour ?: 9) }
    var startMinute by remember { mutableIntStateOf(courseToEdit?.startMinute ?: 0) }
    var endHour by remember { mutableIntStateOf(courseToEdit?.endHour ?: 10) }
    var endMinute by remember { mutableIntStateOf(courseToEdit?.endMinute ?: 30) }
    var selectedColorHex by remember {
        mutableStateOf(courseToEdit?.colorHex ?: PASTEL_PALETTE.first().bgHex)
    }
    var recurrence by remember { mutableStateOf(courseToEdit?.recurrence ?: "Weekly") }
    var notes by remember { mutableStateOf(courseToEdit?.notes ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val startPicker = remember {
        TimePickerDialog(
            context,
            { _, hour, min ->
                startHour = hour
                startMinute = min
            },
            startHour,
            startMinute,
            true
        )
    }

    val endPicker = remember {
        TimePickerDialog(
            context,
            { _, hour, min ->
                endHour = hour
                endMinute = min
            },
            endHour,
            endMinute,
            true
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (courseToEdit == null) "Add New Course" else "Edit Course",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (courseToEdit != null && onDeleteCourse != null) {
                        IconButton(
                            onClick = { onDeleteCourse(courseToEdit.id) },
                            modifier = Modifier.testTag("delete_course_button")
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (errorMessage != null) {
                Surface(
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFDC2626),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Course Details Section
            Text(
                text = "Course Details",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VioletPrimary
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = { Text("Course Name *") },
                placeholder = { Text("e.g. Data Structures & Algorithms") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_course_name")
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it; errorMessage = null },
                    label = { Text("Course Code *") },
                    placeholder = { Text("e.g. CS-201") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_course_code")
                )

                OutlinedTextField(
                    value = classroom,
                    onValueChange = { classroom = it; errorMessage = null },
                    label = { Text("Classroom *") },
                    placeholder = { Text("e.g. Turing 302") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_course_room")
                )
            }

            // Lecturer Section
            Text(
                text = "Lecturer Information",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VioletPrimary
            )

            OutlinedTextField(
                value = lecturer,
                onValueChange = { lecturer = it },
                label = { Text("Lecturer Name *") },
                placeholder = { Text("e.g. Prof. Alan Turing") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_course_lecturer")
            )

            if (existingLecturers.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    existingLecturers.take(4).forEach { existing ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { lecturer = existing }
                        ) {
                            Text(
                                text = existing,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = lecturerEmail,
                    onValueChange = { lecturerEmail = it },
                    label = { Text("Lecturer Email") },
                    placeholder = { Text("email@university.edu") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = lecturerOffice,
                    onValueChange = { lecturerOffice = it },
                    label = { Text("Office") },
                    placeholder = { Text("Hall 412") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            // Day(s) of Week Multi-select
            Text(
                text = "Day(s) of Week *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VioletPrimary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppDayOfWeek.entries.forEach { day ->
                    val isChecked = selectedDays.contains(day)
                    FilterChip(
                        selected = isChecked,
                        onClick = {
                            selectedDays = if (isChecked) {
                                if (selectedDays.size > 1) selectedDays - day else selectedDays
                            } else {
                                selectedDays + day
                            }
                        },
                        label = { Text(day.shortName, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VioletPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("day_chip_${day.shortName}")
                    )
                }
            }

            // Time Pickers
            Text(
                text = "Class Time *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VioletPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start Time Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { startPicker.show() }
                        .testTag("picker_start_time")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Start Time", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = VioletPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                Course.formatTime(startHour, startMinute),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // End Time Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { endPicker.show() }
                        .testTag("picker_end_time")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("End Time", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = VioletPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                Course.formatTime(endHour, endMinute),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Distinct Pastel Color Picker
            Text(
                text = "Course Pastel Color *",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VioletPrimary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PASTEL_PALETTE.forEach { option ->
                    val isSelected = selectedColorHex.equals(option.bgHex, ignoreCase = true)
                    val optBg = Color(android.graphics.Color.parseColor(option.bgHex))
                    val optBorder = Color(android.graphics.Color.parseColor(option.borderHex))

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(optBg)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) VioletPrimary else optBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedColorHex = option.bgHex }
                            .testTag("color_picker_${option.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = VioletPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Recurrence Toggle
            Text(
                text = "Recurrence",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = VioletPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Weekly", "Bi-weekly", "One-time").forEach { rec ->
                    val isSelected = recurrence == rec
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) VioletPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { recurrence = rec }
                            .testTag("recurrence_$rec")
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = rec,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // Notes / Homework
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Course Notes / Syllabus info (Optional)") },
                placeholder = { Text("e.g. Weekly problem sets, office hours Mon 2-4 PM") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Save & Cancel Buttons
            Button(
                onClick = {
                    if (name.isBlank() || code.isBlank() || classroom.isBlank() || lecturer.isBlank()) {
                        errorMessage = "Please fill in course name, code, classroom, and lecturer."
                        return@Button
                    }
                    val startMin = startHour * 60 + startMinute
                    val endMin = endHour * 60 + endMinute
                    if (startMin >= endMin) {
                        errorMessage = "End time must be after start time."
                        return@Button
                    }
                    if (selectedDays.isEmpty()) {
                        errorMessage = "Select at least one day for the course."
                        return@Button
                    }

                    val updatedCourse = Course(
                        id = courseToEdit?.id ?: 0L,
                        name = name.trim(),
                        code = code.trim().uppercase(),
                        lecturer = lecturer.trim(),
                        lecturerEmail = lecturerEmail.trim(),
                        lecturerOffice = lecturerOffice.trim(),
                        classroom = classroom.trim(),
                        days = selectedDays.sortedBy { it.dayNumber },
                        startHour = startHour,
                        startMinute = startMinute,
                        endHour = endHour,
                        endMinute = endMinute,
                        colorHex = selectedColorHex,
                        recurrence = recurrence,
                        notes = notes.trim()
                    )
                    onSaveCourse(updatedCourse)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_save_course"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary)
            ) {
                Text(
                    text = if (courseToEdit == null) "Add to Schedule" else "Save Changes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
