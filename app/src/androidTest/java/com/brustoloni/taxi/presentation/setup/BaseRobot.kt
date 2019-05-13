package com.brustoloni.taxi.presentation.setup

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matcher


fun verify(func: BaseRobot.() -> Unit) = BaseRobot().apply { func() }

open class BaseRobot {

    fun matchText(text: String) = checkIfIsDisplayed(withText(containsString(text)))

    fun matchText(textId: Int, strParam : String = ""): ViewInteraction? {
        val text = getStringFromId(textId, strParam)
        return matchText(text)
    }

    fun matchId(id: Int) = checkIfIsDisplayed(withId(id))

    fun matchDrawable(imageRId: Int, rDrawableId: Int) = onView(withId(imageRId))
        .check(matches(DrawableMatcher(rDrawableId)))

    fun click(rViewId : Int) = onView(withId(rViewId)).perform(ViewActions.click())

    fun clickInText(strRId : Int, strParam : String = ""): ViewInteraction? {
        val text = getStringFromId(strRId, strParam)
        return clickInText(text)
    }

    fun clickInText(text:String) = onView(withText(containsString(text)))
        .perform(ViewActions.click())

    private fun checkIfIsDisplayed(matcher: Matcher<View>) =
        onView(matcher).check(matches(isDisplayed()))

    protected fun getStringFromId(strRId : Int, strParam : String = "") : String{

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

        return when(strParam.isEmpty()) {
            true -> targetContext.getString(strRId)
            else -> targetContext.getString(strRId, strParam)
        }
    }
}