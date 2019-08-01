package io.github.maksymilianrozanski.medicinesbox

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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