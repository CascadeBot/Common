package org.cascadebot.permissions

sealed class PermissionsResult(val cause: ResultCause?, val causeObject: Any?)

class ALLOW(cause: ResultCause? = null, causeObject: Any? = null) : PermissionsResult(cause, causeObject)
class DENY(cause: ResultCause? = null, causeObject: Any? = null) : PermissionsResult(cause, causeObject)
class NEUTRAL : PermissionsResult(null, null)
