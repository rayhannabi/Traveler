package com.example.rayhan.traveler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Animatable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.transition.Transition
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.example.rayhan.traveler.places.Place
import com.example.rayhan.traveler.places.PlaceData
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_PARAM_ID = "place_id"

        fun newIntent(context: Context, position: Int): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_PARAM_ID, position)
            return intent
        }
    }

    private lateinit var inputManager: InputMethodManager
    private lateinit var place: Place
    private lateinit var todoList: ArrayList<String>
    private lateinit var todoAdapter: ArrayAdapter<*>

    private var isEditTextVisible: Boolean = false
    private var defaultColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupValues()
        setupAdapter()
        loadPlace()
        windowTransition()
        getPhoto()
    }

    private fun setupValues() {
        place = PlaceData.getPlaces()[intent.getIntExtra(EXTRA_PARAM_ID, 0)]
        addButton.setOnClickListener(this)
        defaultColor = ContextCompat.getColor(this, R.color.primary_dark)
        inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        revealView.visibility = View.INVISIBLE
        isEditTextVisible = false
    }

    private fun setupAdapter() {
        todoList = ArrayList()
        todoAdapter = ArrayAdapter(this, R.layout.row_todo, todoList)
        activitiesList.adapter = todoAdapter
    }

    private fun loadPlace() {
        placeTitle.text = place.name
        placeImage.setImageResource(place.getImageResourceId(this))
    }

    private fun windowTransition() {
        window.enterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(p0: Transition?) {
                addButton.animate().alpha(1.0f)
                window.enterTransition.removeListener(this)
            }

            override fun onTransitionResume(p0: Transition?) {}
            override fun onTransitionPause(p0: Transition?) {}
            override fun onTransitionCancel(p0: Transition?) {}
            override fun onTransitionStart(p0: Transition?) {}
        })
    }

    private fun getPhoto() {
        val photo = BitmapFactory.decodeResource(resources, place.getImageResourceId(this))
        colorize(photo)
    }

    private fun colorize(photo: Bitmap) {
        val palette = Palette.from(photo).generate()
        applyPalette(palette)
    }

    private fun applyPalette(palette: Palette) {
        window.setBackgroundDrawable(ColorDrawable(palette.getDarkMutedColor(defaultColor)))
        placeNameHolder.setBackgroundColor(palette.getMutedColor(defaultColor))
        revealView.setBackgroundColor(palette.getLightVibrantColor(defaultColor))
        revealView.alpha = 0.95f
    }

    override fun onBackPressed() {
        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.duration = 100
        addButton.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                addButton.visibility = View.GONE
                finishAfterTransition()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.addButton -> if (!isEditTextVisible) {
                revealEditText(revealView)
                todoText.requestFocus()
                inputManager.showSoftInput(todoText, InputMethodManager.SHOW_IMPLICIT)

                addButton.setImageResource(R.drawable.ic_morph)
                val animate = addButton.drawable as Animatable
                animate.start()
            } else {
                addToDo(todoText.text.toString())
                todoAdapter.notifyDataSetChanged()
                inputManager.hideSoftInputFromWindow(todoText.windowToken, 0)
                hideEditText(revealView)

                addButton.setImageResource(R.drawable.ic_morph_inverse)
                val animatable = addButton.drawable as Animatable
                animatable.start()
            }
        }
    }

    private fun addToDo(string: String) {
        todoList.add(string)
    }

    private fun hideEditText(revealView: LinearLayout?) {
        val centerX = revealView!!.right - 30
        val centerY = revealView.bottom - 60
        val initialRadius = revealView.width

        val animation = ViewAnimationUtils.createCircularReveal(revealView, centerX, centerY,
                initialRadius.toFloat(),
                0f)

        animation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                revealView.visibility = View.INVISIBLE
            }
        })

        isEditTextVisible = false
        animation.start()
    }

    private fun revealEditText(revealView: LinearLayout?) {
        val centerX = revealView!!.right - 30
        val centerY = revealView!!.bottom - 60
        val finalRadius = Math.max(centerX, centerY)

        val animation = ViewAnimationUtils.createCircularReveal(revealView, centerX, centerY,
                0f, finalRadius.toFloat())
        revealView.visibility = View.VISIBLE
        isEditTextVisible = true

        animation.start()
    }
}
