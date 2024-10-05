package com.ice.metadata.persistence.entities

import play.api.libs.json.{Reads, Writes}

object ObjectType extends Enumeration {
  type ObjectType = Value
  val Track, Album, Artist = Value

  implicit val readsObjectType = Reads.enumNameReads(ObjectType)
  implicit val writesObjectType = Writes.enumNameWrites
}
