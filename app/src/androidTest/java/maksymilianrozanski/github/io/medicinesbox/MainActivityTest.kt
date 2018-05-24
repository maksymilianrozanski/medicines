package maksymilianrozanski.github.io.medicinesbox

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule @JvmField
    var activityRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java, true, true)

    @Test
    fun sendingIntentTest() {
        stubAllIntents()
        activityRule.activity.runNewItemActivity()

        intended(allOf(hasComponent(hasClassName(AddEditActivity::class.java.name)), toPackage("maksymilianrozanski.github.io.medicinesbox")))
    }

    private fun stubAllIntents() {
        intending(not<Intent>(isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }
}