package com.dewdrop.loliga.view

import com.dewdrop.loliga.model._
import org.scalajs.dom.html.Div

import scalatags.JsDom
import scalatags.JsDom.all._

case class FixtureListWithPastView(fixtureList: FixtureList, date: LocalDate)(
  fixtureListView: FixtureList => View
) extends View {
  private val (pastFixtures, futureFixtures) =
    fixtureList.partition(_._1.date.compareTo(date) < 0)
  override def view(): JsDom.TypedTag[Div] = div(
    pastFixturesLinkIfPresent,
    fixtureListView(futureFixtures).view(),
    pastFixturesIfPresent
  )
  private def pastFixturesIfPresent =
    if (pastFixtures.nonEmpty)
      Seq(
        div(
          a(name := "pastFixtures", ""),
          h4(`class` := "pull-left", "Прошлые игры"),
          br
        ),
        fixtureListView(pastFixtures.reverse).view()
      )
    else Seq.empty
  private def pastFixturesLinkIfPresent =
    if (pastFixtures.nonEmpty)
      Some(
        div(
          h4(
            `class` := "pull-right",
            a(href := "#pastFixtures", "Прошлые игры")
          ),
          br
        )
      )
    else None
}
