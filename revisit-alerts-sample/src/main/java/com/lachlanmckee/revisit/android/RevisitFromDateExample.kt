package com.lachlanmckee.revisit.android

import android.annotation.SuppressLint
import com.lachlanmckee.revisit.RevisitFromDate

// Remove suppress to see demonstrate lint rule.
@SuppressLint("RevisitDate")
@RevisitFromDate(year = 1999, month = 1, day = 1, reason = "For testing purposes", daysInFuture = 10)
class RevisitFromDateExample
