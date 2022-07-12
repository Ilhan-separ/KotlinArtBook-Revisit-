package com.example.fragmentbookkotlin.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.fragmentbookkotlin.R
import com.example.fragmentbookkotlin.adapter.ArtAdapter
import com.example.fragmentbookkotlin.adapter.selectedList
import com.example.fragmentbookkotlin.databinding.FragmentDetailBinding
import com.example.fragmentbookkotlin.databinding.FragmentMainBinding
import com.example.fragmentbookkotlin.model.Art
import com.example.fragmentbookkotlin.roomdb.ArtDao
import com.example.fragmentbookkotlin.roomdb.Artdb
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()
    private lateinit var db : Artdb
    private lateinit var artDao: ArtDao
    var info : String = ""
    var idx : Int = 0

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            activity!!.applicationContext,
            Artdb::class.java,
            "Arts"
        ).build()
        artDao = db.artDao()

        registerLauncher()

        arguments?.let {
            info = DetailFragmentArgs.fromBundle(it).info
            idx = DetailFragmentArgs.fromBundle(it).id
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater,container,false)

        // Data storing
        binding.saveButton.setOnClickListener {
            if (selectedBitmap != null) {
                val art = Art(
                    binding.editTextArtName.text.toString(),
                    binding.editTextArtistName.text.toString(),
                    binding.editTextYear.text.toString(),
                    makeSmallerBitmap(selectedBitmap!!,300)
                )

                compositeDisposable.add(
                    artDao.insert(art)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { this.handleResponse(it) }
                )

            }else{
                println("Paşam bitmap null geldi.")
            }
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Image selecting permissions
        binding.imageView.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Snackbar.make(it, "Permission needed for Gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                }
            }



        // Card clicked data passing
        if (info == "card" && _binding != null&& selectedList != null){
            println("İd : "+idx+" İnfo : "+info+" list: "+ selectedList!!.artist)
            binding.saveButton.visibility = View.INVISIBLE


            binding.editTextArtName.setText(selectedList!!.artName)
            binding.editTextArtistName.setText(selectedList!!.artist)
            binding.editTextYear.setText(selectedList!!.year)
            binding.imageView.setImageBitmap(selectedList!!.image)

        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun makeSmallerBitmap(image : Bitmap,maximumSize: Int) : Bitmap {
        var width = image.width
        var heigth = image.height

        var bitmapRatio : Double = width.toDouble()/heigth.toDouble()

        if (bitmapRatio>1){
            width = maximumSize
            val scaledHeight = width/bitmapRatio
            heigth = scaledHeight.toInt()
        }else{
            heigth = maximumSize
            val scaledWeight = heigth * bitmapRatio
            width = scaledWeight.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,heigth,true)

    }



    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult != null){
                    val imageUri = intentFromResult.data
                    if (imageUri != null ){
                        try {
                            if (Build.VERSION.SDK_INT>=28){
                                val source = ImageDecoder.createSource(requireActivity().contentResolver,imageUri)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }else{
                                selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,imageUri)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }

                        }catch (e : Exception){
                            e.printStackTrace()

                        }
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result->
            if (result){
                val  intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                Toast.makeText(activity!!.baseContext,"Permission Needed",Toast.LENGTH_LONG).show()
            }
        }


    }


    private fun handleResponse(it : View){
        val action = DetailFragmentDirections.actionDetailFragmentToMainFragment()
        Navigation.findNavController(it).navigate(action)
    }

}