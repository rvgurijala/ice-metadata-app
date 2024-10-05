package com.ice.metadata.persistence

import com.ice.metadata.persistence.entities.{Album, Artist, Track}

import java.util.UUID
import scala.collection.mutable.ListBuffer

trait Persistence[A, B, C[_]]{
  val db: C[A]
  def insert(entity: A): A
  def find(id: B): Option[A]
  def update(entity: A): Unit
  def delete(id: B): Unit
  def findAll(): List[A]
}

object ArtistPersistence extends Persistence[Artist, UUID, ListBuffer] {

  override val db: ListBuffer[Artist] = ListBuffer.empty

  override def insert(entity: Artist): Artist = {
    val idx = db.indexWhere(_.id == entity.id)
    val newEntity = entity.copy(id = UUID.randomUUID())
    if (idx > -1) throw new RuntimeException(s"Duplicate element with id: ${entity.id}")
    else db.addOne(newEntity)

    newEntity
  }

  override def find(id: UUID): Option[Artist] = db.find(_.id == id)

  override def update(entity: Artist): Unit = {
    val idx = db.indexWhere(_.id == entity.id)
    if (idx > -1) db.update(idx, entity)
    else throw new RuntimeException(s"No element to update with id: ${entity.id}")
  }

  override def delete(id: UUID): Unit = {
    val idx = db.indexWhere(_.id == id)
    if (idx > -1) db.remove(idx)
    else throw new RuntimeException(s"No element to delete with id: ${id}")
  }

  override def findAll(): List[Artist] = db.toList
}


trait TrackPersistence[A, B, C[_]] extends Persistence[Track, UUID, ListBuffer] {
  def findAllByArtistId(artistId: UUID): List[Track]
}

object TrackPersistence extends TrackPersistence[Track, UUID, ListBuffer] {

  override val db: ListBuffer[Track] = ListBuffer.empty

  override def insert(entity: Track): Track = {
    val idx = db.indexWhere(_.id == entity.id)
    val newEntity = entity.copy(id = UUID.randomUUID())
    if (idx > -1) throw new RuntimeException(s"Duplicate element with id: ${entity.id}")
    else db.addOne(newEntity)

    newEntity
  }

  override def find(id: UUID): Option[Track] = db.find(_.id == id)

  override def update(entity: Track): Unit = {
    val idx = db.indexWhere(_.id == entity.id)
    if (idx > -1) db.update(idx, entity)
    else throw new RuntimeException(s"No element to update with id: ${entity.id}")
  }

  override def delete(id: UUID): Unit = {
    val idx = db.indexWhere(_.id == id)
    if (idx > -1) db.remove(idx)
    else throw new RuntimeException(s"No element to delete with id: ${id}")
  }

  override def findAll(): List[Track] = db.toList

  def findAllByArtistId(artistId: UUID): List[Track] = db.filter(track => track.artists.contains(artistId)).toList
}


object AlbumPersistence extends Persistence[Album, UUID, ListBuffer] {

  override val db: ListBuffer[Album] = ListBuffer.empty

  override def insert(entity: Album): Album = {
    val idx = db.indexWhere(_.id == entity.id)
    val newEntity = entity.copy(id = UUID.randomUUID())
    if (idx > -1) throw new RuntimeException(s"Duplicate element with id: ${entity.id}")
    else db.addOne(newEntity)

    newEntity
  }

  override def find(id: UUID): Option[Album] = db.find(_.id == id)

  override def update(entity: Album): Unit = {
    val idx = db.indexWhere(_.id == entity.id)
    if (idx > -1) db.update(idx, entity)
    else throw new RuntimeException(s"No element to update with id: ${entity.id}")
  }

  override def delete(id: UUID): Unit = {
    val idx = db.indexWhere(_.id == id)
    if (idx > -1) db.remove(idx)
    else throw new RuntimeException(s"No element to delete with id: ${id}")
  }

  override def findAll(): List[Album] = db.toList
}




