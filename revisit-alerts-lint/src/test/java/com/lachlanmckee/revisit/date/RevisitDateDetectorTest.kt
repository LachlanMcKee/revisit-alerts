package com.lachlanmckee.revisit.date

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestLintResult
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.lachlanmckee.revisit.TestDateProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@Suppress("UnstableApiUsage")
@RunWith(JUnit4::class)
class RevisitDateDetectorTest : LintDetectorTest() {

  override fun getIssues(): MutableList<Issue> =
    mutableListOf(RevisitDateDetector.ISSUE)

  override fun getDetector(): Detector = RevisitDateDetector()

  @Test
  fun `GIVEN date after revisit by date WHEN detect THEN expect date exceeded error`() {
    testDetector(
      day = 11,
      month = Month.SEPTEMBER,
      year = 2021,
      testFile = classWithRevisitByDate
    ) {
      expect(
        """
src/RevisitByDateExample.java:4: Error: Revisit date has been exceeded. Date: 10 Sep 2021, Reason: Test reason [RevisitDate]
@Revisit.ByDate(day = 10, month = Revisit.Month.SEPTEMBER, year = 2021, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
      )
    }
  }

  @Test
  fun `GIVEN date after revisit from date WHEN detect THEN expect date exceeded error`() {
    testDetector(
      day = 21,
      month = Month.SEPTEMBER,
      year = 2021,
      testFile = classWithRevisitFromDate
    ) {
      expect(
        """
src/RevisitFromDateExample.java:4: Error: Revisit date has been exceeded. Date: 17 Sep 2021, Reason: Test reason [RevisitDate]
@Revisit.FromDate(day = 10, month = Revisit.Month.SEPTEMBER, year = 2021, reason = "Test reason", delay = Revisit.Delay.ONE_WEEK)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
      )
    }
  }

  @Test
  fun `GIVEN date equals revisit by date WHEN detect THEN expect no errors`() {
    testDetector(
      day = 10,
      month = Month.SEPTEMBER,
      year = 2021,
      testFile = classWithRevisitByDate
    ) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date equals revisit from date WHEN detect THEN expect no errors`() {
    testDetector(
      day = 17,
      month = Month.SEPTEMBER,
      year = 2021,
      testFile = classWithRevisitFromDate
    ) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date before revisit by date WHEN detect THEN expect no errors`() {
    testDetector(day = 9, month = Month.SEPTEMBER, year = 2021, testFile = classWithRevisitByDate) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date before revisit from date WHEN detect THEN expect no errors`() {
    testDetector(
      day = 16,
      month = Month.SEPTEMBER,
      year = 2021,
      testFile = classWithRevisitFromDate
    ) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN by date with negative day WHEN detect THEN expect invalid date error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = -1, month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Date is invalid [RevisitDate]
@Revisit.ByDate(day = -1, month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by date with invalid day of month WHEN detect THEN expect invalid date error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = 32, month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Date is invalid [RevisitDate]
@Revisit.ByDate(day = 32, month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by date with negative year WHEN detect THEN expect invalid date error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, year = -1, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Date is invalid [RevisitDate]
@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, year = -1, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by revisit day is missing WHEN detect THEN expect field missing error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'day' was not found [RevisitDate]
@Revisit.ByDate(month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by revisit month is missing WHEN detect THEN expect field missing error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = 1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'month' was not found [RevisitDate]
@Revisit.ByDate(day = 1, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by revisit year is missing WHEN detect THEN expect field missing error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'year' was not found [RevisitDate]
@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by revisit reason is missing WHEN detect THEN expect field missing error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, year = 2020)
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'reason' was not found [RevisitDate]
@Revisit.ByDate(day = 1, month = Revisit.Month.JANUARY, year = 2020)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN from revisit days in future is missing WHEN detect THEN expect field missing error`() {
    testDetector(
      testFile = java(
        """
import com.lachlanmckee.revisit.Revisit;

@Revisit.FromDate(day = 1, month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'delay' was not found [RevisitDate]
@Revisit.FromDate(day = 1, month = Revisit.Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  private fun testDetector(
    day: Int = 1,
    month: Month = Month.JANUARY,
    year: Int = 2020,
    testFile: TestFile,
    validateFunc: TestLintResult.() -> Unit
  ) {
    RevisitDateDetector.dateProvider =
      TestDateProvider().apply {
        mockDate(GregorianCalendar(year, month.ordinal, day).time)
      }

    validateFunc(
      lint()
        .files(revisitObject, testFile)
        .allowMissingSdk()
        .run()
    )
  }

  private companion object {
    private val classWithRevisitByDate = java(
      """
import com.lachlanmckee.revisit.Revisit;

@Revisit.ByDate(day = 10, month = Revisit.Month.SEPTEMBER, year = 2021, reason = "Test reason")
class RevisitByDateExample {}
"""
    )

    private val classWithRevisitFromDate = java(
      """
import com.lachlanmckee.revisit.Revisit;

@Revisit.FromDate(day = 10, month = Revisit.Month.SEPTEMBER, year = 2021, reason = "Test reason", delay = Revisit.Delay.ONE_WEEK)
class RevisitFromDateExample {}
"""
    )

    private val revisitObject = kotlin(
      """
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
"""
    )
  }
}
