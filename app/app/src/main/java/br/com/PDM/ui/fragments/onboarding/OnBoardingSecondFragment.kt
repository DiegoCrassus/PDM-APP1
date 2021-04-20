package br.com.PDM.ui.fragments.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.PDM.R
import br.com.PDM.databinding.FragmentOnBoardingSecondBinding

class OnBoardingSecondFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingSecondBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding_second, container, false)
    }
}