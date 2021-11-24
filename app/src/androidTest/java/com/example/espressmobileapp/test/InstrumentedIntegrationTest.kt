package com.example.espressmobileapp.test

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.BundleMatchers.hasEntry
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.espressmobileapp.R
import com.example.espressmobileapp.model.City
import com.example.espressmobileapp.util.CommonConstants.CityDetails.CITY_NAME
import com.example.espressmobileapp.util.CommonConstants.TestLevel.INTEGRATION_TEST
import com.example.espressmobileapp.util.clickCityAtRow
import com.example.espressmobileapp.util.getAllCitiesFromSource
import com.example.espressmobileapp.views.MainActivity
import com.example.espressmobileapp.views.MapActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
class InstrumentedIntegrationTest {

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
    fun bb_TC_002_UsersShouldBeAbleToTypeCityName() {
        //Verify the Search text field is enable
        onView(withId(R.id.search))
            .check(matches(isEnabled()))
            .perform(
                typeText(CITY_NAME),
                closeSoftKeyboard()
            )
        //Verify the text field hint
        onView(withId(R.id.search))
            .check(matches(withHint("Search")))

        Log.i(
            INTEGRATION_TEST,
            "Verified bb_TC_002_UsersShouldBeAbleToTypeCityName : Status: PASSED "
        )
    }

    @Test
    fun bb_TC_003_VerifyAccuracyOfCityDataSource() {
        //If there is a duplicate records, this will be failed since HashSet wont accept duplicate records
        val tempSet: MutableSet<City> = HashSet()
        for (city in rawCityList) {
            assertFalse("cities.json file contains duplicate records.", tempSet.add(city))
        }

        Log.i(
            INTEGRATION_TEST,
            "Verified bb_TC_003_VerifyAccuracyOfCityDataSource : Status: PASSED"
        )
    }

    @Test
    fun bb_TC_006_UsersShouldBeAbleToSearchCityAndViewLocationOnMap() {
        //select a city and navigate to the map view
        val selectedCityObject = clickCityAtRow(rawCityList, 2)

        // Verifying map activity is loaded
        // verify the COORDINATES_LAT and COORDINATES_LON using intent BundleMatchers
        intended(
            allOf(
                toPackage("com.example.espressmobileapp"),
                hasComponent(MapActivity::class.java.name),
                hasExtras(
                    allOf(
                        hasEntry(
                            equalTo("COORDINATES_LAT"),
                            equalTo(selectedCityObject.coord.lat)
                        ),
                        hasEntry(
                            equalTo("COORDINATES_LON"),
                            equalTo(selectedCityObject.coord.lon)
                        )
                    )
                )
            )
        )
        //
        //verify the ping on the map
        onView(withId(R.id.insert_point))
            .check(matches(isDisplayed()))

        Log.i(
            INTEGRATION_TEST,
            "Verified bb_TC_006_UsersShouldBeAbleToSearchCityAndViewLocationOnMap : Status: PASSED "
        )
    }
}