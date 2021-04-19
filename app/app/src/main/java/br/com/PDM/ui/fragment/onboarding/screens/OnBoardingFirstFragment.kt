package br.com.PDM.ui.fragment.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import br.com.PDM.databinding.FragmentOnBoardingFirstBinding


class OnBoardingFirstFragment : Fragment() {

    private lateinit var binding : FragmentOnBoardingFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // diz qual é o xml q representa o xml da parte visual desse codigo

        binding = FragmentOnBoardingFirstBinding.inflate(inflater, container, false)

        binding.fragment = this
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
         return binding.root

    }


}