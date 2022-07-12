package com.example.fragmentbookkotlin.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fragmentbookkotlin.model.Art

@Database(entities = [Art::class], version = 1)
@TypeConverters(Converters::class)
abstract class Artdb : RoomDatabase(){
    abstract fun artDao() : ArtDao
}