package com.dewdrop.loliga.model

import scala.scalajs.js.Date

case class LocalDate(year: Int, month: Int, day: Int)
    extends Comparable[LocalDate] {
  import LocalDate._
  verify(this)
  private val date = new Date(year, month - 1, day, 12)
  def toWeekDay(long: Boolean = true): String = weekDay(date, long)
  override def toString: String = f"$day%02d-$month%02d-$year%04d"
  override def compareTo(o: LocalDate): Int = {
    year - o.year match {
      case 0 =>
        month - o.month match {
          case 0 => day - o.day
          case d => d
        }
      case d => d
    }
  }
}

object LocalDate {
  def verify(date: LocalDate): Unit = {
    val errors = validate(date)
    assert(errors.isEmpty, errors.mkString(", "))
  }
  def validate(date: LocalDate): Seq[String] = {
    import date._
    Seq(
      if (month > 0 && month < 13) None
      else Some(s"month value $month is incorrect"),
      if (day > 0 && day < 32) None else Some(s"day value $day is incorrect"),
      if (day > maxDayIn(year, month))
        Some(s"date $year-$month-$day is incorrect")
      else None
    ).flatten
  }
  def maxDayIn(year: Int, month: Int): Int = {
    month match {
      case 2 if year % 4 > 0 => 28
      case 2                 => 29
      case 4 | 6 | 9 | 11    => 30
      case _                 => 31
    }
  }
  def apply(date: Date): LocalDate = LocalDate(
    year = date.getFullYear().intValue(),
    month = date.getMonth().intValue() + 1,
    day = date.getDate().intValue()
  )
  def weekDay(date: Date, long: Boolean): String = date.getDay() match {
    case 1 => if (long) "Понедельник" else "Пн"
    case 2 => if (long) "Вторник" else "Вт"
    case 3 => if (long) "Среда" else "Ср"
    case 4 => if (long) "Четверг" else "Чт"
    case 5 => if (long) "Пятница" else "Пт"
    case 6 => if (long) "Суббота" else "Сб"
    case 0 => if (long) "Воскресенье" else "Вс"
    case a => "?" + a
  }
}
