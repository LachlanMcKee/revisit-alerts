# Revisit Alerts

Provides a variety of 'revisit' annotations that alert developers (using lint rules) when code needs to be revisited.

## How to use

The annotations are used in the following ways:

### RevisitDate

The `RevisitByDate` annotation allows you to specify a date in the future when something should be revisited.

```kotlin
@RevisitByDate(year = 3000, month = 1, day = 1, reason = "For testing purposes")
fun foo() {
}
```

### RevisitFromDate

The `RevisitFromDate` annotation allows you to specify a date (typically the date which the annotation was added) and a number of days in the future when something should be revisited.

```kotlin
@RevisitFromDate(year = 2000, month = 1, day = 1, daysInFuture = 365, reason = "For testing purposes")
fun foo() {
}
```

To see this in practice, please take a look at the [revisit-alerts-sample](revisit-alerts-sample) directory.

## Download
This library is available on Maven, you can add it to your project using the following gradle dependencies:

```gradle
implementation 'net.lachlanmckee.revisit:alerts:1.0.0'
```
