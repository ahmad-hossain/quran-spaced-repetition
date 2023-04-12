package com.github.ahmad_hossain.quranspacedrepetition.feature_pages.util

import comgithubahmadhossainquranspacedrepetition.Page
import java.time.LocalDate

object PageUtil {
    const val DATABASE_NAME = "page_db.db"

    fun defaultPage(
        pageNumber: Int,
        interval: Int = 0,
        repetitions: Int = 0,
        eFactor: Double = 2.5,
        dueDate: Long? = null
    ) = Page(
        pageNumber,
        interval,
        repetitions,
        eFactor,
        dueDate
    )

    val Page.localDueDate: LocalDate?
        get() = if (dueDate == null) null else LocalDate.ofEpochDay(dueDate)

    val Page.relativeDueDate: String?
        get() = localDueDate?.let {
            val periodTillDueDate = LocalDate.now().until(it)
            var s = ""
            if (periodTillDueDate.years != 0)
                s += "${periodTillDueDate.years}Y "
            if (periodTillDueDate.months != 0)
                s += "${periodTillDueDate.months}M "
            if (periodTillDueDate.days != 0 || s.isEmpty())
                s += "${periodTillDueDate.days}D"
            return s.trim()
        }
}