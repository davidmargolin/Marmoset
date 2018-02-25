package com.iter.marmoset

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.User
import com.github.kittinunf.fuel.Fuel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*


// sign in response id and providers used with AuthUI
private const val RC_SIGN_IN = 420
private val providers = mutableListOf(
        AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())
internal var db = FirebaseFirestore.getInstance()

class MainActivity : AppCompatActivity(), SalonFragment.OnListFragmentInteractionListener, ProfileFragment.OnProfileFragmentInteractionListener{
    var bottomnav : BottomNavigationView? = null
    var query : String = "*"
    var floatingSearchView:FloatingSearchView? = null
    var business: Boolean = false
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
            handlemenu(user);

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

                val bookingsFragment: Fragment = BookingsFragment.newInstance(false)
                supportFragmentManager.beginTransaction().replace(R.id.content_view, bookingsFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.biz_appointments -> {
                val bookingsFragment: Fragment = BookingsFragment.newInstance(true)
                supportFragmentManager.beginTransaction().replace(R.id.content_view, bookingsFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.biz_profile -> {
                val bizProfFragment: Fragment = BizProfFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, bizProfFragment).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun switcher(){
        val i = baseContext.packageManager
                .getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        startActivity(i)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Successfully signed in - update vaccines
                if (bottomnav != null){
                    handlemenu(FirebaseAuth.getInstance().currentUser!!)
                }
            } else {
                // TODO: handle sign in failed
            }
        }

    }



    override fun onFragmentInteraction(uri: Uri?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListFragmentInteraction(salon: Salon) {
        val intent = Intent(this, SalonActivity::class.java).apply {
            putExtra("address", salon.address)
            putExtra("name", salon.name)
            putExtra("id", salon.id)
            putExtra("description", salon.description)
            putExtra("image_url", salon.image_url)
            putExtra("slideshow", salon.slideshow)
        }
        startActivity(intent)
    }

    fun handlemenu(user:FirebaseUser){
        db.collection("Users").document(user.uid).get().addOnCompleteListener {task->
            var documentSnapshot:DocumentSnapshot = task.getResult();
            if (documentSnapshot.exists() && (documentSnapshot.get("is_biz")==true)) {
                business = true
                bottomnav!!.inflateMenu(R.menu.menu_biz)
                bottomnav!!.selectedItemId = R.id.biz_appointments
                floatingSearchView!!.visibility=View.GONE
                Log.e("business", "true")

            }else {
                business = false
                bottomnav!!.inflateMenu(R.menu.menu_user)

                Log.e("business", "false")
                bottomnav!!.selectedItemId = R.id.salons
                supportActionBar!!.hide()

            }
        }
    }

}
