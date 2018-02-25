package com.iter.marmoset

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sequencing.androidoauth.core.ISQAuthCallback
import com.sequencing.oauth.core.Token
import kotlinx.android.synthetic.main.activity_main.*


// sign in response id and providers used with AuthUI
private const val RC_SIGN_IN = 420
private val providers = mutableListOf(
        AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

class MainActivity : AppCompatActivity(), SalonFragment.OnListFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener, ISQAuthCallback {
    var bottomnav : BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        bottomnav = navigation
        bottomnav!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // setup bottom navigation bar
        var user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // user already signed in
            bottomnav!!.selectedItemId = R.id.salons

        }else{
            // handle sign in
            Log.e("help:","no user found");
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)

        }

        // bottomnav!!.visibility = View.GONE



    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
        // TODO: decide on which navigation menus to include

            R.id.salons -> {
                val salonFragment: Fragment = SalonFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, salonFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                val user = FirebaseAuth.getInstance().currentUser!!

                val profileFragment: Fragment = ProfileFragment.newInstance(user.displayName, user.photoUrl.toString(), "test", user!!.email)
                supportFragmentManager.beginTransaction().replace(R.id.content_view, profileFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.appointments -> {
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

    override fun onFragmentInteraction(uri: Uri?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun onAuthentication(token: Token) {
        Log.i("IDK", "Authenticated")
        // TODO: save token
        Log.e("this is the token!!!: ", token.accessToken)
        bottomnav!!.selectedItemId = R.id.profile
    }

    override fun onFailedAuthentication(e: Exception) {
        Log.w("IDK", "Failure to authenticate user")
    }


}
