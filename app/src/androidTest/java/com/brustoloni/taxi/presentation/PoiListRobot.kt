package com.brustoloni.taxi.presentation

import com.brustoloni.taxi.R
import com.brustoloni.taxi.presentation.setup.BaseRobot

class PoiListRobot : BaseRobot() {

    fun withEmptyImage(): PoiListRobot {
        matchDrawable(R.id.imageStatus, R.drawable.gauge_empty)
        return this
    }
    fun withErrorImage(): PoiListRobot {
        matchDrawable(R.id.imageStatus, R.drawable.sad_cloud)
        return this
    }

    fun withEmptyText(): PoiListRobot {
        matchText(R.string.empty_msg_try_again)
        return this
    }

    fun withErrorText(): PoiListRobot {
        matchText(R.string.error_msg_try_again)
        return this
    }

    fun clickInTryAgain(): PoiListRobot {
        click(R.id.tryAgainButton)
        return this
    }

    fun waitAlternativeView(): PoiListRobot {
        Thread.sleep(1000)
        matchId(R.id.imageStatus)
        return this
    }

    fun waitSuccessView(): PoiListRobot {
        Thread.sleep(1000)
        matchId(R.id.rv_map_poi_vehicles)
        return this
    }
}