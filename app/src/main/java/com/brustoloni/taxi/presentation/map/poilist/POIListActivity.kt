package com.brustoloni.taxi.presentation.map.poilist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brustoloni.taxi.R
import com.brustoloni.taxi.utils.replaceFragment

class POIListActivity : AppCompatActivity() {

    private val mFragment by lazy { POIListFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_poi_vehicles)

        if (savedInstanceState == null)
            replaceFragment(R.id.framelayout_container, mFragment, POILIST_FRAGMENT_TAG)
    }
}
