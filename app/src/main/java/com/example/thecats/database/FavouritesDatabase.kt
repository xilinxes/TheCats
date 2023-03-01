package com.example.thecats.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.thecats.database.dao.FavouritesDao
import com.example.thecats.database.entity.FavouritesEntity
import dagger.Module
import dagger.Provides

@Database(
    entities = [
        FavouritesEntity::class
    ],
    version = 1
)
abstract class FavouritesDatabase: RoomDatabase() {
    abstract fun favourites() : FavouritesDao
}