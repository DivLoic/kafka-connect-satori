package fr.xebia.ldi.satori.common

import scala.util.matching.Regex

object SatoriConstant {

  val WEBSITE: String = "satori.com"
  val SDK_VERSION: String = "1.0.3"
  val END_POINT_REGEX: Regex = "(^wss://[\\w\\-\\.]+\\.com+$)".r

}
