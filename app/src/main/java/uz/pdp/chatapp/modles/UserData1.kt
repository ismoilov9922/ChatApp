package uz.pdp.chatapp.modles

import java.io.Serializable

class UserData1 : Serializable {
    private var uid: String? = null
    private var newMessage: String? = null
    private var time: String? = null
    private var userMessage: String? = null


    constructor()
    constructor(
        uid: String?,
        newMessage: String?,
        time: String?,
        userMessage: String?,
    ) {
        this.uid = uid
        this.newMessage = newMessage
        this.time = time
        this.userMessage = userMessage
    }


    fun getUid(): String? {
        return uid
    }

    fun getNewMessage(): String? {
        return newMessage
    }

    fun getTime(): String? {
        return time
    }

    fun getUserMessage(): String? {
        return time
    }

}