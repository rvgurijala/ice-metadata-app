package com.ice.metadata.repositories

import com.ice.metadata.persistence.Persistence
import com.ice.metadata.persistence.entities.Artist
import com.ice.metadata.repositories.errors.RepositoryError

import java.util.UUID
import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.util.Try

class ArtistRepository (val db: Persistence[Artist, UUID, ListBuffer]) {

  def findById(id: UUID): Either[RepositoryError, Option[Artist]] = handleIfErrors(db.find(id))

  def save(artist: Artist): Either[RepositoryError, Artist] = handleIfErrors(db.insert(artist))

  def update(artist: Artist): Either[RepositoryError, Unit] = handleIfErrors(db.update(artist))

  def delete(id: UUID): Either[RepositoryError, Unit] = handleIfErrors(db.delete(id))

  def findAll(): Either[RepositoryError, List[Artist]] = handleIfErrors(db.findAll())

  private def handleIfErrors[A](f: => A) =
    Try(f).fold(e => Left(RepositoryError(e.getMessage)), v => Right(v))
}
