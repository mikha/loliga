package com.dewdrop.loliga.model

trait Season {
  def name: String
  def participantsOnly: Boolean
  def version: String
  protected def matchDays: FixtureList
  private lazy val filteredMatchDays: FixtureList =
    if (participantsOnly)
      matchDays.map {
        case (round, fixtures) =>
          round -> fixtures.filter(_.hasParticipant)
      }
    else
      matchDays
  def fixtureList: FixtureList = filteredMatchDays
  def teams: Seq[Team] = {
    filteredMatchDays
      .flatMap(_._2)
      .flatMap(f => Seq(f.host, f.visitor))
      .distinct
      .filter(t => !participantsOnly || t.participant)
      .sortBy(_.name)
  }
}
