package lej.happy.retube.ui.statistic

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.github.mikephil.charting.animation.Easing
import lej.happy.retube.R
import lej.happy.retube.data.network.YoutubeApi
import lej.happy.retube.data.repositories.YoutubeRepository
import lej.happy.retube.databinding.FragmentStatBinding


class StatFragment : Fragment() {

    private lateinit var factory: StatViewModelFactory
    private lateinit var viewModel: StatViewModel

    private lateinit var binding: FragmentStatBinding

    var isPieChartShow = false

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

        //사용자 데이터
        viewModel.getUserData()
        viewModel.userData.observe(viewLifecycleOwner, Observer { user ->

            when (user.time_text) {
                "새벽" -> user.time_img = R.drawable.dawn
                "오전" -> user.time_img = R.drawable.am
                "오후" -> user.time_img = R.drawable.pm
                "밤" -> user.time_img = R.drawable.night
            }

            when (user.day_text) {
                "주중" -> user.day_img = R.drawable.week
                "주말" -> user.day_img = R.drawable.holy
            }

            binding.user = user

        })

        //선호 채널
        viewModel.getChannelData(getString(R.string.api_key))
        viewModel.chennels.observe(viewLifecycleOwner, Observer { channel ->

            initViewPager()
            binding.channelViewPager.adapter = LikeChannelVPAdapter(channel)

        })


        //선호 카테고리
        initPieChart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView2.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if (checkPieChartShow()) {
                    if (!isPieChartShow) {
                        binding.piechart.animateY(1000, Easing.EaseInOutCubic) //애니메이션
                    }
                    isPieChartShow = true
                } else {
                    isPieChartShow = false
                }
            })
        }

        //선호 검색 키워드
        binding.searchTag = viewModel.getKeyWordSearch(0)
        binding.radioGroup1.setOnCheckedChangeListener { group, checkedId ->

            binding.searchTag = viewModel.getKeyWordSearch(checkedId)
        }

        binding.viewTag = viewModel.getKeyWordView(0)
        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->

            binding.viewTag = viewModel.getKeyWordView(checkedId)
        }


    }

    private fun checkPieChartShow(): Boolean {
        val scrollBounds = Rect()
        binding.scrollView2.getHitRect(scrollBounds)
        return binding.piechart.getLocalVisibleRect(scrollBounds)
    }


    private fun initPieChart(){

        binding.piechart.isRotationEnabled = false
        binding.piechart.setUsePercentValues(true)
        binding.piechart.dragDecelerationFrictionCoef = 0.95f
        binding.piechart.isDrawHoleEnabled = true
        binding.piechart.setHoleColor(Color.TRANSPARENT)
        binding.piechart.transparentCircleRadius = 50f
        binding.piechart.holeRadius = 40f

        binding.piechart.data = viewModel.getPieChartData()
        binding.piechart.setEntryLabelColor(R.color.colorPrimary)
        binding.piechart.legend.isEnabled = false
        binding.piechart.description.isEnabled = false

    }


    private fun initViewPager(){
        binding.channelViewPager.clipToPadding = false
        binding.channelViewPager.setPadding(40, 0, 40, 0)
        binding.channelViewPager.pageMargin = resources.displayMetrics.widthPixels / -9
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