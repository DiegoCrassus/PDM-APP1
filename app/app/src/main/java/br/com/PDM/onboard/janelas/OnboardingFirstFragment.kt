package br.com.PDM.onboard.janelas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.PDM.R
import br.com.PDM.databinding.FragmentOnboardingFirstBinding

class OnboardingFirstFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingFirstBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }
}