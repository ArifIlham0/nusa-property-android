package com.nusaproperty.app.ui.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    fun formatRupiah(amount: Long): String {
        val format = NumberFormat.getNumberInstance(Locale("in", "ID"))
        return "Rp ${format.format(amount)}"
    }
}
