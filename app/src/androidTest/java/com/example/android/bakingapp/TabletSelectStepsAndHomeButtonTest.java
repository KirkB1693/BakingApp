package com.example.android.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TabletSelectStepsAndHomeButtonTest {

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void tabletSelectStepsAndHomeButtonTest() {

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.select_recipe_rv),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView2.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView3.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction textView = onView(
                withId(R.id.recipe_step_instructions_tv));
        textView.check(matches(withText("3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.")));

        ViewInteraction frameLayout = onView(
                allOf(withId(R.id.recipe_instructions_player),
                        childAtPosition(
                                allOf(withId(R.id.video_container_rl),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView4.perform(actionOnItemAtPosition(6, click()));

        ViewInteraction recyclerView5 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView5.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction recyclerView6 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView6.perform(actionOnItemAtPosition(5, click()));

        ViewInteraction textView2 = onView(
                withId(R.id.recipe_step_instructions_tv));
        textView2.check(matches(withText("5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.")));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView7 = onView(
                allOf(withId(R.id.select_recipe_rv),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView7.perform(actionOnItemAtPosition(3, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction recyclerView8 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView8.perform(actionOnItemAtPosition(11, click()));

        ViewInteraction recyclerView9 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView9.perform(actionOnItemAtPosition(8, click()));

        ViewInteraction recyclerView10 = onView(
                allOf(withId(R.id.recipe_steps_rv),
                        childAtPosition(
                                withId(R.id.recipe_steps_container),
                                3)));
        recyclerView10.perform(actionOnItemAtPosition(10, click()));

        ViewInteraction textView3 = onView(
                withId(R.id.recipe_step_instructions_tv));
        textView3.check(matches(withText("10. Turn off the oven but keep the cheesecake in the oven with the door closed for 50 more minutes.")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.step_short_description_tv), withText("Turn off oven and leave cake in."),
                        childAtPosition(
                                allOf(withId(R.id.step_wrapper_ll),
                                        childAtPosition(
                                                withId(R.id.recipe_step_cv),
                                                0)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Turn off oven and leave cake in.")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
