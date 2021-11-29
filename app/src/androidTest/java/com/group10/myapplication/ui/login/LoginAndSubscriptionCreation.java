package com.group10.myapplication.ui.login;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.group10.myapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginAndSubscriptionCreation {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void loginAndSubscriptionCreation() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.new_user_button), withText("New User"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                5)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction materialButton2 = onView(
                allOf(withClassName(is("com.google.android.material.button.MaterialButton")), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                1)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout1),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("cloud"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout2),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("pass"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password_confirm),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout3),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("pass"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.budget),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout5),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("40"), closeSoftKeyboard());

        ViewInteraction materialAutoCompleteTextView = onView(
                allOf(withId(R.id.currency), withText("US Dollar-$"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout4),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView.perform(replaceText("US Dollar-"));

        ViewInteraction materialAutoCompleteTextView2 = onView(
                allOf(withId(R.id.currency), withText("US Dollar-"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout4),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView2.perform(closeSoftKeyboard());

        DataInteraction materialTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.done_button), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                7),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.exit_button), withText("Back to Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                8),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.username_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout),
                                        0),
                                1)));
        appCompatEditText5.perform(scrollTo(), replaceText("cloud"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.password_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout2),
                                        0),
                                1)));
        appCompatEditText6.perform(scrollTo(), replaceText("pass"), closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.login_button), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.app_bar_navigation),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.subscription_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("Spotify"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.subscription_cost),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout2),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("4"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageButton")), withContentDescription("Next month"),
                        childAtPosition(
                                allOf(withClassName(is("android.widget.DayPickerView")),
                                        childAtPosition(
                                                withClassName(is("com.android.internal.widget.DialogViewAnimator")),
                                                0)),
                                2)));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction materialButton6 = onView(
                allOf(withClassName(is("com.google.android.material.button.MaterialButton")), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction materialAutoCompleteTextView3 = onView(
                allOf(withId(R.id.every), withText("year"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout4),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView3.perform(replaceText("mon"));

        ViewInteraction materialAutoCompleteTextView4 = onView(
                allOf(withId(R.id.every), withText("mon"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.text_input_layout4),
                                        0),
                                1),
                        isDisplayed()));
        materialAutoCompleteTextView4.perform(closeSoftKeyboard());

        DataInteraction materialTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        materialTextView2.perform(click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.done_button), withText("Create Subscription"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_fragment),
                                        0),
                                5),
                        isDisplayed()));
        materialButton7.perform(click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.go_to_applications), withText("Go To Applications"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_fragment),
                                        0),
                                6),
                        isDisplayed()));
        materialButton8.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.name), withText("Spotify"),
                        withParent(withParent(withId(R.id.list))),
                        isDisplayed()));
        textView.check(matches(withText("Spotify")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.cost), withText("4"),
                        withParent(withParent(withId(R.id.list))),
                        isDisplayed()));
        textView2.check(matches(withText("4")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.delete),
                        withParent(withParent(withId(R.id.list))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));
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
