package com.ice.metadata.services

import com.ice.metadata.persistence.entities.ArtistDTO
import com.ice.metadata.services.errors.ServiceError
import play.api.Logger

import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import scala.collection.mutable

class ArtistOfTheDayService @Inject()(artistService: ArtistServiceImpl){

  private val logger = Logger(getClass)

  def getArtistOfTheDay(): Either[ServiceError, ArtistDTO] = {
    val today: LocalDate = LocalDate.now()
    val cacheKey = today.toString

    Cache.get(cacheKey) match {
      case Some(artist) => Right(artist)
      case None =>
        val epoch: LocalDate = LocalDate.of(1990, 1, 1)
        val daysBetween: Long = ChronoUnit.DAYS.between(epoch, today)
        artistService.getAll() match {
          case Right(artists) => if(artists.isEmpty) {
            logger.error(s"No artist found for artist of the day")
            Left(ServiceError("No Artist found"))
          } else {
            val artistOfTheDay: ArtistDTO = artists((daysBetween % artists.size).toInt)
            logger.info(s"Artist of the day: $artistOfTheDay")
            Cache.set(cacheKey, artistOfTheDay)
            Right(artistOfTheDay)
          }
          case Left(e) => Left(e)
        }
    }
  }

  private object Cache {
    private val cache = mutable.Map[String, ArtistDTO]()
    def set(key: String, value: ArtistDTO): Unit = cache.put(key, value)
    def get(key: String): Option[ArtistDTO] = cache.get(key)
  }

}
