package com.dewdrop.loliga.model

trait Season {
  def name: String
  def participantsOnly: Boolean
  def version: String
  protected def matchDays: Seq[FixtureRound]
  def fixtureList: Seq[FixtureRound] = matchDays
  def teams: Seq[Team] = {
    matchDays
      .flatMap(_._2)
      .flatMap(f => Seq(f.host, f.visitor))
      .view
      .distinct
      .filter(t => t.link != Link.noLink && !t.nationTeam)
      .filter(t => !participantsOnly || t.participant)
      .toSeq
      .sortBy(_.name)
  }
}
