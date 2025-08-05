package com.sample.weatherappmodule

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CurrentWeatherFragment())
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val frag = when (item.itemId) {
            R.id.menu_current -> CurrentWeatherFragment()
            R.id.menu_about -> AboutFragment()
            else -> return super.onOptionsItemSelected(item)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, frag)
            .commit()
        return true
    }
}
