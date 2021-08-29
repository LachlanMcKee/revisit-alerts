package com.lachlanmckee.revisit

/**
 * Defines a date (usually the date the annotation is added) that will trigger an alert when the
 * date plus the number of days in future is exceeded.
 */
annotation class RevisitFromDate(
  val day: Int,
  val month: Int,
  val year: Int,
  val reason: String,
  val daysInFuture: Int = 30
)
