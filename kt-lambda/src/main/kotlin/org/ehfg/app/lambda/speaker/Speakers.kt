package org.ehfg.app.lambda.speaker

data class EventSpeaker(
    val id: String,
    val firstName: String,
    val lastName: String,
    val description: String,
    val imageUrl: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}