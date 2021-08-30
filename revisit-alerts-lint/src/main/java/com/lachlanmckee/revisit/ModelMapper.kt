package com.lachlanmckee.revisit

import com.android.tools.lint.checks.findSelector
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UNamedExpression

class ModelMapper(
  private val context: JavaContext,
  private val node: UAnnotation,
  private val issue: Issue,
  private val fields: List<UNamedExpression>
) {
  fun getIntegerValue(fieldName: String, reportError: Boolean = true): Int =
    getStringValue(fieldName, reportError).toInt()

  fun getStringValue(fieldName: String, reportError: Boolean = true): String =
    getFieldAndStringValue(fieldName, reportError).second

  fun <T> getEnumValue(
    fieldName: String,
    enumMapper: (String) -> T,
    reportError: Boolean = true
  ): T {
    val (field, value) = getFieldAndStringValue(fieldName, reportError) {
      this.findSelector()?.asRenderString()
    }
    return try {
      enumMapper(value)
    } catch (e: Exception) {
      context.report(
        issue = issue,
        scope = node,
        location = context.getLocation(field),
        message = "Invalid enum value '$value'"
      )
      throw e
    }
  }

  private fun getFieldAndStringValue(
    fieldName: String,
    reportError: Boolean = true,
    valueExtractFunc: UExpression.() -> String? = { evaluate().toString() },
  ): Pair<UNamedExpression, String> =
    try {
      val field = fields.first { it.name == fieldName }
      field to requireNotNull(valueExtractFunc(field.expression))
    } catch (e: Exception) {
      if (reportError) {
        context.report(
          issue = issue,
          scope = node,
          location = context.getLocation(node),
          message = "Field '$fieldName' was not found"
        )
      }
      throw e
    }
}
