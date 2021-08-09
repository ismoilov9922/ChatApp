package uz.pdp.chatapp.modles

import java.io.Serializable

class UserData : Serializable {
    private var uid: String? = null
    private var username: String? = null
    private var telNumber: String? = null
    private var imageUrl: String? = null
    private var email: String? = null
    private var lastTime: String? = null
    private var lastMessage: String? = null
    private var online: Boolean? = null


    constructor()
    constructor(
        uid: String?,
        username: String?,
        telNumber: String?,
        imageUrl: String?,
        email: String?,
        lastTime: String?,
        lastMessage: String?,
        online: Boolean?,
    ) {
        this.uid = uid
        this.username = username
        this.telNumber = telNumber
        this.imageUrl = imageUrl
        this.email = email
        this.lastTime = lastTime
        this.lastMessage = lastMessage
        this.online = online
    }


    fun getUid(): String? {
        return uid
    }

    fun getLastTime(): String? {
        return lastTime
    }

    fun getLastMessage(): String? {
        return lastMessage
    }

    fun setLastMessage(lastMessage: String?) {
        this.lastMessage = lastMessage
    }

    fun setLastTime(lastTime: String?) {
        this.lastTime = lastTime
    }


    fun getUsername(): String? {
        return username
    }

    fun getTelNumber(): String? {
        return telNumber
    }


    fun getImageUrl(): String? {
        return imageUrl
    }

    fun getEmail(): String? {
        return email
    }

    fun getOnline(): Boolean? {
        return online
    }

}