package io.github.maksymilianrozanski.medicinesbox

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java, true, true)

    @Before
    fun setup() {
        stubAllIntents()
    }

    @Test
    fun sendingIntentTest() {
        activityRule.activity.runNewItemActivity()
        intended(allOf(hasComponent(hasClassName(AddEditActivity::class.java.name)), toPackage("io.github.maksymilianrozanski.medicinesbox")))
    }

    @Test
    fun displayingMenuItemTest() {
        onView(ViewMatchers.withId(R.id.addNewItem)).check(matches(isDisplayed()))
    }

    @Test
    fun sendingIntentFromMenuTest() {
        onView(ViewMatchers.withId(R.id.addNewItem)).perform(click())
        intended(allOf(hasComponent(hasClassName(AddEditActivity::class.java.name)), toPackage("io.github.maksymilianrozanski.medicinesbox")))
    }

    private fun stubAllIntents() {
        intending(not<Intent>(isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
}