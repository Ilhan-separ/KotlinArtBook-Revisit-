package com.example.fragmentbookkotlin.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob


@Entity
class Art(
    @ColumnInfo(name = "artName")
    var artName: String,
    @ColumnInfo(name = "artist")
    var artist : String,
    @ColumnInfo(name = "year")
    var year : String,
    @ColumnInfo(name = "image")
    var image : Bitmap
)
{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0


}