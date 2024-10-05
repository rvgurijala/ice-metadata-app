package com.ice.metadata.services

import com.ice.metadata.persistence.entities.{AlbumDTO, ArtistDTO, Image, TrackDTO}
import com.ice.metadata.persistence.{AlbumPersistence, ArtistPersistence, TrackPersistence}
import com.ice.metadata.repositories.{AlbumRepository, ArtistRepository, TrackRepository}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test._

import java.util.{Date, UUID}

class TrackServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  val fakeArtist = ArtistDTO(Some(UUID.fromString("b6640c2e-1c7a-471f-b734-1ad643661d50")), "artist", Array(Image("", 200, 200)), None, None)

  val fakeAlbum = AlbumDTO(Some(UUID.fromString("c29d743e-e124-4889-a587-9151238fc25c")), "album", new Date(), "", Image("", 200, 200), Array(UUID.fromString("b6640c2e-1c7a-471f-b734-1ad643661d50")), None)

  val fakeTrack = TrackDTO(Some(UUID.fromString("c29d743e-e124-4889-a587-9151238fc25c")), "title", None, fakeAlbum.id.get, Array(UUID.fromString("b6640c2e-1c7a-471f-b734-1ad643661d50")), true, None, "", "", Array("jazz"), 200)

  "TrackService" should {

    "save track" in {
      val trackRepository =  new TrackRepository(TrackPersistence)
      val artistRepository = new ArtistRepository(ArtistPersistence)
      val albumRepository =  new AlbumRepository(AlbumPersistence)

      val artistService = new TrackServiceImpl(trackRepository, albumRepository, artistRepository)

      artistRepository.save(fakeArtist.toArtist())
      albumRepository.save(fakeAlbum.toAlbum())

      val artist = artistService.save(fakeTrack)
      artist.fold(e => (), res => res.title mustBe fakeArtist.name)
    }
  }
}
