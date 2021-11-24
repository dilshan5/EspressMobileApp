package com.example.espressmobileapp.util;


import android.view.View;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


public class ListMatcher {

    public static int listItemCount = 0;

    /**
     * This method is to implement a matcher to compare the size of a list
     *
     * @param listSize expected size of the list
     * @return returns true if matching else false
     */
    public static Matcher<View> withItemSize(final int listSize) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("SearchList should have " + listSize + " cities");
            }

            @Override
            public boolean matchesSafely(final View view) {
                return ((ListView) view).getCount() == listSize;
            }
        };
    }

    /**
     * This method is to implement a matcher to get the ListView item count
     *
     * @return returns true
     */
    public static Matcher<View> getItemCount() {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(final Description description) {
                description.appendText("Number of items: " + listItemCount);
            }

            @Override
            public boolean matchesSafely(final View view) {
                listItemCount = ((ListView) view).getCount();
                return true;
            }
        };
    }

}
