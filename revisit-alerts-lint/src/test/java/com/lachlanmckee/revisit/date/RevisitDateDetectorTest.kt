package com.lachlanmckee.revisit.date

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
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
  fun `GIVEN date after annotation date WHEN detect THEN expect date exceeded error`() {
    testDetector(day = 11, month = 9, year = 2021) {
      expect(
        """
src/com/lachlanmckee/revisit/android/ui/DeprecatedButton.java:6: Error: Revisit date has been exceeded: 10 Sep 2021 [RevisitDate]
  @com.lachlanmckee.revisit.RevisitDate(day = 10, month = 9, year = 2021)
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1 errors, 0 warnings
            """
      )
    }
  }

  @Test
  fun `GIVEN date equals annotation date WHEN detect THEN expect no errors`() {
    testDetector(day = 10, month = 9, year = 2021) {
      expectClean()
    }
  }

  @Test
  fun `GIVEN date before annotation date WHEN detect THEN expect no errors`() {
    testDetector(day = 9, month = 9, year = 2021) {
      expectClean()
    }
  }

  private fun testDetector(
    day: Int,
    month: Int,
    year: Int,
    validateFunc: TestLintResult.() -> Unit
  ) {
    RevisitDateDetector.dateProvider =
      TestDateProvider().apply {
        mockDate(GregorianCalendar(year, month - 1, day).time)
      }

    validateFunc(
      lint()
        .files(classWithRevisitDate)
        .allowMissingSdk()
        .run()
    )
  }

  private companion object {
    private val classWithRevisitDate = java(
      """
package com.lachlanmckee.revisit.android.ui;

class DeprecatedButton {

  @com.lachlanmckee.revisit.RevisitDate(day = 10, month = 9, year = 2021)
  String foo() {
    return "";
  }

}
"""
    )
  }
}
