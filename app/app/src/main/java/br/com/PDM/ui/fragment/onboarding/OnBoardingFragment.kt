package br.com.PDM.ui.fragment.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.PDM.R
import br.com.PDM.databinding.FragmentOnBoardingBinding
import br.com.PDM.databinding.FragmentOnBoardingFirstBinding
import br.com.PDM.ui.fragment.onboarding.screens.OnBoardingFirstFragment
import br.com.PDM.ui.fragment.onboarding.screens.OnBoardingSecondFragment

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOnBoardingBinding.inflate(inflater,  container, false)
        binding.lifecycleOwner = this

        val listFragments = listOf(OnBoardingFirstFragment(), OnBoardingSecondFragment())
        // estamos instanciando o fragmento em memoria, pronto para aresentar

        val adapter = AdapterToComunicateWithVP(listFragments, requireActivity().supportFragmentManager, lifecycle)

        binding.vpOnBoarding.adapter = adapter

        binding.fragment = this

        // Inflate the layout for this fragment
        return binding.root
    }

}

class AdapterToComunicateWithVP(
    val listFragments: List<fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount() = listFragments.size

    override fun createFragment(position: Int) = listFragments[position]

}