package com.elifnuroksuz.busonolsun.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.elifnuroksuz.busonolsun.R
import com.elifnuroksuz.busonolsun.adapter.ProductAdapter
import com.elifnuroksuz.busonolsun.databinding.FragmentHomeBinding
import com.elifnuroksuz.busonolsun.util.ApplicationViewModelFactory
import com.elifnuroksuz.busonolsun.viewmodel.MainViewModel
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView


class HomeFragment : Fragment() {



   private val viewModel : MainViewModel by viewModels {
       ApplicationViewModelFactory(requireActivity().application)
   }

    lateinit var bannerView: BannerView
    private val TAG = "HUAWEI _ ADS"


    private lateinit var binding : FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, com.elifnuroksuz.busonolsun.R.layout.fragment_home,container, false)
        loadBannerAd()
        binding.countryRV.layoutManager = LinearLayoutManager(requireContext())


        viewModel.getDataFromAPI()
        setObservers()
        return binding.root
    }



    @SuppressLint("SuspiciousIndentation")
    private fun setObservers(){
        viewModel.countryData.observe(viewLifecycleOwner) { list ->
            val  countryAdapter = ProductAdapter(arrayListOf()){position ->
            //findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(list[position]))
            }
            binding.countryRV.adapter = countryAdapter

                countryAdapter.updateList(list)
            ////////////////////////////////
            viewModel.insertAll(list)
        }





        viewModel.countryLoad.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.loadingPB.visibility = View.VISIBLE
            } else {
                binding.loadingPB.visibility = View.GONE
            }
        }
        viewModel.countryError.observe(viewLifecycleOwner) { error ->
            if (error) {
                binding.errorTV.visibility = View.VISIBLE
            } else {
                binding.errorTV.visibility = View.GONE
            }
        }

    }
    private fun loadBannerAd(){

        // Obtain BannerView.
        bannerView = binding.hwBannerView
        bannerView.adListener = getAdListener()
        // Set the ad unit ID and ad dimensions. "testw6vs28auh3" is a dedicated test ad unit ID.
        bannerView.adId = "testw6vs28auh3"
        bannerView.bannerAdSize = BannerAdSize.BANNER_SIZE_360_57
        // Set the refresh interval to 60 seconds.
        bannerView.setBannerRefresh(60)
        // Create an ad request to load an ad.
        val adParam = AdParam.Builder().build()
        bannerView.loadAd(adParam)

    }

    private fun getAdListener(): AdListener{
        val adListener: AdListener = object : AdListener() {
            override fun onAdLoaded() {
                // Called when an ad is loaded successfully.
                Log.i(TAG,"onAdLoaded")
            }
            override fun onAdFailed(errorCode: Int) {
                // Called when an ad fails to be loaded.
                Log.i(TAG,"onAdFailed")
                Log.i(TAG,errorCode.toString())
            }
            override fun onAdOpened() {
                // Called when an ad is opened.
                Log.i(TAG,"onAdOpened")
            }
            override fun onAdClicked() {
                // Called when an ad is clicked.
                Log.i(TAG,"onAdClicked")
            }
            override fun onAdLeave() {
                // Called when an ad leaves an app.
                Log.i(TAG,"onAdLeave")
            }
            override fun onAdClosed() {
                // Called when an ad is closed.
                Log.i(TAG,"onAdClosed")
            }
        }
        return adListener
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerView.destroy()
    }

}