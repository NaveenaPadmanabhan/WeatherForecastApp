package com.navy.weatherforecastapp.activity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.navy.weatherforecastapp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

    @Test
    fun mainActivityTest() {
        val frameLayout = onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        )
        frameLayout.check(matches(isDisplayed()))

        val frameLayout2 = onView(
            allOf(
                withId(android.R.id.content),
                withParent(
                    allOf(
                        withId(androidx.appcompat.R.id.action_bar_root),
                        withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))

        val appCompatImageView = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_button), withContentDescription("Search"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_bar),
                        childAtPosition(
                            withId(R.id.searchView),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("NEW york "), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("NEW york "),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(click())

        val searchAutoComplete3 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("NEW york "),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete3.perform(click())

        val searchAutoComplete4 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("NEW york "),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete4.perform(pressImeActionButton())

        val appCompatImageView2 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_button), withContentDescription("Search"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_bar),
                        childAtPosition(
                            withId(R.id.searchView),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val searchAutoComplete5 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete5.perform(replaceText("New York"), closeSoftKeyboard())

        val frameLayout3 = onView(
            allOf(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java), isDisplayed())
        )
        frameLayout3.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
