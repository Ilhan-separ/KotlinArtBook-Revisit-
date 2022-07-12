package com.example.fragmentbookkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.fragmentbookkotlin.R
import com.example.fragmentbookkotlin.adapter.ArtAdapter
import com.example.fragmentbookkotlin.databinding.FragmentMainBinding
import com.example.fragmentbookkotlin.model.Art
import com.example.fragmentbookkotlin.roomdb.ArtDao
import com.example.fragmentbookkotlin.roomdb.Artdb
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()
    private lateinit var db : Artdb
    private lateinit var artDao: ArtDao
    private var artList : List<Art>? = null
    private lateinit var artAdapter: ArtAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        db = Room.databaseBuilder(
            activity!!.applicationContext,
            Artdb::class.java,
            "Arts"
        ).build()
        artDao = db.artDao()
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        compositeDisposable.add(
            artDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )


        _binding = FragmentMainBinding.inflate(inflater,container,false)


        return binding.root
    }

    private fun handleResponse(artList: List<Art>){
        binding.recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        artAdapter = ArtAdapter(artList)
        binding.recyclerView.adapter = artAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment("button")
            Navigation.findNavController(it).navigate(action)
        }

    }

}

