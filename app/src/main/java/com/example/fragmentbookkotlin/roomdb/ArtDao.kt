package com.example.fragmentbookkotlin.roomdb

import androidx.room.*
import com.example.fragmentbookkotlin.model.Art
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ArtDao {
    @Query("SELECT * FROM Art")
    fun getAll() : Flowable<List<Art>>
    @Query("SELECT * FROM Art WHERE id= :id")
    fun getSelected(id : Int) : Flowable<List<Art>>


    @Insert
    fun insert(art: Art) : Completable

    @Delete
    fun delete(art: Art) : Completable



}