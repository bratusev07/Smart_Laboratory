package ru.bratusev.smartlab.feature_login.models

import ru.bratusev.smartlab.ui.core.resources.StringsRes

enum class LoginStage(val stateText: String, val isShowing: Boolean = true) {
    NOTHING_0(stateText = StringsRes.AUTH_NOT_STARTED, isShowing = false),
    START_1(stateText = StringsRes.AUTH_STARTED),
    SAVING_TOKEN_2(stateText = StringsRes.TOKEN_SAVING),
    CHECKING_TOKEN_3(stateText = StringsRes.TOKEN_CHECKING),
    COMPLETED_4(stateText = StringsRes.AUTH_SUCCESS),
    FAILED_DURING_LOGIN(stateText = StringsRes.ERROR),
    FAILED_DURING_TOKEN(stateText = StringsRes.ERROR),
}