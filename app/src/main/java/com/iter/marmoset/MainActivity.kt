package com.iter.marmoset

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.iter.marmoset.dummy.DummyContent

// sign in response id and providers used with AuthUI
private const val RC_SIGN_IN = 420
private val providers = mutableListOf(
        AuthUI.IdpConfig.EmailBuilder().build())

class MainActivity : AppCompatActivity(), SalonFragment.OnListFragmentInteractionListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // user already signed in
            val salonFragment: Fragment = SalonFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.content_view, salonFragment).commit()

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // Successfully signed
                val vaccineFragment: Fragment = SalonFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.content_view, vaccineFragment).commit()
            } else {
                // TODO: handle sign in failed
            }
        }

    }

    override fun onListFragmentInteraction(salon: Salon) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater : MenuInflater = getMenuInflater()
//        inflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when(item!!.itemId){
//            R.id.add -> {
//                val newAdder = Intent(this, AddVaccination::class.java)
//                startActivityForResult(newAdder, 5)
//            }
//            else -> {
//                return true
//            }
//
//        }
        return super.onOptionsItemSelected(item)
    }
}
