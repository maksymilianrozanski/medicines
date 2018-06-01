package maksymilianrozanski.github.io.medicinesbox

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.mock.MockContext
import maksymilianrozanski.github.io.medicinesbox.data.MedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.data.TestMedicinesDatabaseHandler
import maksymilianrozanski.github.io.medicinesbox.module.AppModule
import maksymilianrozanski.github.io.medicinesbox.module.ContextModule
import maksymilianrozanski.github.io.medicinesbox.module.DatabaseModule
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
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java, true, false)

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
}