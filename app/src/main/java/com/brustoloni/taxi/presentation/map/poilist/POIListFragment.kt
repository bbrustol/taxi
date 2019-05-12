package com.brustoloni.taxi.presentation.map.poilist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brustoloni.taxi.R
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.databinding.FragmentMapPoiVehiclesBinding
import com.brustoloni.taxi.utils.Constants
import kotlinx.android.synthetic.main.fragment_map_poi_vehicles.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class POIListFragment : Fragment() {

    private val viewModel: POIListViewModel by viewModel()

    private lateinit var dataBinding: FragmentMapPoiVehiclesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_map_poi_vehicles, container, false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        dataBinding.executePendingBindings()

        initializeRecyclerView(view)
        setupListeners()

        viewModel.start(53.46036882190762,9.909716434648558,53.668806556867445,10.019908942943804)
    }

    private fun initializeRecyclerView(view: View) {
        val recycleListener = RecyclerView.RecyclerListener { holder ->
            val mapHolder = holder as POIListAdapter.POIViewHandler
            mapHolder.clearView()
        }

        dataBinding.rvMapPoiVehicles.layoutManager = LinearLayoutManager(context)
        dataBinding.rvMapPoiVehicles.setRecyclerListener(recycleListener)
        val clickAction = { poi: Poi ->
            val bundle = Bundle().apply { putParcelable(Constants.EXTRA_POI_LIST_VEHICLES, poi) }
            view.findNavController().navigate(R.id.action_map_poi_vehicles_to_detail, bundle)
        }

        dataBinding.rvMapPoiVehicles.adapter = POIListAdapter(clickAction)
    }

    private fun setupListeners() {
        viewModel.dataReceived.observe(viewLifecycleOwner, Observer {
            val poiListAdapter = rv_map_poi_vehicles.adapter as POIListAdapter
            poiListAdapter.submitList(it)
            poiListAdapter.notifyDataSetChanged()
            dataBinding.rvMapPoiVehicles.scrollToPosition(0)
        })
    }
}
