package com.iter.marmoset

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


// sign in response id and providers used with AuthUI
private const val RC_SIGN_IN = 420
private val providers = mutableListOf(
        AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

class MainActivity : AppCompatActivity(), SalonFragment.OnListFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener{
    var bottomnav : BottomNavigationView? = null
    var query : String = "*"
    var floatingSearchView:FloatingSearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        bottomnav = navigation
        bottomnav!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        floatingSearchView = floating_search_view
        floatingSearchView!!.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                query = currentQuery.toString()
                bottomnav!!.selectedItemId = R.id.salons
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
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

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.salons -> {
                if (query=="*"){
                 floatingSearchView!!.setSearchText("")
                }
                val salonFragment: Fragment = SalonFragment.newInstance(query)
                supportFragmentManager.beginTransaction().replace(R.id.content_view, salonFragment).commit()
                query = "*"
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                floatingSearchView!!.setSearchText("")

                val user = FirebaseAuth.getInstance().currentUser!!

                val profileFragment: Fragment = ProfileFragment.newInstance(user.displayName, user.photoUrl.toString(), "My name is Matthew Delvaux, and I am a PhD candidate studying the history and archaeology of slavery during the Viking Age. In my research, I follow the flow of early medieval slavery from viking raids in the west, through the booming ports of the Scandinavian north, and out into the Islamic world. My dissertation is titled “Consuming Violence: Captivity and Slavery during the Viking Age.” My research has been funded in part by the American-Scandinavian Foundation and the Medieval Academy of America. ", user!!.email)
                supportFragmentManager.beginTransaction().replace(R.id.content_view, profileFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.appointments -> {
                floatingSearchView!!.setSearchText("")

                val bookingsFragment: Fragment = BookingsFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, bookingsFragment).commit()
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
            putExtra("description", salon.description)
            putExtra("image_url", salon.image_url)
            putExtra("slideshow", salon.slideshow)
        }
        startActivity(intent)
    }


}
