package maksymilianrozanski.github.io.medicinesbox

import android.content.Context
import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.mock.MockContext
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.data.TestTimeProvider
import maksymilianrozanski.github.io.medicinesbox.data.TimeProvider
import maksymilianrozanski.github.io.medicinesbox.model.KEY_ID
import maksymilianrozanski.github.io.medicinesbox.model.Medicine
import maksymilianrozanski.github.io.medicinesbox.module.AppModule
import maksymilianrozanski.github.io.medicinesbox.module.ContextModule
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule
import maksymilianrozanski.github.io.medicinesbox.module.TimeProviderModule
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@RunWith(AndroidJUnit4::class)
class AddEditActivitySavingTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<AddEditActivity> = ActivityTestRule(AddEditActivity::class.java, true, false)

    private lateinit var testAppComponent: TestAppComponent
    private lateinit var mockedContext: Context
    private val stoppedTime: Long = 1528819101551L //12.06.2018, 15:58

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val app = InstrumentationRegistry.getInstrumentation()
                .targetContext.applicationContext as MyApp

        mockedContext = Mockito.mock(MockContext::class.java)

        testAppComponent = DaggerTestAppComponent.builder()
                .contextModule(ContextModule(app))
                .appModule(AppModule(app))
                .databaseModule(MyTestDbModule())
                .timeProviderModule(TestTimeProviderModule(stoppedTime))
                .build()

        app.appComponent = testAppComponent
        testAppComponent.inject(this)
    }

    @Ignore
    class MyTestDbModule() : DatabaseModule() {
        override fun medicinesDatabaseHandler(context: Context?): MedicinesDatabaseHandler {
            return Mockito.mock(MedicinesDatabaseHandler::class.java)
        }
    }

    @Ignore
    class TestTimeProviderModule(var time: Long) : TimeProviderModule() {
        override fun provideClock(): TimeProvider {
            return TestTimeProvider(time)
        }
    }

    private class MedicineMatcher(var expectedMedicine: Medicine) : ArgumentMatcher<Medicine> {
        override fun matches(argument: Medicine): Boolean {
            return argument.id == expectedMedicine.id
                    && argument.name.equals(expectedMedicine.name)
                    && argument.quantity == expectedMedicine.quantity
                    && argument.dailyUsage == expectedMedicine.dailyUsage
                    && argument.savedTime == expectedMedicine.savedTime
        }

        override fun toString(): String {
            return "Inside MedicineMatcher's toString"
        }
    }

    private fun hasMedicine(medicine: Medicine): MedicineMatcher {
        return MedicineMatcher(medicine)
    }

    private fun <T> myArgThat(matcher: ArgumentMatcher<T>): T {
        Mockito.argThat(matcher)
        return null as T
    }

    @Test
    fun savingNewMedicineTest() {
        activityRule.launchActivity(null)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("")))

        onView(withId(R.id.medicineNameEditText)).perform(typeText("Paracetamol"))
        onView(withId(R.id.medicineQuantityEditText)).perform(typeText("13"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(typeText("2"))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("13"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.saveButton)).perform(click())

        var mockedMedicinesDatabaseHandler = activityRule.activity.databaseHandler

        var expectedMedicine = Medicine()
        expectedMedicine.name = "Paracetamol"
        expectedMedicine.quantity = 13.0
        expectedMedicine.dailyUsage = 2.0
        expectedMedicine.savedTime = stoppedTime

        verify(mockedMedicinesDatabaseHandler).createMedicine(myArgThat(hasMedicine(expectedMedicine)))
    }

    @Test
    fun savingEditedMedicineTest() {
        var medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        var launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("15"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.medicineNameEditText)).perform(replaceText("Acetaminophen"))
        onView(withId(R.id.medicineQuantityEditText)).perform(replaceText("13"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(replaceText("1"))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Acetaminophen")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("13"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))

        onView(withId(R.id.saveButton)).perform(click())

        var mockedMedicinesDatabaseHandler = activityRule.activity.databaseHandler
        var expectedMedicine = Medicine()
        expectedMedicine.id = 5
        expectedMedicine.name = "Acetaminophen"
        expectedMedicine.quantity = 13.0
        expectedMedicine.dailyUsage = 1.0
        expectedMedicine.savedTime = stoppedTime

        verify(mockedMedicinesDatabaseHandler).updateMedicine(myArgThat(hasMedicine(expectedMedicine)))
    }
}
