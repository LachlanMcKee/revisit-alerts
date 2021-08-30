package com.lachlanmckee.revisit.android

import android.annotation.SuppressLint
import com.lachlanmckee.revisit.Month
import com.lachlanmckee.revisit.RevisitByDate

// Remove suppress to see demonstrate lint rule.
@SuppressLint("RevisitDate")
@RevisitByDate(day = 1, month = Month.JANUARY, year = 1999, reason = "For testing purposes")
class RevisitByDateExample
