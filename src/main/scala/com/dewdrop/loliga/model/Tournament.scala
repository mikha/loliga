package com.dewdrop.loliga.model

case class Tournament(name: String, link: Link)

case class TournamentRound(tournament: Tournament, round: Int, date: LocalDate, link: Link) {
  override def toString: String = s"${tournament.name} - тур $round"
}
