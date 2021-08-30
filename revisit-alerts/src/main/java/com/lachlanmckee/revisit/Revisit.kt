package com.lachlanmckee.revisit

object Revisit {

  /**
   * Defines a date in future that will trigger an alert when exceeded.
   */
  annotation class ByDate(
    val day: Int,
    val month: Month,
    val year: Int,
    val reason: String
  )

  /**
   * Defines a date (usually the date the annotation is added) that will trigger an alert when the
   * date plus the delay is exceeded.
   */
  annotation class FromDate(
    val day: Int,
    val month: Month,
    val year: Int,
    val reason: String,
    val delay: Delay
  )

  enum class Month {
    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
  }

  enum class Delay(val days: Int = 0, val months: Int = 0, val years: Int = 0) {
    ONE_WEEK(days = 7),
    TWO_WEEKS(days = 14),
    ONE_MONTH(months = 1),
    TWO_MONTHS(months = 2),
    THREE_MONTHS(months = 3),
    FOUR_MONTHS(months = 4),
    FIVE_MONTHS(months = 5),
    SIX_MONTHS(months = 6),
    SEVEN_MONTHS(months = 7),
    EIGHT_MONTHS(months = 8),
    NINE_MONTHS(months = 9),
    TEN_MONTHS(months = 10),
    ELEVEN_MONTHS(months = 11),
    ONE_YEAR(years = 1)
  }
}
