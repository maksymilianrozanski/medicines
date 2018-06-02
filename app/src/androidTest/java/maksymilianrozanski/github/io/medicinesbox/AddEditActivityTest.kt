package maksymilianrozanski.github.io.medicinesbox

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import maksymilianrozanski.github.io.medicinesbox.model.KEY_ID
import maksymilianrozanski.github.io.medicinesbox.model.Medicine
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddEditActivityTest {

    @Rule
    @JvmField
    var activityRule: ActivityTestRule<AddEditActivity> = ActivityTestRule(AddEditActivity::class.java, true, false)

    @Test
    fun displayingValuesFromIntentTest() {

        var medicine = Medicine()
        medicine.id = 5
        medicine.name = "some name"
        medicine.dailyUsage = 2
        medicine.quantity = 10
        medicine.savedTime = 1527957713864L

        var launchIntent = Intent()
        launchIntent.putExtra(KEY_ID, medicine)

        activityRule.launchActivity(launchIntent)

        onView(withId(R.id.medicineNameEditText)).check(matches(withText("some name")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("2")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("10")))
    }

    @Test
    fun launchingActivityWithNullIntentTest(){
        activityRule.launchActivity(null)
        onView(withId(R.id.medicineNameEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineDailyUsageEditText)).check(matches(withText("")))
        onView(withId(R.id.medicineQuantityEditText)).check(matches(withText("")))
    }


}