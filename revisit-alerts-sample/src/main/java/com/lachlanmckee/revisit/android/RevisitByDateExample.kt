package com.lachlanmckee.revisit.android

import android.annotation.SuppressLint
import com.lachlanmckee.revisit.Revisit

// Remove suppress to see demonstrate lint rule.
@SuppressLint("RevisitDate")
@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, year = 1999, reason = "For testing purposes")
class RevisitByDateExample
