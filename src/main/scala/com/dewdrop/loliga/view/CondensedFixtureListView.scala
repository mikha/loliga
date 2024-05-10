package com.dewdrop.loliga.view

import com.dewdrop.loliga.model._
import org.scalajs.dom.html.Div

import scalatags.JsDom
import scalatags.JsDom.all._

case class CondensedFixtureListView(
  fixtureList: FixtureList,
  teamName: String
) extends View {
  private def renderTeam(team: Team, host: Boolean): JsDom.TypedTag[Div] =
    div(
      `class` := "col-xs-3" + (if (host) " text-right" else ""),
      if (team.name == teamName) b(TeamView(team).view()) else TeamView(team).view()
    )
  private val fixturesElements = fixtureList.flatMap {
    case (round, fixtures) =>
      fixtures
        .filter(f => f.host.name == teamName || f.visitor.name == teamName)
        .map { fixture =>
          div(
            `class` := s"list-group-item${if (fixture.participateInRound(round.round)) " list-group-item-info" else ""}",
            div(
              `class` := "row",
              div(
                `class` := "col-xs-2",
                span(round.date.toString + " - " + round.date.toWeekDay(false))
              ),
              renderTeam(fixture.host, host = true),
              div(`class` := "col-xs-1 text-center", " - "),
              renderTeam(fixture.visitor, host = false),
              div(
                `class` := "col-xs-3",
                a(href := round.link.address, round.toString)
              )
            )
          )
        }
  }
  override def view(): JsDom.TypedTag[Div] =
    div(`class` := "list-group", paddingTop := "20px", fixturesElements)
}
