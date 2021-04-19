package br.com.PDM.ui.fragment.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.PDM.R
import br.com.PDM.databinding.FragmentOnBoardingFirstBinding
import br.com.PDM.databinding.FragmentOnBoardingSecondBinding


class OnBoardingSecondFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingSecondBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOnBoardingSecondBinding.inflate(inflater,  container, false)

        binding.fragment = this
        binding.lifecycleOwner = this

        // Inflate the layout for this fragment
        return binding.root
    }

}