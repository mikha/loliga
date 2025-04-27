package com.dewdrop.loliga.model

case class Fixture(host: Team, visitor: Team) {
  def participateInRound(round: Int): Boolean =
    host.participateInRound(round) && visitor.participateInRound(round) && (host.euroPlayerInRound(round) || visitor.euroPlayerInRound(round))

  lazy val hasParticipant: Boolean = host.participant || visitor.participant
}
