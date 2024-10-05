package com.ice.metadata.services

import com.ice.metadata.persistence.entities.{AlbumDTO, ArtistDTO, Image}
import com.ice.metadata.persistence.{AlbumPersistence, ArtistPersistence}
import com.ice.metadata.repositories.{AlbumRepository, ArtistRepository}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsJson, Headers}
import play.api.test.Helpers._
import play.api.test._

import java.time.LocalDate
import java.util.{Calendar, Date, UUID}

class AlbumServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val fakeArtist = ArtistDTO(Some(UUID.fromString("b6640c2e-1c7a-471f-b734-1ad643661d50")), "artist", Array(Image("", 200, 200)), None, None)

  val fakeAlbum = AlbumDTO(Some(UUID.fromString("c29d743e-e124-4889-a587-9151238fc25c")), "album",new Date(), "", Image("", 200, 200), Array(UUID.fromString("c29d743e-e124-4889-a587-9151238fc25c")), None)

  "AlbumService" should {

    "save album" in {
      val albumRepository =  new AlbumRepository(AlbumPersistence)
      val artistRepository = new ArtistRepository(ArtistPersistence)
      val albumServiceImpl = new AlbumServiceImpl(albumRepository, artistRepository)

      val artist = artistRepository.save(fakeArtist.toArtist())
      artist.fold(e => (), res => res.name mustBe fakeArtist.name)

      val album = albumServiceImpl.save(fakeAlbum)
      album.fold(e => (), res => res.name mustBe fakeAlbum.name)
    }
  }
}
