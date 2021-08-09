package uz.pdp.chatapp.modles

class Message {
    private var message: String? = null
    private var fromUser: String? = null
    private var toUser: String? = null
    private var time: String? = null

    constructor(message: String?, fromUser: String?, toUser: String?, time: String?) {
        this.message = message
        this.fromUser = fromUser
        this.toUser = toUser
        this.time = time
    }

    constructor()

    fun getMessage(): String? {
        return message
    }

    fun getFromUser(): String? {

        return fromUser
    }

    fun getToUser(): String? {

        return toUser
    }

    fun getTime(): String? {
        return time
    }

}