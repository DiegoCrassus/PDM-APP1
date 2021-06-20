package br.com.pdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import br.com.pdm.R
import kotlinx.android.synthetic.main.activity_main_carrossel.*
import kotlinx.android.synthetic.main.tip_content.view.*
import java.lang.Math.abs

@Suppress("DEPRECATION")
class MainActivityCarrossel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        setContentView(R.layout.activity_main_carrossel)
//        setContentView(new SampleView(this));


        val tips = arrayOf(
            Tip("1 - Aepnas um texto qualquer para testar ",
                "2 - is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it",
                R.drawable.logo_ok, R.drawable.syd3),

            Tip("2 - Aepnas um texto qualquer para testar ",
                "2 - is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it",
                R.drawable.logo_ok, R.drawable.syd1),

            Tip("3 - Aepnas um texto qualquer para testar ",
                "3 - is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it",
                R.drawable.logo_ok, R.drawable.syd2)

        )

        addDots(tips.size)

        view_pager.adapter = OnboardingAdapter(tips)

        view_pager.setPageTransformer(true){ page, position ->
            page.alpha = 1 - abs(position)
            page.translationX = -position * page.width
        }


        view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                addDots(tips.size, position)
            }
        })

        next.setOnClickListener{
            if (view_pager.currentItem == view_pager.size)
                Toast.makeText(this, "Colocar a actityvt sei la", Toast.LENGTH_LONG).show()
            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
        }

        back.setOnClickListener{
            if (view_pager.currentItem == view_pager.size)
                Toast.makeText(this, "Colocar a actityvt para voltar", Toast.LENGTH_LONG).show()
            view_pager.setCurrentItem(view_pager.currentItem - 1, true)
        }
    }

    private fun addDots(size: Int, position: Int = 0){
        dots.removeAllViews()
        Array(size){
            val textView = TextView(baseContext).apply {
                text = getText(R.string.dotted)
                textSize = 35f
                setTextColor(
                    if (position == it) ContextCompat.getColor(baseContext, android.R.color.white)
                    else ContextCompat.getColor(baseContext, android.R.color.darker_gray)
                )

            }
            dots.addView(textView)
        }
    }

    private inner class OnboardingAdapter (val tips: Array<Tip>) : PagerAdapter(){

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = layoutInflater.inflate(R.layout.tip_content, container, false)

            with(tips[position]){
                view.tip_title.text = title
                view.tip_subtitle.text = subtitle
                view.tip_logo.setImageResource(logo)
                view.background = ContextCompat.getDrawable(this@MainActivityCarrossel, background)

            }

            container.addView(view)

            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean{
            return view == `object`
        }

        override fun getCount(): Int = tips.size

    }
}