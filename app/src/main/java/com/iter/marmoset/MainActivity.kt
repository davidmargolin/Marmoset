package com.iter.marmoset

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

// sign in response id and providers used with AuthUI
private const val RC_SIGN_IN = 420
private val providers = mutableListOf(
        AuthUI.IdpConfig.EmailBuilder().build())

class MainActivity : AppCompatActivity(), SalonFragment.OnListFragmentInteractionListener{
    var bottomnav : BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setup bottom navigation bar
        bottomnav = navigation
        // bottomnav!!.visibility = View.GONE
        bottomnav!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // user already signed in
            bottomnav!!.selectedItemId = R.id.salons

        }else{
            // handle sign in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)

        }


    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
        // TODO: decide on which navigation menus to include
            R.id.salons -> {
                val salonFragment: Fragment = SalonFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, salonFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.appointments -> {
                val salonFragment: Fragment = SalonFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, salonFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                val salonFragment: Fragment = SalonFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, salonFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in - update vaccines
                if (bottomnav != null){
                    bottomnav!!.selectedItemId = R.id.salons
                }
            } else {
                // TODO: handle sign in failed
            }
        }
        bottomnav!!.selectedItemId = R.id.salons

    }

    override fun onListFragmentInteraction(salon: Salon) {
        val intent = Intent(this, SalonActivity::class.java).apply {
            putExtra("address", salon.address)
            putExtra("name", salon.name)
            putExtra("image_url", salon.image_url)
            putExtra("slideshow", salon.slideshow)
        }
        startActivity(intent)
    }

}
