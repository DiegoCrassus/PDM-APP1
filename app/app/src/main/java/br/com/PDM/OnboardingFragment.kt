package br.com.PDM.onboard.janelas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import br.com.PDM.databinding.FragmentOnboardingBinding
//import androidx.lifecycle.Lifecycle
import br.com.PDM.databinding.FragmentOnboardingBinding.inflate
import br.com.PDM.databinding.FragmentOnboardingFirstBinding.inflate
import br.com.PDM.databinding.FragmentOnboardingSecondBinding.inflate


//FragmentOnboardingBinding era p estar esse n o firts
class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val listaFragmentos = listOf(
                OnboardingFirstFragment(),
                OnboardingSecondFragment()
                )

        val adaptador = AdaptadorParaConversarComVp(listaFragmentos, requireActivity().supportFragmentManager,
        lifecycle)

        binding.vpOnboarding.adapter = adaptador
        return binding.root
    }
    fun start(){

    }
}

class AdaptadorParaConversarComVp(
        val listaFragmentos: List<Fragment>,
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle){
            override fun getItemCount() =  listaFragmentos.Size

            override fun createFragment(position: Int): Fragment{
                return listaFragmentos[position]
        }

}