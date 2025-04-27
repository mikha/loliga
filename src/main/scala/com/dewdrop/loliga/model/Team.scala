package com.dewdrop.loliga.model

case class Team(name: String, link: Link, nationTeam: Boolean, roundEuroPlayer: Seq[RoundEuroPlayer], roundParticipate: Seq[RoundParticipate]) {
  private lazy val roundParticipateDesc = roundParticipate.sortBy(_.fromRound).reverse
  private lazy val roundEuroPlayerDesc = roundEuroPlayer.sortBy(_.fromRound).reverse

  def participateInRound(round: Int): Boolean = roundParticipateDesc.find(_.fromRound <= round).exists(_.participate)

  def euroPlayerInRound(round: Int): Boolean = roundEuroPlayerDesc.find(_.fromRound <= round).exists(_.euroPlayer)

  lazy val participant: Boolean = roundParticipate.exists(_.participate)
}

case class RoundParticipate(fromRound: Int, participate: Boolean)

case class RoundEuroPlayer(fromRound: Int, euroPlayer: Boolean)

object Team {
  def apply(name: String, link: Link, euroPlayer: Boolean, participate: Boolean): Team =
    Team(name, link, nationTeam = false, Seq((1 -> euroPlayer).asRoundEuroPlayer), Seq[RoundParticipate]((1 -> participate).asRoundParticipate))

  def nationTeam(name: String, link: Link): Team =
    Team(name, link, nationTeam = true, Seq((1 -> false).asRoundEuroPlayer), Seq[RoundParticipate]((1 -> false).asRoundParticipate))

  implicit class TupleConversions(val roundFlag: (Int, Boolean)) extends AnyVal {
    def asRoundParticipate: RoundParticipate =
      RoundParticipate(fromRound = roundFlag._1, participate = roundFlag._2)

    def asRoundEuroPlayer: RoundEuroPlayer =
      RoundEuroPlayer(fromRound = roundFlag._1, euroPlayer = roundFlag._2)
  }
}