package com.brustoloni.taxi.presentation.map.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.brustoloni.taxi.R
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

val TAG: String = DetailFragment::class.java.name

class DetailFragment: Fragment(){
    private lateinit var binding: com.brustoloni.taxi.databinding.FragmentDetailBinding
    private val viewModel: DetailsViewModel by viewModel()
    private var mPoi: Poi? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { bundle ->
            mPoi = bundle.getParcelable(Constants.EXTRA_POI_LIST_VEHICLES)
        }


        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    override fun onResume() {
        super.onResume()
        viewModel.start(binding.mapDetails, mPoi)
    }
}