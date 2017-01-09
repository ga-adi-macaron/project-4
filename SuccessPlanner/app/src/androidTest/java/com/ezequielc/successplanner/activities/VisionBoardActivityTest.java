package com.ezequielc.successplanner.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ezequielc.successplanner.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VisionBoardActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void visionBoardActivityTest() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Vision Board"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_upload_image), withContentDescription("Upload Image"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_text), withContentDescription("Text"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("New"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.new_text_edit_text), isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.new_text_edit_text), isDisplayed()));
        appCompatEditText2.perform(replaceText("Expresso Testing"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.new_text_edit_text), withText("Expresso Testing"), isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("ADD")));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView3 = onView(
                allOf(withId(R.id.action_text), withContentDescription("Text"), isDisplayed()));
        actionMenuItemView3.perform(click());

        ViewInteraction actionMenuItemView4 = onView(
                allOf(withId(R.id.action_text), withContentDescription("Text"), isDisplayed()));
        actionMenuItemView4.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(android.R.id.text1), withText("Change Size"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                3),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Expresso Testing"),
                        withParent(allOf(withId(R.id.activity_vision_board),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Set")));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView5 = onView(
                allOf(withId(R.id.action_text), withContentDescription("Text"), isDisplayed()));
        actionMenuItemView5.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("Change Color"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                2),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("Expresso Testing"),
                        withParent(allOf(withId(R.id.activity_vision_board),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView2.perform(click());

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(android.R.id.text1), withText("Green"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction actionMenuItemView6 = onView(
                allOf(withId(R.id.action_text), withContentDescription("Text"), isDisplayed()));
        actionMenuItemView6.perform(click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(android.R.id.text1), withText("Edit"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatTextView6.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withText("Expresso Testing"),
                        withParent(allOf(withId(R.id.activity_vision_board),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        textView3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.new_text_edit_text), withText("Expresso Testing"), isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.new_text_edit_text), withText("Expresso Testing"), isDisplayed()));
        appCompatEditText5.perform(replaceText("Expresso Testing..."), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.new_text_edit_text), withText("Expresso Testing..."), isDisplayed()));
        appCompatEditText6.perform(pressImeActionButton());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1), withText("EDIT")));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction actionMenuItemView7 = onView(
                allOf(withId(R.id.action_delete), withContentDescription("Delete"), isDisplayed()));
        actionMenuItemView7.perform(click());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(android.R.id.text1), withText("Delete Item"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                0),
                        isDisplayed()));
        appCompatTextView7.perform(click());

        ViewInteraction imageView = onView(
                allOf(withClassName(is("android.widget.ImageView")),
                        withParent(allOf(withId(R.id.activity_vision_board),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageView.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("Yes")));
        appCompatButton4.perform(scrollTo(), click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView8 = onView(
                allOf(withId(R.id.title), withText("Change Background Color"), isDisplayed()));
        appCompatTextView8.perform(click());

        ViewInteraction appCompatTextView9 = onView(
                allOf(withId(android.R.id.text1), withText("Yellow"),
                        childAtPosition(
                                allOf(withId(R.id.select_dialog_listview),
                                        withParent(withId(R.id.contentPanel))),
                                3),
                        isDisplayed()));
        appCompatTextView9.perform(click());

        ViewInteraction actionMenuItemView8 = onView(
                allOf(withId(R.id.action_save), withContentDescription("Save"), isDisplayed()));
        actionMenuItemView8.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("Yes")));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button2), withText("No")));
        appCompatButton6.perform(scrollTo(), click());

        pressBack();

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(android.R.id.button1), withText("Yes")));
        appCompatButton7.perform(scrollTo(), click());

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
