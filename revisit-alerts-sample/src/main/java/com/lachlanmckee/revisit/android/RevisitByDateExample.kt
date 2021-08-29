package com.lachlanmckee.revisit.android

import android.annotation.SuppressLint
import com.lachlanmckee.revisit.RevisitByDate

// Remove suppress to see demonstrate lint rule.
@SuppressLint("RevisitDate")
@RevisitByDate(year = 1999, month = 1, day = 1, reason = "For testing purposes")
class RevisitByDateExample
