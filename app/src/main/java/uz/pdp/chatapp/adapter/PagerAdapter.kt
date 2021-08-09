package uz.pdp.chatapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.pdp.chatapp.fragment.GruhFragment
import uz.pdp.chatapp.fragment.UsersFragment

class PagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return UsersFragment()
        }
        return GruhFragment()
    }
}