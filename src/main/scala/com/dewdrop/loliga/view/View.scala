package com.dewdrop.loliga.view

import org.scalajs.dom.html.Element

import scalatags.JsDom

trait View {
  def view(): JsDom.all.ConcreteHtmlTag[Element]
}
