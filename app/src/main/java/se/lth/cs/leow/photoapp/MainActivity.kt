package se.lth.cs.leow.photoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_new_photo.*


class MainActivity : AppCompatActivity() {
    private val mapFragment = MapsFragment()
    private val profileFragment = ProfileFragment()
    private val newPhotoFragment = NewPhotoFragment()
    private lateinit var viewModel:SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java);
        setContentView(R.layout.activity_main)
        coordinator_layout.bringToFront()
        //sätter lyssnare på menyknapparna, men även på floating action button
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mapsFragment -> switchFragment(mapFragment)
                R.id.profileFragment -> switchFragment(profileFragment)
            }
            true
        }
        fab_button.setOnClickListener {
            switchFragment(newPhotoFragment)
        }
    }
    //Byter fragment på den inneslutande fragmentcontainern i aktiviteten
    private fun switchFragment(fragment:Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}