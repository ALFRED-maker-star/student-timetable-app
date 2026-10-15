package com.example.data.local

import com.example.data.model.AppDayOfWeek
import com.example.data.model.Course

object SampleData {
    val sampleCourses = listOf(
        Course(
            id = 1,
            name = "Data Structures & Algorithms",
            code = "CS-201",
            lecturer = "Prof. Alan Turing",
            lecturerEmail = "a.turing@university.edu",
            lecturerOffice = "Turing Hall, Office 412",
            classroom = "Turing Hall 302",
            days = listOf(AppDayOfWeek.MON, AppDayOfWeek.WED),
            startHour = 9,
            startMinute = 0,
            endHour = 10,
            endMinute = 30,
            colorHex = "#E9D5FF", // Lavender
            recurrence = "Weekly",
            notes = "Bring laptop with CLion/VS Code installed. Weekly problem sets."
        ),
        Course(
            id = 2,
            name = "General Physics Lab",
            code = "PHYS-102",
            lecturer = "Dr. Richard Feynman",
            lecturerEmail = "r.feynman@university.edu",
            lecturerOffice = "Science Complex B-22",
            classroom = "Curie Science Lab 204",
            days = listOf(AppDayOfWeek.TUE, AppDayOfWeek.THU),
            startHour = 10,
            startMinute = 0,
            endHour = 11,
            endMinute = 45,
            colorHex = "#D1FAE5", // Mint Green
            recurrence = "Weekly",
            notes = "Safety goggles mandatory. Lab reports due before midnight."
        ),
        Course(
            id = 3,
            name = "Multivariable Calculus",
            code = "MATH-150",
            lecturer = "Dr. Katherine Johnson",
            lecturerEmail = "k.johnson@university.edu",
            lecturerOffice = "Euler Math Wing 308",
            classroom = "Euler Hall 105",
            days = listOf(AppDayOfWeek.MON, AppDayOfWeek.WED, AppDayOfWeek.FRI),
            startHour = 11,
            startMinute = 0,
            endHour = 12,
            endMinute = 15,
            colorHex = "#BAE6FD", // Sky Blue
            recurrence = "Weekly",
            notes = "Weekly quiz every Friday. Office hours Mon & Wed 2-4 PM."
        ),
        Course(
            id = 4,
            name = "Technical Communication",
            code = "ENG-210",
            lecturer = "Prof. Grace Hopper",
            lecturerEmail = "g.hopper@university.edu",
            lecturerOffice = "Humanities Quad H-14",
            classroom = "Arts Complex A-12",
            days = listOf(AppDayOfWeek.TUE, AppDayOfWeek.THU),
            startHour = 13,
            startMinute = 30,
            endHour = 15,
            endMinute = 0,
            colorHex = "#FED7AA", // Peach Coral
            recurrence = "Weekly",
            notes = "Semester group presentation project and engineering whitepapers."
        ),
        Course(
            id = 5,
            name = "Human-Computer Interaction",
            code = "ART-105",
            lecturer = "Dr. Don Norman",
            lecturerEmail = "d.norman@university.edu",
            lecturerOffice = "Media Wing 502",
            classroom = "Design Studio 4",
            days = listOf(AppDayOfWeek.WED, AppDayOfWeek.FRI),
            startHour = 14,
            startMinute = 0,
            endHour = 15,
            endMinute = 30,
            colorHex = "#FFE4E6", // Rose Pink
            recurrence = "Weekly",
            notes = "User study interviews and Figma prototypes required for final design portfolio."
        ),
        Course(
            id = 6,
            name = "Applied Statistics & Inference",
            code = "STAT-220",
            lecturer = "Dr. Ronald Fisher",
            lecturerEmail = "r.fisher@university.edu",
            lecturerOffice = "Turing Hall 215",
            classroom = "Science Auditorium B",
            days = listOf(AppDayOfWeek.MON, AppDayOfWeek.THU),
            startHour = 15,
            startMinute = 30,
            endHour = 17,
            endMinute = 0,
            colorHex = "#FEF08A", // Warm Lemon
            recurrence = "Weekly",
            notes = "R-Studio datasets and hypothesis tests."
        )
    )
}
