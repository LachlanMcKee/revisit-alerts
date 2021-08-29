package com.lachlanmckee.revisit

import com.android.tools.lint.detector.api.JavaContext
import com.lachlanmckee.revisit.date.RevisitDateDetector
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UNamedExpression

class ModelMapper(
  private val context: JavaContext,
  private val node: UAnnotation,
  private val fields: List<UNamedExpression>
) {
  fun getIntegerValue(fieldName: String, reportError: Boolean = true): Int =
    getStringValue(fieldName, reportError).toInt()

  fun getStringValue(fieldName: String, reportError: Boolean = true): String =
    try {
      fields.first { it.name == fieldName }.expression.evaluate().toString()
    } catch (e: Exception) {
      if (reportError) {
        context.report(
          issue = RevisitDateDetector.ISSUE,
          scope = node,
          location = context.getLocation(node),
          message = "Field '$fieldName' was not found"
        )
      }
      throw e
    }
}
