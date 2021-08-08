package com.lachlanmckee.revisit.date

import java.util.*

class StandardDateProvider : DateProvider {
  override fun currentDate(): Date = Date()
}
