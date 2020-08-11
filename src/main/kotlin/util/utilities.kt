package util

import org.joda.time.DateTime
import java.time.LocalDate

fun DateTime.toJavaLocalDate(): LocalDate {
    return LocalDate.of(this.year, this.monthOfYear, this.dayOfMonth)
}

fun LocalDate.toDate(default: DateTime = DateTime(
    1900,
    1,
    1,
    0,
    0,
    0)): DateTime {
    return DateTime (this.year, this.monthValue, this.dayOfMonth, 0, 0)
}