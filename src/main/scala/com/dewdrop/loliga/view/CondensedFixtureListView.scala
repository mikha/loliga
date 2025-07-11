package com.dewdrop.loliga.view

import com.dewdrop.loliga.model._
import org.scalajs.dom
import org.scalajs.dom.html.Div
import scalatags.JsDom
import scalatags.JsDom.all._

case class CondensedFixtureListView(fixtureList: Seq[FixtureRound],
                                    teamName: String,
                                    tournament: Tournament,
                                    pastFixtures: Boolean
                                   ) extends View {
  private val allFixturesFlag = input(`type` := "checkbox").render

  private def tournamentOnlyFixturesForTeam(fixture: (TournamentRound, Seq[Fixture])): Boolean =
    fixture._1.tournament == tournament && fixture._2.exists(f => f.host.name == teamName || f.visitor.name == teamName)

  private def allFixturesForTeam(fixture: (TournamentRound, Seq[Fixture])): Boolean =
    fixture._2.isEmpty || fixture._2.exists(f => f.host.name == teamName || f.visitor.name == teamName || f.host.nationTeam)

  private val fixturesContainer = div(fixturesListGroup(fixtureList.filter(tournamentOnlyFixturesForTeam))).render

  private def updateAllFixtureFlag(allFixtures: Boolean): Unit = {
    val fixtures = fixtureList.filter(if (allFixtures) allFixturesForTeam else tournamentOnlyFixturesForTeam)
    val newFixtureListGroup = fixturesListGroup(fixtures)
    fixturesContainer.replaceChild(newFixtureListGroup.render, fixturesContainer.firstChild)
  }

  private def renderTeam(team: Team, host: Boolean, round: Int, coveredFixture: Boolean): JsDom.TypedTag[Div] =
    div(
      `class` := "col-xs-3" + (if (host) " text-right" else ""),
      if (team.name == teamName) b(TeamView(team, round, coveredFixture).view()) else TeamView(team, round, coveredFixture).view()
    )

  private def renderFixtureRound(fixtureRound: FixtureRound) = {
    val (round, fixtures) = fixtureRound
    round.tournament match {
      case Data.Tournaments.loliga if fixtures.nonEmpty =>
        fixtures
          .filter(f => f.host.name == teamName || f.visitor.name == teamName)
          .map { fixture =>
            val coveredFixture: Boolean = fixture.participateInRound(round.round)
            div(
              `class` := s"list-group-item${if (coveredFixture) " list-group-item-info" else ""}",
              div(
                `class` := "row",
                div(
                  `class` := "col-xs-2",
                  span(round.date.toString + " - " + round.date.toWeekDay(false))
                ),
                renderTeam(fixture.host, host = true, round.round, coveredFixture),
                div(`class` := "col-xs-1 text-center", " - "),
                renderTeam(fixture.visitor, host = false, round.round, coveredFixture),
                div(
                  `class` := "col-xs-3",
                  a(href := round.link.address, round.toString)
                )
              )
            )
          }
      case Data.Tournaments.nation if fixtures.nonEmpty =>
        val teamLinksAndImgSrcs = fixtures.map { fixture =>
          val teamLink = fixture.host.link.address
          val imgSrc = if (fixture.host == Data.SeasonData.spain) "https://pefl.ru/system/img/g/int.gif"
          else "https://pefl.ru/system/img/g/u21.gif"
          (teamLink, imgSrc)
        }.distinct
        Seq(
          div(
            `class` := "list-group-item",
            div(
              `class` := "row",
              div(
                `class` := "col-xs-2",
                span(round.date.toString + " - " + round.date.toWeekDay(false))
              ),
              div(
                `class` := "col-xs-10",
                teamLinksAndImgSrcs.map {
                  case (teamLink, imgSrc) =>
                    a(href := teamLink, img(src := imgSrc), marginRight := "5px")
                }
              )
            )
          )
        )
      case Data.Tournaments.other =>
        Seq(
          div(
            `class` := "list-group-item",
            div(
              `class` := "row",
              div(
                `class` := "col-xs-2",
                span(round.date.toString + " - " + round.date.toWeekDay(false))
              ),
              div(
                `class` := "col-xs-10",
                img(src := "https://pefl.ru/system/img/g/ball1.gif", widthA := "15")
              )
            )
          )
        )
      case Data.Tournaments.cup =>
        Seq(
          div(
            `class` := "list-group-item",
            div(
              `class` := "row",
              div(
                `class` := "col-xs-2",
                span(round.date.toString + " - " + round.date.toWeekDay(false))
              ),
              div(
                `class` := "col-xs-10",
                img(src := "https://pefl.ru/plugins/s/topcontributors/img/cup-1.gif", widthA := "15")
              )
            )
          )
        )
      case Data.Tournaments.euro =>
        Seq(
          div(
            `class` := "list-group-item",
            div(
              `class` := "row",
              div(
                `class` := "col-xs-2",
                span(round.date.toString + " - " + round.date.toWeekDay(false))
              ),
              div(
                `class` := "col-xs-10",
                img(src := "https://pefl.ru/system/img/g/e.gif", widthA := "15")
              )
            )
          )
        )
      case _ =>
        Seq.empty
    }
  }

  private def fixturesListGroup(fixtures: Seq[FixtureRound]) =
    div(`class` := "list-group", fixtures.flatMap(renderFixtureRound))

  override def view(): JsDom.TypedTag[Div] = {
    if (pastFixtures) {
      div(`class` := "container-fluid", fixturesContainer)
    }
    else {
      allFixturesFlag.onchange = (_: dom.Event) => {
        updateAllFixtureFlag(allFixturesFlag.checked)
      }
      div(
        `class` := "container-fluid",
        div(
          `class` := "checkbox",
          label(allFixturesFlag, "Все игровые дни"),
        ),
        fixturesContainer
      )
    }
  }
}
