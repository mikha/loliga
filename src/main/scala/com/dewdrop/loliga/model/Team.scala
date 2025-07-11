package com.dewdrop.loliga.model

import com.dewdrop.loliga.model.Team.TupleConversions
import org.scalajs.dom.html.Element
import scalatags.JsDom
import scalatags.JsDom.all._

case class Team(name: String, link: Link, nationTeam: Boolean, roundEuroPlayer: Seq[RoundEuroPlayer], roundParticipate: Seq[RoundParticipate], extraNote: Option[String]) {
  private lazy val roundParticipateDesc = roundParticipate.sortBy(_.fromRound).reverse
  private lazy val roundEuroPlayerDesc = roundEuroPlayer.sortBy(_.fromRound).reverse

  def participateInRound(round: Int): Boolean = roundParticipateDesc.find(_.fromRound <= round).exists(_.participate)

  def euroPlayerInRound(round: Int): Boolean = roundEuroPlayerDesc.find(_.fromRound <= round).exists(_.euroPlayer)

  def maybeRenderNote(includeNode: Boolean): Option[JsDom.all.ConcreteHtmlTag[Element]] =
    extraNote
      .filter(n => includeNode && n.trim.nonEmpty)
      .map { note =>
        span(title := note.trim, " *")
      }

  lazy val participant: Boolean = roundParticipate.exists(_.participate)

  def notEuroPlayerFromRound(round: Int): Team =
    copy(roundEuroPlayer = roundEuroPlayer :+ (round -> false).asRoundEuroPlayer)

  def notParticipantPlayerFromRound(round: Int): Team =
    copy(roundParticipate = roundParticipate :+ (round -> false).asRoundParticipate)
}

case class RoundParticipate(fromRound: Int, participate: Boolean)

case class RoundEuroPlayer(fromRound: Int, euroPlayer: Boolean)

object Team {
  def apply(name: String, link: Link, euroPlayer: Boolean, participate: Boolean, extraNote: String): Team =
    Team(name, link, nationTeam = false, Seq((1 -> euroPlayer).asRoundEuroPlayer), Seq[RoundParticipate]((1 -> participate).asRoundParticipate), Some(extraNote))

  def apply(name: String, link: Link, euroPlayer: Boolean, participate: Boolean): Team =
    Team(name, link, nationTeam = false, Seq((1 -> euroPlayer).asRoundEuroPlayer), Seq[RoundParticipate]((1 -> participate).asRoundParticipate), None)

  def nationTeam(name: String, link: Link): Team =
    Team(name, link, nationTeam = true, Seq((1 -> false).asRoundEuroPlayer), Seq[RoundParticipate]((1 -> false).asRoundParticipate), None)

  implicit class TupleConversions(val roundFlag: (Int, Boolean)) extends AnyVal {
    def asRoundParticipate: RoundParticipate =
      RoundParticipate(fromRound = roundFlag._1, participate = roundFlag._2)

    def asRoundEuroPlayer: RoundEuroPlayer =
      RoundEuroPlayer(fromRound = roundFlag._1, euroPlayer = roundFlag._2)
  }
}