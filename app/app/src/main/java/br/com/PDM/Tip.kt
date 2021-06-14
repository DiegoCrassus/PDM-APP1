package br.com.PDM

import androidx.annotation.DrawableRes

data class Tip(
    val title: String,
    val subtitle: String,
    @DrawableRes val logo: Int,
    @DrawableRes val background: Int

)