package br.com.PDM

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.PDM.databinding.FragmentOnboardingBinding
import br.com.PDM.onboard.janelas.AdaptadorParaConversarComVp
import br.com.PDM.onboard.janelas.OnboardingFirstFragment
import br.com.PDM.onboard.janelas.OnboardingSecondFragment

import kotlinx.android.synthetic.main.activity_onboard.*


class OnboardActivity : AppCompatActivity() {
//    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

//        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
//        binding.lifecycleOwner = this
        val listaFragmentos = listOf(
            OnboardingFirstFragment(),
            OnboardingSecondFragment()
        )

        val adaptador = Adaptador(listaFragmentos, supportFragmentManager, lifecycle)

//        binding.vpOnboarding.adapter = adaptador
//        return binding.root
        vpOnboarding.adapter = adaptador
    }

}

class Adaptador(val listaFragmentos: List<Fragment>, fragmentManager: FragmentManager,
                                    lifecycle: Lifecycle) :

    FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount() =  listaFragmentos.size

    override fun createFragment(position: Int): Fragment {
        return listaFragmentos[position]
    }

}