package ru.raysmith.tgbot

import seskar.js.JsValue

/**
 * @property finger fingerprint-based biometrics
 * @property face face-based biometrics
 * @property unknown biometrics of an unknown type
 * */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface BiometricType {
    companion object {

        /** fingerprint-based biometrics */
        @JsValue("finger")
        val finger: BiometricType

        /** face-based biometrics */
        @JsValue("face")
        val face: BiometricType

        /** biometrics of an unknown type */
        @JsValue("unknown")
        val unknown: BiometricType
    }
}
