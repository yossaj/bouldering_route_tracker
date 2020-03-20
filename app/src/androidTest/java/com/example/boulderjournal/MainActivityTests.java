package com.example.boulderjournal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTests {

    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void checkRouteNoteCanBeMovedToDone(){

        onView(withId(R.id.recyclerRoutesToDo)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, swipeLeft())
        );

        onView(withId(R.id.recyclerRoutesDone)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, swipeRight())
        );
    }

    @Test
    public void checkPreferencesCanBeOpenedAndClosed() {
        onView(withId(R.id.preferences))
                .perform(click());

        onView(withId(R.id.day_picker)).check(matches(isCompletelyDisplayed())).perform(pressBack());
    }


}
