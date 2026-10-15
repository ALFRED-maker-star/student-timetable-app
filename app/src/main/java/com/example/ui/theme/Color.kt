package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Brand Colors
val VioletPrimary = Color(0xFF7C3AED)
val VioletPrimaryDark = Color(0xFFA78BFA)
val VioletContainer = Color(0xFFEDE9FE)
val VioletOnContainer = Color(0xFF4C1D95)

// Neutral Backgrounds & Surfaces
val BackgroundLight = Color(0xFFF8FAFC)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariantLight = Color(0xFFF1F5F9)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF64748B)
val BorderLight = Color(0xFFE2E8F0)

val BackgroundDark = Color(0xFF0F172A)
val SurfaceDark = Color(0xFF1E293B)
val SurfaceVariantDark = Color(0xFF334155)
val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)
val BorderDark = Color(0xFF334155)

// Pastel Course Palette with matching deep contrasting text colors
data class PastelColorOption(
    val id: String,
    val name: String,
    val bgHex: String,
    val textHex: String,
    val borderHex: String
)

val PASTEL_PALETTE = listOf(
    PastelColorOption("purple", "Lavender", "#E9D5FF", "#581C87", "#D8B4FE"),
    PastelColorOption("emerald", "Mint Green", "#D1FAE5", "#065F46", "#A7F3D0"),
    PastelColorOption("peach", "Peach Coral", "#FED7AA", "#9A3412", "#FDBA74"),
    PastelColorOption("sky", "Sky Blue", "#BAE6FD", "#0369A1", "#7DD3FC"),
    PastelColorOption("rose", "Rose Pink", "#FFE4E6", "#9F1239", "#FECDD3"),
    PastelColorOption("lemon", "Warm Lemon", "#FEF08A", "#854D0E", "#FDE047"),
    PastelColorOption("indigo", "Soft Indigo", "#C7D2FE", "#3730A3", "#A5B4FC"),
    PastelColorOption("teal", "Aqua Teal", "#CCFBF1", "#115E59", "#99F6E4")
)

fun getPastelOptionByHex(hex: String): PastelColorOption {
    return PASTEL_PALETTE.firstOrNull { it.bgHex.equals(hex, ignoreCase = true) }
        ?: PASTEL_PALETTE.first()
}
