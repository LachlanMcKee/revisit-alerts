package com.lachlanmckee.revisit

import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UNamedExpression

fun <T> UAnnotation.mapToModel(func: List<UNamedExpression>.() -> T): T =
  func(attributeValues)

fun List<UNamedExpression>.getIntegerValue(fieldName: String): Int =
  getStringValue(fieldName).toInt()

fun List<UNamedExpression>.getStringValue(fieldName: String): String =
  first { it.name == fieldName }.expression.evaluate().toString()
