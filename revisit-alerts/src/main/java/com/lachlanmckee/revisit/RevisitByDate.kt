package com.lachlanmckee.revisit

/**
 * Defines a date in future that will trigger an alert when exceeded.
 */
annotation class RevisitByDate(
  val day: Int,
  val month: Int,
  val year: Int,
  val reason: String
)
