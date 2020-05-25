package org.cascadebot.permissions

sealed class PermissionsResult(val cause: ResultCause?, val causeObject: Any?)

class Allow(cause: ResultCause? = null, causeObject: Any? = null) : PermissionsResult(cause, causeObject)
class Deny(cause: ResultCause? = null, causeObject: Any? = null) : PermissionsResult(cause, causeObject)
class Neutral : PermissionsResult(null, null)
