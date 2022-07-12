package com.example.fragmentbookkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.room.Room
import com.example.fragmentbookkotlin.R
import com.example.fragmentbookkotlin.adapter.ArtAdapter
import com.example.fragmentbookkotlin.databinding.ActivityMainBinding
import com.example.fragmentbookkotlin.model.Art
import com.example.fragmentbookkotlin.roomdb.Artdb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



       /* artAdapter = ArtAdapter(artList = ArrayList<Art>())


        val db = Room.databaseBuilder(
            applicationContext,
            Artdb::class.java,
            "Arts"
        ).build()

        val artDao = db.artDao()
        /*compositeDisposable.add(
            artDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )*/*/



    }

    private fun handleRespose(artList: ArrayList<Art>){

    }


    fun saveFunc(view: View){

    }

    fun selectImageClicked(view: View){

    }


}