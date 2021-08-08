package com.lachlanmckee.revisit

import com.lachlanmckee.revisit.date.DateProvider
import java.util.*

class TestDateProvider : DateProvider {
  private var mockDate: Date? = null

  fun mockDate(date: Date) {
    mockDate = date
  }

  override fun currentDate(): Date = requireNotNull(mockDate) { "Date is not mocked" }
}
