package com.lachlanmckee.revisit.date

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.lachlanmckee.revisit.getIntegerValue
import com.lachlanmckee.revisit.mapToModel
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UElement
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Suppress("UnstableApiUsage")
class RevisitDateDetector : Detector(), SourceCodeScanner {

  companion object {
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
        if (node.qualifiedName == "com.lachlanmckee.revisit.RevisitDate") {
          val model = getModel(node)
          val revisitDate = mapModelToRevisitDate(model)
          val currentDate = dateProvider.currentDate()

          if (currentDate.after(revisitDate)) {
            context.report(
              issue = ISSUE,
              scope = node,
              location = context.getLocation(node),
              message = "Revisit date has been exceeded: ${dateFormatter.get().format(revisitDate)}"
            )
          }
        }
      }
    }
  }

  private fun getModel(annotation: UAnnotation): Model =
    annotation.mapToModel {
      Model(
        day = getIntegerValue("day"),
        month = getIntegerValue("month"),
        year = getIntegerValue("year")
      )
    }

  private fun mapModelToRevisitDate(model: Model): Date {
    return GregorianCalendar(model.year, model.month - 1, model.day).time
  }

  private val dateFormatter = object : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat = SimpleDateFormat("dd MMM yyyy")
  }

  private data class Model(
    val day: Int,
    val month: Int,
    val year: Int
  )
}
