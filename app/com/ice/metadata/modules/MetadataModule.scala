package com.ice.metadata.modules

import com.google.inject.AbstractModule
import com.ice.metadata.persistence.{AlbumPersistence, ArtistPersistence, TrackPersistence}
import com.ice.metadata.repositories.{AlbumRepository, ArtistRepository, TrackRepository}

class MetadataModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AlbumRepository]).toInstance(new AlbumRepository(AlbumPersistence))
    bind(classOf[ArtistRepository]).toInstance(new ArtistRepository(ArtistPersistence))
    bind(classOf[TrackRepository]).toInstance(new TrackRepository(TrackPersistence))
  }
}
