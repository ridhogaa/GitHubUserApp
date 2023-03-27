package com.ergea.githubuserapp.ui.home

import android.view.KeyEvent
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.ergea.githubuserapp.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {

    private lateinit var scenario: FragmentScenario<HomeFragment>
    private val query = "ridhogaa"

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_GitHubUserApp)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun changeDarkMode() {
        onView(withId(R.id.ic_btn_setting)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.ic_btn_setting)).perform(click())

        onView(withId(R.id.switch_theme)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.switch_theme)).perform(click())
    }

    @Test
    fun onSearchQuery() {
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(
            typeText(query),
            pressKey(KeyEvent.KEYCODE_ENTER)
        )
    }
}
