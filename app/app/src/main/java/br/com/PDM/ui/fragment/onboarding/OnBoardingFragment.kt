package br.com.PDM.ui.fragment.onboarding

import android.os.Bundle
import android.support.wearable.activity.WearableActivity

class OnBoardingFragment : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding_fragment)

        // Enables Always-on
        setAmbientEnabled()
    }
}