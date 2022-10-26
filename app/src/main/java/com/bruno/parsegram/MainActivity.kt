package com.bruno.parsegram
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bruno.parsegram.Fragments.MainActivity.ComposeFragment
import com.bruno.parsegram.Fragments.MainActivity.FeedFragment
import com.bruno.parsegram.Fragments.MainActivity.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseUser
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if(supportActionBar!=null){
            supportActionBar!!.setLogo(R.drawable.insta_logo)
            supportActionBar?.title = ""
        }
        val fragmentManager: FragmentManager = supportFragmentManager
        findViewById<BottomNavigationView>(R.id.main_nav_bar).setOnItemSelectedListener {
                item->
            var fragmentToShow:Fragment?=null
            when(item.itemId){
                R.id.action_home->{
                    fragmentToShow = FeedFragment()
                }
                R.id.action_compose->{
                    fragmentToShow = ComposeFragment()
                }
                R.id.action_profile->{
                    fragmentToShow = ProfileFragment()
                }
            }
            if(fragmentToShow!=null){
                fragmentManager.beginTransaction().replace(R.id.mainLayoutContainer,fragmentToShow).commit()
            }
            true
        }
        findViewById<BottomNavigationView>(R.id.main_nav_bar).selectedItemId=R.id.action_home
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout->{
                ParseUser.logOut()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
                Log.i(TAG,"LOGGED OUT")
            }
        }
        return true
    }
    companion object{
        const val TAG = "MainActivity"
    }

}