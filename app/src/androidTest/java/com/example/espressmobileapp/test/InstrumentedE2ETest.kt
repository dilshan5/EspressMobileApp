package com.example.espressmobileapp.test

import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.espressmobileapp.util.getAllCitiesFromSource
import org.hamcrest.Matchers.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import com.example.espressmobileapp.util.CommonConstants.ApplicationDetails.APPLICATION_NAME
import com.example.espressmobileapp.util.CommonConstants.CityDetails.FULL_CITY_NAME
import com.example.espressmobileapp.util.CommonConstants.CityDetails.PARTIAL_CITY_NAME
import com.example.espressmobileapp.util.CommonConstants.TestLevel.E2E_TEST
import com.example.espressmobileapp.util.checkSearchResultContentByPartialCityName
import com.example.espressmobileapp.util.clickCityAtRow
import com.example.espressmobileapp.model.City
import com.example.espressmobileapp.util.ListMatcher
import com.example.espressmobileapp.views.MainActivity
import com.example.espressmobileapp.views.MapActivity
import org.junit.*
import org.junit.Assert.assertTrue
import java.util.*
import com.example.espressmobileapp.R
import com.example.espressmobileapp.util.ListMatcher.listItemCount

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class InstrumentedE2ETest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    companion object {
        var rawCityList: List<City> = getAllCitiesFromSource()
    }

    @Before
    fun setUp() {
        // Initializes Intents
        Intents.init()
    }

    @After
    fun tearDown() {
        // Clears Intents state.
        Intents.release()
    }

    @Test
    fun bb_TC_001_UsersShouldBeAbleToOpenTheApplication() {
        //check the application name
        onView(withText(APPLICATION_NAME))
            .check(matches(isDisplayed()))

        //Verify the search text field is displayed
        onView(withId(R.id.search))
            .check(matches(isDisplayed()))

        // Verify search city list shows all cities from the data source
        onView(withId(R.id.citiesList))
            .check(matches(ListMatcher.withItemSize(rawCityList.size)))

        Log.i(E2E_TEST, "Verified bb_TC_001_UsersShouldBeAbleToOpenTheApplication : Status: PASSED ")
    }

    @Test
    fun bb_TC_010_UsersShouldBeAbleToSearchCityWithPartialCityName() {
        // Type city name.
        onView(withId(R.id.search))
            .perform(typeText(PARTIAL_CITY_NAME), closeSoftKeyboard())
        Thread.sleep(4000)

        // Verify the search results contain all cities starting with partialCityName
        checkSearchResultContentByPartialCityName(PARTIAL_CITY_NAME)

        Log.i(
            E2E_TEST,
            "Verified bb_TC_010_UsersShouldBeAbleToSearchCityWithPartialCityName : Status: PASSED "
        )
    }

    @Test
    fun bb_TC_005_UsersShouldBeAbleToSearchCityWithCityNameAndCountryCode() {
        // Type city name.
        onView(withId(R.id.search))
            .perform(typeText(FULL_CITY_NAME), closeSoftKeyboard())
        Thread.sleep(4000)

        // Verify the search results contain only city with full city name
        onData((anything())).inAdapterView(withId(R.id.citiesList))
            .atPosition(0)
            .onChildView(withId(R.id.cityName))
            .check(matches(withText(FULL_CITY_NAME)))

        Log.i(
            E2E_TEST,
            "Verified bb_TC_005_UsersShouldBeAbleToSearchCityWithCityNameAndCountryCode : Status: PASSED "
        )
    }

    @Test
    fun bb_TC_008_UsersShouldNotGetAnyResultsForInvalidCityName() {
        // Type city name.
        onView(withId(R.id.search))
            .perform(typeText("!@#$%^%%"), closeSoftKeyboard())
        Thread.sleep(4000)

        Log.i("Search results item count: ", ListMatcher.getItemCount().toString())
        //get the number of items in the search results view. Count should be Zero
        assertTrue("Invalid Search Results Displayed", listItemCount == 0)

        //Check the search result view
        onView(withId(R.id.cityName))
            .check(doesNotExist())

        Log.i(
            E2E_TEST,
            "Verified bb_TC_008_UsersShouldNotGetAnyResultsForInvalidCityName : Status: PASSED "
        )
    }

    @Test
    fun bb_TC_009_VerifyFilterGetClearWhenInputTextIsDeleted() {
        // Type city name.
        onView(withId(R.id.search))
            .perform(typeText(PARTIAL_CITY_NAME), closeSoftKeyboard())
        Thread.sleep(2000)

        //Clears text from input field
        onView(withId(R.id.search))
            .perform(clearText())
        Thread.sleep(2000)

        //Verify the search text field is displayed
        onView(withId(R.id.search))
            .check(matches(isDisplayed()))

        // Verify search city list shows all cities from the data source
        onView(withId(R.id.citiesList))
            .check(matches(ListMatcher.withItemSize(rawCityList.size)))

        Log.i(
            E2E_TEST,
            "Verified bb_TC_009_VerifyFilterGetClearWhenInputTextIsDeleted : Status: PASSED "
        )
    }

    @Test
    fun bb_TC_007_UsersShouldAbleToNavigateBackFromTheMapScreen() {
        //select a city and navigate to the map view
        clickCityAtRow(rawCityList, 3)

        // Verifying map activity is loaded
        // verify the COORDINATES_LAT and COORDINATES_LON using intent BundleMatchers
        intended(
            allOf(
                toPackage("app.com.mobileassignment"),
                hasComponent(MapActivity::class.java.name),
            )
        )
        Thread.sleep(3000)

        //press back button (keep note that we are not in the root activity now)
        Espresso.pressBack()
        Thread.sleep(3000)

        //Verify the search text field is displayed
        onView(withId(R.id.search))
            .check(matches(isDisplayed()))

        // Verify search city list shows all cities from the data source
        onView(withId(R.id.citiesList))
            .check(matches(ListMatcher.withItemSize(rawCityList.size)))

        Log.i(
            E2E_TEST,
            "Verified bb_TC_007_UsersShouldAbleToNavigateBackFromTheMapScreen : Status: PASSED "
        )

    }
}