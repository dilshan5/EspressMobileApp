package com.example.espressmobileapp.util

import android.util.Log
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.example.espressmobileapp.R
import com.example.espressmobileapp.model.City
import com.example.espressmobileapp.utils.JsonMapper
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.startsWith
import java.util.*

/**
 * This method will return the list of all cities from the cities.json file
 *
 * @return Array list of City objects
 */
fun getAllCitiesFromSource(): List<City> {
    val rawCityList: List<City> = JsonMapper().getCityListFromRawFile(
        InstrumentationRegistry.getInstrumentation().targetContext, R.raw.cities
    )
    //sort the rawCityList since the default cityList view is alphabetically sorted
    Collections.sort(rawCityList)
    return rawCityList
}

/**
 * This method check the content of the city search results (By partial city name)
 *
 * @param cityName partial city name
 * @return
 */
fun checkSearchResultContentByPartialCityName(cityName: String) {
    //get the number of items in the search results view
    ListMatcher.getItemCount()
    //iterate through all the cities
    for (i in 0..ListMatcher.listItemCount) {
        onData((anything())).inAdapterView(withId(R.id.citiesList))
            .atPosition(i)
            .onChildView(withId(R.id.cityName))
            .check(ViewAssertions.matches(withText(startsWith(cityName))))
    }
}

/**
 * This method will click the city at given row
 *
 * @param cityList Array list of City objects
 * @param row position of the city name to be selected
 * @return the City object of the selected item
 */
fun clickCityAtRow(cityList: List<City>, row: Int): City {
    onData(anything())
        .inAdapterView(withId(R.id.citiesList))
        .atPosition(row)
        .perform(scrollTo())
        .perform(click())

    Log.i(
        "Helper.kt",
        "Select and click City: " + cityList[row].name
    )

    return cityList[row]
}

