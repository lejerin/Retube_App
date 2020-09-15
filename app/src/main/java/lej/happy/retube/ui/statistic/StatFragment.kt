package lej.happy.retube.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import io.realm.RealmResults
import lej.happy.retube.R
import lej.happy.retube.data.models.ChannelStat
import lej.happy.retube.data.models.Realm.RealmSearch
import lej.happy.retube.data.models.Realm.ViewVideo
import lej.happy.retube.data.network.YoutubeApi
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.databinding.FragmentStatBinding


class StatFragment : Fragment() {

    private lateinit var factory: StatViewModelFactory
    private lateinit var viewModel: StatViewModel

    private lateinit var binding: FragmentStatBinding

    var searches: RealmResults<RealmSearch>? = null
    var viewVideos: RealmResults<ViewVideo>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stat, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api = YoutubeApi()
        val repository =
            YoutubeRepository(api)
        factory = StatViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(StatViewModel::class.java)

        binding.statViewModel = viewModel

        viewModel.getChannelData(getString(R.string.api_key))
        viewModel.chennels.observe(viewLifecycleOwner, Observer { channel ->

            initViewPager()
            binding.channelViewPager.adapter = LikeChannelVPAdapter(channel)

        })






    }





    fun initViewPager(){
        binding.channelViewPager.clipToPadding = false
        binding.channelViewPager.setPadding(40, 0, 40, 0)
        binding.channelViewPager.setPageMargin(resources.displayMetrics.widthPixels / -9)
        binding.indicator.createDotPanel(
            3,
            R.drawable.indicator_dot_off,
            R.drawable.indicator_dot_on,
            0
        )

        binding.channelViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.indicator.selectDot(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


    }
}