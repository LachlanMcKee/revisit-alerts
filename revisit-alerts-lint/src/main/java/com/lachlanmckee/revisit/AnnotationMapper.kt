package com.lachlanmckee.revisit

import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.UAnnotation

class AnnotationMapper {
  fun <T> mapToModel(context: JavaContext, annotation: UAnnotation, func: ModelMapper.() -> T): T? =
    try {
      func(ModelMapper(context, annotation, annotation.attributeValues))
    } catch (e: Exception) {
      null
    }
}
