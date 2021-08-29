package com.lachlanmckee.revisit.date

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.lachlanmckee.revisit.AnnotationMapper
import com.lachlanmckee.revisit.ModelMapper
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UElement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UnstableApiUsage")
class RevisitDateDetector : Detector(), SourceCodeScanner {

  companion object {
    private const val DEFAULT_DAYS_IN_FUTURE = 30

    val ISSUE = Issue.create(
      id = "RevisitDate",
      briefDescription = "Revisit date has been exceeded",
      explanation = "`Revisit date has been exceeded",
      category = Category.MESSAGES,
      severity = Severity.ERROR,
      implementation = Implementation(
        RevisitDateDetector::class.java,
        Scope.JAVA_FILE_SCOPE
      )
    )

    var dateProvider: DateProvider = StandardDateProvider()
  }

  override fun getApplicableUastTypes(): List<Class<out UElement>> {
    return listOf(UAnnotation::class.java)
  }

  override fun createUastHandler(context: JavaContext): UElementHandler {
    return object : UElementHandler() {
      override fun visitAnnotation(node: UAnnotation) {
        val model: Model =
          when (node.qualifiedName) {
            "com.lachlanmckee.revisit.RevisitFromDate" -> {
              getModel(context, node) {
                try {
                  getIntegerValue(fieldName = "daysInFuture", reportError = false)
                } catch (e: Exception) {
                  DEFAULT_DAYS_IN_FUTURE
                }
              }
            }
            "com.lachlanmckee.revisit.RevisitByDate" -> {
              getModel(context, node) { 0 }
            }
            else -> null
          } ?: return

        handleModel(context, node, model)
      }
    }
  }

  private fun handleModel(context: JavaContext, node: UAnnotation, model: Model) {
    val revisitDate: Date = mapModelToRevisitDate(context, node, model) ?: return
    val currentDate: Date = dateProvider.currentDate()

    if (currentDate.after(revisitDate)) {
      val formattedDate = dateFormatter.get().format(revisitDate)
      context.report(
        issue = ISSUE,
        scope = node,
        location = context.getLocation(node),
        message = "Revisit date has been exceeded. Date: $formattedDate, Reason: ${model.reason}"
      )
    }
  }

  private fun getModel(
    context: JavaContext,
    annotation: UAnnotation,
    daysInFutureFunc: ModelMapper.() -> Int
  ): Model? =
    AnnotationMapper().mapToModel(context, annotation) {
      Model(
        day = getIntegerValue("day"),
        month = getIntegerValue("month"),
        year = getIntegerValue("year"),
        reason = getStringValue("reason"),
        daysInFuture = daysInFutureFunc(this)
      )
    }

  private fun mapModelToRevisitDate(context: JavaContext, node: UAnnotation, model: Model): Date? =
    try {
      GregorianCalendar(model.year, model.month - 1, model.day)
        .apply {
          isLenient = false
          add(Calendar.DAY_OF_YEAR, model.daysInFuture)
        }
        .time
    } catch (e: Exception) {
      context.report(
        issue = ISSUE,
        scope = node,
        location = context.getLocation(node),
        message = "Date is invalid"
      )
      null
    }

  private val dateFormatter = object : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat = SimpleDateFormat("dd MMM yyyy")
  }

  private data class Model(
    val day: Int,
    val month: Int,
    val year: Int,
    val reason: String,
    val daysInFuture: Int
  )
}
