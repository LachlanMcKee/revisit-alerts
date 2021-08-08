package com.lachlanmckee.revisit

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.lachlanmckee.revisit.date.RevisitDateDetector

@Suppress("UnstableApiUsage", "unused")
class IssueRegistry : IssueRegistry() {

  override val issues: List<Issue> = listOf(
    RevisitDateDetector.ISSUE
  )

  override val api: Int = CURRENT_API

  /**
   * See [com.android.tools.lint.detector.api.describeApi]
   */
  override val minApi: Int
    get() = 10

  override val vendor = Vendor(
    vendorName = "LachlanMcKee/revisit-alerts",
    identifier = "com.lachlanmckee.revisit:alerts:{version}",
    feedbackUrl = "https://github.com/LachlanMcKee/revisit-alerts/issues",
  )
}
