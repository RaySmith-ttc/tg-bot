package js

/**
 * Wrapper for the Javascript `process` object.
 */
external val process: Process = definedExternally

external interface Process {
    val env: ProcessEnvVariables
}

external object ProcessEnvVariables {
    val TG_BOT_TOKEN: String
}