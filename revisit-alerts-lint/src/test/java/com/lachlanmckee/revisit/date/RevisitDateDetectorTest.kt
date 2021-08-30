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
@com.lachlanmckee.revisit.RevisitByDate(day = 10, month = Month.SEPTEMBER, year = 2021, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
src/RevisitFromDateExample.java:5: Error: Revisit date has been exceeded. Date: 17 Sep 2021, Reason: Test reason [RevisitDate]
@com.lachlanmckee.revisit.RevisitFromDate(day = 10, month = Month.SEPTEMBER, year = 2021, reason = "Test reason", delay = RevisitFromDate.Delay.ONE_WEEK)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(day = -1, month = Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = -1, month = Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(day = 32, month = Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 32, month = Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = Month.JANUARY, year = -1, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = Month.JANUARY, year = -1, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(month = Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'day' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(month = Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
@com.lachlanmckee.revisit.RevisitByDate(day = 1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Field 'month' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = Month.JANUARY, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'year' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = Month.JANUARY, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = Month.JANUARY, year = 2020)
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'reason' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = Month.JANUARY, year = 2020)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitFromDate(day = 1, month = Month.JANUARY, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:4: Error: Field 'delay' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitFromDate(day = 1, month = Month.JANUARY, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
        .files(monthEnum, revisitFromDateAnnotation, testFile)
        .allowMissingSdk()
        .run()
    )
  }

  private companion object {
    private val classWithRevisitByDate = java(
      """
import com.lachlanmckee.revisit.Month;

@com.lachlanmckee.revisit.RevisitByDate(day = 10, month = Month.SEPTEMBER, year = 2021, reason = "Test reason")
class RevisitByDateExample {}
"""
    )

    private val classWithRevisitFromDate = java(
      """
import com.lachlanmckee.revisit.Month;
import com.lachlanmckee.revisit.RevisitFromDate;

@com.lachlanmckee.revisit.RevisitFromDate(day = 10, month = Month.SEPTEMBER, year = 2021, reason = "Test reason", delay = RevisitFromDate.Delay.ONE_WEEK)
class RevisitFromDateExample {}
"""
    )

    private val monthEnum = kotlin(
      """
package com.lachlanmckee.revisit

enum class Month {
  JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
}
"""
    )

    private val revisitFromDateAnnotation = kotlin(
      """
package com.lachlanmckee.revisit

annotation class RevisitFromDate(
  val day: Int,
  val month: Month,
  val year: Int,
  val reason: String,
  val delay: Delay
) {
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
