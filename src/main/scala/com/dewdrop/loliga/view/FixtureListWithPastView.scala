package com.dewdrop.loliga.view

import com.dewdrop.loliga.model._
import org.scalajs.dom.html.Div

import scalatags.JsDom
import scalatags.JsDom.all._

case class FixtureListWithPastView(fixtureList: Seq[FixtureRound], date: LocalDate)(
  fixtureListView: (Seq[FixtureRound], Boolean) => View
) extends View {
  private val (pastFixtures, futureFixtures) =
    fixtureList.partition(_._1.date.compareTo(date) < 0)
  override def view(): JsDom.TypedTag[Div] = div(
    pastFixturesLinkIfPresent,
    fixtureListView(futureFixtures, false).view(),
    pastFixturesIfPresent
  )
  private def pastFixturesIfPresent =
    if (pastFixtures.nonEmpty)
      Seq(
        div(
          `class` := "container-fluid",
          a(name := "pastFixtures", ""),
          h4(`class` := "pull-left", "Прошлые игры"),
          br
        ),
        fixtureListView(pastFixtures.reverse, true).view()
      )
    else Seq.empty
  private def pastFixturesLinkIfPresent =
    if (pastFixtures.nonEmpty)
      Some(
        div(
          `class` := "container-fluid",
          h4(
            `class` := "pull-right",
            a(href := "#pastFixtures", "Прошлые игры")
          ),
          br
        )
      )
    else None
}
