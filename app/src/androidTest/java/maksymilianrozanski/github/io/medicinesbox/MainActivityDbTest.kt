package maksymilianrozanski.github.io.medicinesbox

import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.matcher.IntentMatchers.toPackage
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.test.mock.MockContext
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.data.TestMedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.model.KEY_ID
import maksymilianrozanski.github.io.medicinesbox.model.Medicine
import maksymilianrozanski.github.io.medicinesbox.module.AppModule
import maksymilianrozanski.github.io.medicinesbox.module.ContextModule
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule
import org.hamcrest.Description
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainActivityDbTest {

    @Rule
    @JvmField
    var activityRule: IntentsTestRule<MainActivity> = IntentsTestRule(MainActivity::class.java, true, false)

    private lateinit var testAppComponent: TestAppComponent

    private lateinit var mockedContext: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val app = InstrumentationRegistry.getInstrumentation()
                .targetContext.applicationContext as MyApp

        mockedContext = Mockito.mock(MockContext::class.java)

        var myTestDbModule: DatabaseModule = MyTestDbModule(mockedContext)

        testAppComponent = DaggerTestAppComponent.builder()
                .contextModule(ContextModule(app))
                .appModule(AppModule(app))
                .databaseModule(myTestDbModule)
                .build()

        app.appComponent = testAppComponent
        testAppComponent.inject(this)
    }

    @Ignore
    class MyTestDbModule(var mockedContext: Context) : DatabaseModule() {
        override fun medicinesDatabaseHandler(context: Context): MedicinesDatabaseHandler {
            return TestMedicinesDatabaseHandler(mockedContext)
        }
    }

    @Test
    fun displayingNameTest() {
        activityRule.launchActivity(null)
        onView(withText("Paracetamol")).check(matches(isDisplayed()))
    }

    @Test
    fun sendingEditIntentTest() {
        var medicine = TestMedicinesDatabaseHandler(mockedContext).readMedicines()[0]

        activityRule.launchActivity(null)
        onView(withText("Paracetamol")).check(matches(isDisplayed()))
        onView(withId(R.id.editButton)).perform(click())
        stubAllIntents()
        intended(allOf(hasComponent(hasClassName(AddEditActivity::class.java.name))
                , toPackage("maksymilianrozanski.github.io.medicinesbox")
                , object : TypeSafeMatcher<Intent>() {
            override fun matchesSafely(intent: Intent): Boolean {
                var medicineFromIntent = intent.getParcelableExtra<Medicine>(KEY_ID)
                return (medicine.id == medicineFromIntent.id
                        && medicine.name == medicineFromIntent.name
                        && medicine.dailyUsage == medicineFromIntent.dailyUsage
                        && medicine.savedTime == medicineFromIntent.savedTime
                        && medicine.quantity == medicineFromIntent.quantity)
            }

            override fun describeTo(description: Description?) {
            }
        }
        ))
    }
}