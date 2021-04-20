package br.com.PDM.ui.fragments.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.PDM.R
import br.com.PDM.databinding.FragmentOnBoardingBinding
import br.com.PDM.signIn

class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOnBoardingBinding.inflate(inflater,  container, false)
        binding.lifecycleOwner = this

        val listFragments = listOf(OnBoardingFirstFragment(), OnBoardingSecondFragment())
        // estamos instanciando o fragmento em memoria, pronto para aresentar

        val adapter = AdapterToComunicateWithVP(listFragments, requireActivity().supportFragmentManager, lifecycle)

        binding.vponboard.adapter = adapter

        //binding.Fragment = this

        // Inflate the layout for this fragment
       // return binding.root

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false)
    }


}

class AdapterToComunicateWithVP(
    val listFragments: List<Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle){

    override fun getItemCount() = listFragments.size

    override fun createFragment(position: Int) = listFragments[position]

}