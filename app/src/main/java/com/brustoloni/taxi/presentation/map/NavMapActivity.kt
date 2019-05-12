package com.brustoloni.taxi.presentation.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.brustoloni.taxi.R

class NavMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_map)
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.nav_map_host_fragment).navigateUp()
}
