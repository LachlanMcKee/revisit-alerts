package com.lachlanmckee.revisit.android

import android.annotation.SuppressLint
import com.lachlanmckee.revisit.Month
import com.lachlanmckee.revisit.RevisitFromDate

// Remove suppress to see demonstrate lint rule.
@SuppressLint("RevisitDate")
@RevisitFromDate(
  day = 1,
  month = Month.JANUARY,
  year = 1999,
  reason = "For testing purposes",
  delay = RevisitFromDate.Delay.ONE_MONTH
)
class RevisitFromDateExample
