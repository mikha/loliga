package com.dewdrop.loliga.view

import com.dewdrop.loliga.model._
import org.scalajs.dom
import org.scalajs.dom.html.{Element, Select}

import scala.scalajs.js.Date
import scalatags.JsDom
import scalatags.JsDom.all._

case class SeasonView(season: Season) extends View {
  private val allFixtures =
    season.fixtureList.filter(_._2.nonEmpty)
  private val fixtureListWithPastView =
    FixtureListWithPastView(allFixtures, LocalDate(new Date(Date.now()))) _
  private val teamSelector = TeamSelector(season.teams).view().render
  private val fixtureListContainer = div(
    fixtureListWithPastView(
      fixtures => FixtureListView(fixtures, season.participantsOnly)
    ).view()
  ).render
  override def view(): JsDom.all.ConcreteHtmlTag[Element] = {
    teamSelector.onchange = (_: dom.Event) => {
      val teamName = teamSelector.value
      fixtureListContainer.removeChild(fixtureListContainer.firstChild)
      val newView =
        if (teamName.nonEmpty)
          fixtureListWithPastView(
            fixtures => CondensedFixtureListView(fixtures, teamName)
          )
        else
          fixtureListWithPastView(
            fixtures => FixtureListView(fixtures, season.participantsOnly)
          )
      fixtureListContainer.appendChild(newView.view().render)
    }
    div(
      `class` := "container-fluid",
      h1(season.name, small(`class` := "pull-right", season.version)),
      teamSelector,
      fixtureListContainer
    )
  }
}

case class TeamSelector(teams: Seq[Team]) extends View {
  override def view(): JsDom.TypedTag[Select] =
    select(
      `class` := "form-control input-lg",
      option(value := "", "-- все команды --"),
      for (team <- teams) yield option(value := team.name, team.name)
    )
}

case class FixtureListView(fixtureList: FixtureList, participantsOnly: Boolean) extends View {
  private val children = fixtureList.map {
    case (round, fixtures) =>
      TournamentRoundView(
        round,
        fixtures.filter(f => !participantsOnly || f.hasParticipant)
      )
  }
  private val elem =
    div(paddingTop := "20px", for (child <- children) yield child.view())
  override def view(): JsDom.all.ConcreteHtmlTag[Element] = elem
}

case class TournamentRoundView(round: TournamentRound,
                               fixtures: Seq[Fixture]) extends View {
  private val children = fixtures.map(FixtureView(_, round))
  private val elem = div(
    `class` := "list-group",
    a(
      `class` := "list-group-item list-group-item-action active",
      href := round.link.address,
      s"${round.date} - $round"
    ),
    for (child <- children) yield child.view()
  )
  override def view(): JsDom.all.ConcreteHtmlTag[Element] = elem
}

case class FixtureView(fixture: Fixture, round: TournamentRound) extends View {
  private lazy val host =
    div(`class` := "col-xs-5 text-right", TeamView(fixture.host).view())
  private lazy val visitor =
    div(`class` := "col-xs-5", TeamView(fixture.visitor).view())
  private lazy val elem = div(
    `class` := s"list-group-item${if (fixture.participateInRound(round.round)) " list-group-item-info" else ""}",
    div(
      `class` := "row",
      host,
      div(`class` := "col-xs-1 text-center", " - "),
      visitor
    )
  )
  override def view(): JsDom.all.ConcreteHtmlTag[Element] = elem
}

case class TeamView(team: Team) extends View {
  override def view(): JsDom.all.ConcreteHtmlTag[Element] =
    a(href := team.link.address, team.name)
}
