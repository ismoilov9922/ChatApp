package uz.pdp.chatapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import uz.pdp.chatapp.R
import uz.pdp.chatapp.adapter.PagerAdapter
import uz.pdp.chatapp.adapter.TabData
import uz.pdp.chatapp.databinding.FragmentPagerBinding
import uz.pdp.chatapp.databinding.ItemTabBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PagerFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentPagerBinding
    lateinit var pagerAdapter: PagerAdapter

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPagerBinding.inflate(layoutInflater)
        pagerAdapter = activity?.let { PagerAdapter(it) }!!
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tablayout, binding.viewPager) { tab, possition ->
            val tabBinding: ItemTabBinding =
                ItemTabBinding.inflate(layoutInflater)
            tab.customView = tabBinding.root
            if (possition == 0) {
                tabBinding.tv.text = "Person"
                tabBinding.image.setImageResource(R.drawable.ic_user)
            }
            if (possition == 1) {
                tabBinding.tv.text = "Group"
                tabBinding.image.setImageResource(R.drawable.ic_group)
            }

        }.attach()
        return binding.root
    }
}