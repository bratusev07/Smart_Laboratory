package ru.bratusev.smartlab.feature_login.models

import org.jetbrains.compose.resources.StringResource
import smartlaboratory.ui.core.generated.resources.Res
import smartlaboratory.ui.core.generated.resources.auth_failed
import smartlaboratory.ui.core.generated.resources.auth_not_started
import smartlaboratory.ui.core.generated.resources.auth_started
import smartlaboratory.ui.core.generated.resources.auth_success
import smartlaboratory.ui.core.generated.resources.token_checking
import smartlaboratory.ui.core.generated.resources.token_save_failed
import smartlaboratory.ui.core.generated.resources.token_saving

enum class LoginStage(val stateStringRes: StringResource, val isShowing: Boolean = true) {
    NOTHING_0(stateStringRes = Res.string.auth_not_started, isShowing = false),
    START_1(stateStringRes = Res.string.auth_started),
    SAVING_TOKEN_2(stateStringRes = Res.string.token_saving),
    CHECKING_TOKEN_3(stateStringRes = Res.string.token_checking),
    COMPLETED_4(stateStringRes = Res.string.auth_success),
    FAILED_DURING_LOGIN(stateStringRes = Res.string.auth_failed),
    FAILED_DURING_TOKEN(stateStringRes = Res.string.token_save_failed),
}