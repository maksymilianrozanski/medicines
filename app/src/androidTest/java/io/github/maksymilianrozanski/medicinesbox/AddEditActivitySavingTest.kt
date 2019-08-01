package io.github.maksymilianrozanski.medicinesbox

import android.content.Context
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.platform.app.InstrumentationRegistry
import io.github.maksymilianrozanski.medicinesbox.data.MedicinesDatabaseHandler
import io.github.maksymilianrozanski.medicinesbox.data.TestTimeProvider
import io.github.maksymilianrozanski.medicinesbox.data.TimeProvider
import io.github.maksymilianrozanski.medicinesbox.model.KEY_ID
import io.github.maksymilianrozanski.medicinesbox.model.Medicine
import io.github.maksymilianrozanski.medicinesbox.module.AppModule
import io.github.maksymilianrozanski.medicinesbox.module.ContextModule
import io.github.maksymilianrozanski.medicinesbox.module.DatabaseModule
import io.github.maksymilianrozanski.medicinesbox.module.TimeProviderModule
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

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

        mockedContext = app.applicationContext

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
    class MyTestDbModule : DatabaseModule() {
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
    fun savingNewMedicineEmptyEditTextTest1() {
        activityRule.launchActivity(null)
        onView(withId(R.id.medicineNameEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("")))

        onView(withId(R.id.medicineNameEditText)).perform(typeText("Paracetamol"))
        onView(withId(R.id.medicineQuantityEditText)).perform(typeText("13"))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("13"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(hasErrorText(containsString("cannot"))))
    }

    @Test
    fun savingNewMedicineEmptyEditTextTest2() {
        activityRule.launchActivity(null)
        onView(withId(R.id.medicineNameEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("")))

        onView(withId(R.id.medicineNameEditText)).perform(typeText("Paracetamol"))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(hasErrorText(containsString("cannot"))))
    }

    @Test
    fun savingNewMedicineEmptyEditText3() {
        activityRule.launchActivity(null)
        onView(withId(R.id.medicineNameEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("")))

        onView(withId(R.id.medicineQuantityEditText)).perform(typeText("13"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(typeText("2"))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineNameEditText)).check(matches(hasErrorText(containsString("cannot"))))
        onView(withId(R.id.medicineQuantityEditText)).perform(typeText("13"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(typeText("2"))
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
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.medicineNameEditText)).perform(replaceText("Acetaminophen"))
        onView(withId(R.id.medicineQuantityEditText)).perform(replaceText("10"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(replaceText("1"))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Acetaminophen")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))

        onView(withId(R.id.saveButton)).perform(click())

        var mockedMedicinesDatabaseHandler = activityRule.activity.databaseHandler
        var expectedMedicine = Medicine()
        expectedMedicine.id = 5
        expectedMedicine.name = "Acetaminophen"
        expectedMedicine.quantity = 10.0
        expectedMedicine.dailyUsage = 1.0
        expectedMedicine.savedTime = stoppedTime

        verify(mockedMedicinesDatabaseHandler).updateMedicine(myArgThat(hasMedicine(expectedMedicine)))
    }

    @Test
    fun savingEditedMedicineBlankQuantityEditTextTest() {
        val medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        val launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.medicineNameEditText)).perform(replaceText("Acetaminophen"))
        onView(withId(R.id.medicineQuantityEditText)).perform(replaceText(""))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(replaceText("1"))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Acetaminophen")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineQuantityEditText)).check(matches(hasErrorText(containsString("cannot"))))
    }

    @Test
    fun savingEditedMedicineEmptyNameEditTextTest() {
        val medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        val launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.medicineNameEditText)).perform(replaceText(" "))
        onView(withId(R.id.medicineQuantityEditText)).perform(replaceText("10"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(replaceText("1"))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText(" ")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineNameEditText)).check(matches(hasErrorText(containsString("cannot"))))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))
    }

    @Test
    fun saveEditedMedicineBlankNameEditTextTest() {
        val medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        val launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.medicineNameEditText)).perform(replaceText(""))
        onView(withId(R.id.medicineQuantityEditText)).perform(replaceText("10"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(replaceText("1"))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineNameEditText)).check(matches(hasErrorText(containsString("cannot"))))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("1"))))
    }

    @Test
    fun saveEditedMedicineBlankDailyUsageEditText() {
        val medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        val launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Paracetamol")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText(containsString("2"))))

        onView(withId(R.id.medicineNameEditText)).perform(replaceText("Acetaminophen"))
        onView(withId(R.id.medicineQuantityEditText)).perform(replaceText("10"))
        onView(withId(R.id.medicineDailyUsageEditText)).perform(replaceText(""))

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Acetaminophen")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("")))

        onView(withId(R.id.saveButton)).perform(click())

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("Acetaminophen")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("10"))))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(hasErrorText(containsString("cannot"))))
    }

    @Test
    fun displayingAndHidingAddQuantityViews() {
        val medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        val launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineQuantityEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.addMoreMedicineButton)).check(matches(isDisplayed()))
        onView(withId(R.id.amountOfMedicineToAddEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.acceptQuantityButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.addMoreMedicineButton)).perform(click())

        onView(withId(R.id.medicineQuantityEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.addMoreMedicineButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.amountOfMedicineToAddEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.acceptQuantityButton)).check(matches(isDisplayed()))

        onView(withId(R.id.acceptQuantityButton)).perform(click())

        onView(withId(R.id.medicineQuantityEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.addMoreMedicineButton)).check(matches(isDisplayed()))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText(containsString("11"))))
        onView(withId(R.id.amountOfMedicineToAddEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.acceptQuantityButton)).check(matches(not(isDisplayed())))
    }

    @Test
    fun increasingQuantityTest() {
        val medicineInIntent = Medicine()
        medicineInIntent.id = 5
        medicineInIntent.name = "Paracetamol"
        medicineInIntent.quantity = 15.0
        medicineInIntent.dailyUsage = 2.0
        medicineInIntent.savedTime = 1528624800000L   //10-06-2018, 12:00
        val launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicineInIntent)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineQuantityEditText))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("11"))))
        onView(withId(R.id.addMoreMedicineButton)).check(matches(isDisplayed()))
        onView(withId(R.id.amountOfMedicineToAddEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.acceptQuantityButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.addMoreMedicineButton)).perform(click())

        onView(withId(R.id.medicineQuantityEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.addMoreMedicineButton)).check(matches(not(isDisplayed())))
        onView(withId(R.id.amountOfMedicineToAddEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.acceptQuantityButton)).check(matches(isDisplayed()))

        onView(withId(R.id.amountOfMedicineToAddEditText))
                .perform(click())
                .perform(replaceText("2.5"))
        onView(withId(R.id.acceptQuantityButton)).perform(click())

        onView(withId(R.id.medicineQuantityEditText))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("13.5"))))
        onView(withId(R.id.addMoreMedicineButton)).check(matches(isDisplayed()))
        onView(withId(R.id.amountOfMedicineToAddEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.acceptQuantityButton)).check(matches(not(isDisplayed())))

        onView(withId(R.id.addMoreMedicineButton)).perform(click())
        onView(withId(R.id.amountOfMedicineToAddEditText))
                .check(matches(isDisplayed()))
                .check(matches(withText("")))
    }

    @Test
    fun increasingQuantityEmptyEditTextsTest() {
        activityRule.launchActivity(null)

        onView(withId(R.id.medicineQuantityEditText))
                .check(matches(isDisplayed()))
                .check(matches(withText("")))
        onView(withId(R.id.addMoreMedicineButton)).perform(click())
        onView(withId(R.id.medicineQuantityEditText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.amountOfMedicineToAddEditText))
                .check(matches(isDisplayed()))
                .check(matches(withText("")))
        onView(withId(R.id.acceptQuantityButton)).perform(click())
        onView(withId(R.id.medicineQuantityEditText))
                .check(matches(isDisplayed()))
                .check(matches(withText(containsString("0"))))
    }
}
