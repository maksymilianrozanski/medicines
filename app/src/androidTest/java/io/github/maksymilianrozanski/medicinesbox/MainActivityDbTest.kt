package io.github.maksymilianrozanski.medicinesbox


import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.data.TestMedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.model.KEY_ID
import io.github.maksymilianrozanski.medicinesbox.model.Medicine
import io.github.maksymilianrozanski.medicinesbox.module.AppModule
import io.github.maksymilianrozanski.medicinesbox.module.ContextModule
import io.github.maksymilianrozanski.medicinesbox.module.DatabaseModule
import org.hamcrest.Description
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations


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

        mockedContext = app.applicationContext

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

    @Ignore
    class MyTestEmptyDbModule(var mockedContext: Context) : DatabaseModule() {
        override fun medicinesDatabaseHandler(context: Context?): MedicinesDatabaseHandler {
            var emptyDb = TestMedicinesDatabaseHandler(mockedContext)
            emptyDb.deleteMedicine(0)
            return emptyDb
        }
    }

    @Test
    fun displayingNameTest() {
        activityRule.launchActivity(null)
        onView(withId(R.id.medicineName)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.expectedQuantity)).check(matches(withText(containsString("Expected quantity today: "))))
        onView(withId(R.id.medicineDailyUsage)).check(matches(withText(containsString("Daily usage: 2"))))
        onView(withId(R.id.enoughUntil)).check(matches(withText("Enough until: May 30, 2018")))
        onView(withId(R.id.emptyView)).check(matches(not(isDisplayed())))
    }

    @Test
    fun displayingEmptyViewTest() {
        val app = InstrumentationRegistry.getInstrumentation()
                .targetContext.applicationContext as MyApp
        var myTestEmptyDbModule: DatabaseModule = MyTestEmptyDbModule(mockedContext)
        testAppComponent = DaggerTestAppComponent.builder()
                .contextModule(ContextModule(app))
                .appModule(AppModule(app))
                .databaseModule(myTestEmptyDbModule)
                .build()

        app.appComponent = testAppComponent
        testAppComponent.inject(this)

        activityRule.launchActivity(null)
        onView(withId(R.id.emptyView)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerViewId)).check(matches(not(isDisplayed())))
    }

    @Test
    fun sendingEditIntentTest() {
        var medicine = TestMedicinesDatabaseHandler(mockedContext).readMedicines()[0]

        activityRule.launchActivity(null)
        onView(withText("Paracetamol")).check(matches(isDisplayed()))
        onView(withId(R.id.editButton)).perform(click())
        stubAllIntents()
        intended(allOf(hasComponent(hasClassName(AddEditActivity::class.java.name))
                , toPackage("io.github.maksymilianrozanski.medicinesbox")
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

    @Test
    fun deletionConfirmationDialogTest() {
        activityRule.launchActivity(null)
        onView(withId(R.id.deleteButton)).perform(click())
        onView(withText(containsString("Are you sure you want to delete"))).check(matches(isDisplayed()))
    }

    @Test
    fun calendarIntentTest() {
        activityRule.launchActivity(null)
        onView(withId(R.id.calendarIntentButton)).check(matches(isDisplayed()))
        val medicine = testAppComponent.getDatabaseHandler().readMedicines()[0]
        stubAllIntents()
        onView(withId(R.id.calendarIntentButton)).perform(click())
        intended(allOf(hasAction(Intent.ACTION_INSERT)
                , hasExtra(CalendarContract.Events.TITLE
                , "${activityRule.activity.applicationContext.getString(R.string.last_day_of)} Paracetamol ${activityRule.activity.applicationContext.getString((R.string.supply))}.")
                , hasExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                , hasExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, medicine.enoughUntil())))
    }
}