package com.example.rayhan.traveler

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    private lateinit var staggeredLayout: StaggeredGridLayoutManager
    private lateinit var travelListAdapter: TravelListAdapter

    private var isListView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isListView = true

        staggeredLayout = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredLayout

        travelListAdapter = TravelListAdapter(this)
        recyclerView.adapter = travelListAdapter

        val clickListener = object : TravelListAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {

                val intent = DetailActivity.newIntent(this@MainActivity, position)

                val placeImage: ImageView = view.findViewById(R.id.placeImage)
                val placeNameHolder: LinearLayout = view.findViewById(R.id.placeNameHolder)

                val imagePair = Pair.create(placeImage as View, "tImage")
                val holderPair = Pair.create(placeNameHolder as View, "tNameHolder")

                val navigationBar: View = findViewById(android.R.id.navigationBarBackground)
                val statusBar: View = findViewById(android.R.id.statusBarBackground)

                val navBarPair = Pair.create(navigationBar, Window
                        .NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
                val statusBarPair = Pair.create(statusBar, Window
                        .STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                var toolBarPair = Pair.create(toolbar as View, "tActionBar")

                val pairs = mutableListOf(imagePair, holderPair, statusBarPair, toolBarPair)
                if (navigationBar != null && navBarPair != null) {
                    pairs += navBarPair
                }

                val options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this@MainActivity, *pairs.toTypedArray())
                ActivityCompat.startActivity(this@MainActivity, intent, options.toBundle())
            }
        }

        travelListAdapter.setOnClickListener(clickListener)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.elevation = 7.0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        this.menu = menu!!

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_toggle) {
            toggle()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggle() {
        if (isListView) {
            showGridView()
        } else {
            showListView()
        }
    }

    private fun showListView() {
        staggeredLayout.spanCount = 1

        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_grid_white)
        item.title = getString(R.string.show_as_grid)
        isListView = true
    }

    private fun showGridView() {
        staggeredLayout.spanCount = 2

        val item = menu.findItem(R.id.action_toggle)
        item.setIcon(R.drawable.ic_action_list_white)
        item.title = getString(R.string.show_as_list)
        isListView = false
    }
}
