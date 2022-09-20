package org.ehfg.app.lambda.speaker

data class EventSpeaker(
    val id: String,
    val firstName: String,
    val lastName: String,
    val organisation: String,
    val imageUrl: String,
    val biography: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}