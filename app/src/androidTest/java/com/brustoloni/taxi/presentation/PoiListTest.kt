package com.brustoloni.taxi.presentation

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.brustoloni.taxi.presentation.map.NavMapActivity
import com.brustoloni.taxi.presentation.setup.BaseInstrumentedTest
import com.brustoloni.taxi.presentation.setup.Constants.Companion.EMPTY_POILIST
import com.brustoloni.taxi.presentation.setup.Constants.Companion.NOT_FOUND_ERROR_CODE
import com.brustoloni.taxi.presentation.setup.Constants.Companion.SUCCESS_POILIST
import com.brustoloni.taxi.presentation.setup.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PoiListTest : BaseInstrumentedTest() {

    @get:Rule
    var activityRule: ActivityTestRule<NavMapActivity> =
        ActivityTestRule(NavMapActivity::class.java, false, false)

    private val poiListRobot = PoiListRobot()

    @Test
    fun shouldShowListOfItens() {

        setupMockWebServer(SUCCESS_POILIST)

        activityRule.launchActivity(Intent())

        verify {
            poiListRobot
                .waitSuccessView()

        }
    }

    @Test
    fun shouldShowEmptyView() {

        setupMockWebServer(EMPTY_POILIST)

        activityRule.launchActivity(Intent())

        verify {
            poiListRobot
                .waitAlternativeView()
                .withEmptyImage()
                .withEmptyText()
                .clickInTryAgain()
        }
    }

    @Test
    fun shouldShowErrorView() {

        setupMockWebServer(EMPTY_POILIST, statusCode = NOT_FOUND_ERROR_CODE)

        activityRule.launchActivity(Intent())

        verify {
            poiListRobot
                .waitAlternativeView()
                .withErrorImage()
                .withErrorText()
                .clickInTryAgain()
        }
    }
}