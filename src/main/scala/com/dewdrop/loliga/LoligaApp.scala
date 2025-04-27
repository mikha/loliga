package com.dewdrop.loliga

import com.dewdrop.loliga.model.Data
import com.dewdrop.loliga.view.SeasonView
import org.scalajs.dom

object LoligaApp {

  def main(args: Array[String]): Unit = {
    val season = SeasonView(Data.SeasonData, Data.Tournaments.loliga)
    dom.document.body.appendChild(season.view().render)
  }
}
