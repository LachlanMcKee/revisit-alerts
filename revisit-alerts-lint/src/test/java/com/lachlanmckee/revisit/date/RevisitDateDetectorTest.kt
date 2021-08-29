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
    testDetector(day = 11, month = 9, year = 2021, testFile = classWithRevisitByDate) {
      expect(
        """
src/RevisitByDateExample.java:2: Error: Revisit date has been exceeded. Date: 10 Sep 2021, Reason: Test reason [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 10, month = 9, year = 2021, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
      )
    }
  }

  @Test
  fun `GIVEN date after revisit from date WHEN detect THEN expect date exceeded error`() {
    testDetector(day = 21, month = 9, year = 2021, testFile = classWithRevisitFromDate) {
      expect(
        """
src/RevisitFromDateExample.java:2: Error: Revisit date has been exceeded. Date: 20 Sep 2021, Reason: Test reason [RevisitDate]
@com.lachlanmckee.revisit.RevisitFromDate(day = 10, month = 9, year = 2021, reason = "Test reason", daysInFuture = 10)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
      )
    }
  }

  @Test
  fun `GIVEN date equals revisit by date WHEN detect THEN expect no errors`() {
    testDetector(day = 10, month = 9, year = 2021, testFile = classWithRevisitByDate) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date equals revisit from date WHEN detect THEN expect no errors`() {
    testDetector(day = 20, month = 9, year = 2021, testFile = classWithRevisitFromDate) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date before revisit by date WHEN detect THEN expect no errors`() {
    testDetector(day = 9, month = 9, year = 2021, testFile = classWithRevisitByDate) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date before revisit from date WHEN detect THEN expect no errors`() {
    testDetector(day = 19, month = 9, year = 2021, testFile = classWithRevisitFromDate) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN by date with negative day WHEN detect THEN expect invalid date error`() {
    testDetector(
      testFile = java(
        """
@com.lachlanmckee.revisit.RevisitByDate(day = -1, month = 1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = -1, month = 1, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
@com.lachlanmckee.revisit.RevisitByDate(day = 32, month = 1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 32, month = 1, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by date with negative month WHEN detect THEN expect invalid date error`() {
    testDetector(
      testFile = java(
        """
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = -1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = -1, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN by date with invalid month WHEN detect THEN expect invalid date error`() {
    testDetector(
      testFile = java(
        """
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 13, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 13, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 1, year = -1, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Date is invalid [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 1, year = -1, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
@com.lachlanmckee.revisit.RevisitByDate(month = 1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Field 'day' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(month = 1, year = 2020, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 1, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Field 'year' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 1, reason = "Test reason")
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 1, year = 2020)
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expect(
          """
src/RevisitFromDateExample.java:2: Error: Field 'reason' was not found [RevisitDate]
@com.lachlanmckee.revisit.RevisitByDate(day = 1, month = 1, year = 2020)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
        )
      }
    )
  }

  @Test
  fun `GIVEN from revisit days in future is missing WHEN detect THEN expect no errors`() {
    testDetector(
      testFile = java(
        """
@com.lachlanmckee.revisit.RevisitFromDate(day = 1, month = 1, year = 2020, reason = "Test reason")
class RevisitFromDateExample {}
"""
      ),
      validateFunc = {
        expectClean()
      }
    )
  }

  private fun testDetector(
    day: Int = 1,
    month: Int = 1,
    year: Int = 2020,
    testFile: TestFile,
    validateFunc: TestLintResult.() -> Unit
  ) {
    RevisitDateDetector.dateProvider =
      TestDateProvider().apply {
        mockDate(GregorianCalendar(year, month - 1, day).time)
      }

    validateFunc(
      lint()
        .files(testFile)
        .allowMissingSdk()
        .run()
    )
  }

  private companion object {
    private val classWithRevisitByDate = java(
      """
@com.lachlanmckee.revisit.RevisitByDate(day = 10, month = 9, year = 2021, reason = "Test reason")
class RevisitByDateExample {}
"""
    )

    private val classWithRevisitFromDate = java(
      """
@com.lachlanmckee.revisit.RevisitFromDate(day = 10, month = 9, year = 2021, reason = "Test reason", daysInFuture = 10)
class RevisitFromDateExample {}
"""
    )
  }
}
