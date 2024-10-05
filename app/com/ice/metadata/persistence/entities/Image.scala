package com.ice.metadata.persistence.entities

import play.api.libs.json.{Format, Json}

case class Image(url: String, height: Int, width: Int)

object Image {
  implicit val formatImage: Format[Image] = Json.format[Image]
}
