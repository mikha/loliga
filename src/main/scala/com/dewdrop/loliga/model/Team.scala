package com.dewdrop.loliga.model

import scala.language.implicitConversions

case class Team(name: String, link: Link, euroPlayer: Boolean, roundParticipate: RoundParticipate*) {
  private lazy val roundParticipateDesc = roundParticipate.sortBy(_.fromRound).reverse
  def participateInRound(round: Int): Boolean = roundParticipateDesc.find(_.fromRound <= round).exists(_.participate)

  lazy val participant: Boolean = roundParticipate.exists(_.participate)
}

case class RoundParticipate(fromRound: Int, participate: Boolean)

object Team {
  def apply(name: String, link: Link, euroPlayer: Boolean, participate: Boolean): Team =
    Team(name, link, euroPlayer, 1 -> participate)
}

object RoundParticipate {
  implicit def tupleToRoundParticipate(roundParticipate: (Int, Boolean)): RoundParticipate =
    RoundParticipate(fromRound = roundParticipate._1, participate = roundParticipate._2)
}