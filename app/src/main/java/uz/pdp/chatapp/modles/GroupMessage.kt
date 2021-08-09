package uz.pdp.chatapp.modles

class GroupMessage {
    private var message: String? = null
    private var fromImg: String? = null
    private var toImg: String? = null
    private var time: String? = null
    private var groupName: String? = null
    private var fromName: String? = null


    constructor()
    constructor(
        message: String?,
        fromImg: String?,
        toImg: String?,
        time: String?,
        groupName: String?,
        fromName: String?,
    ) {
        this.message = message
        this.fromImg = fromImg
        this.toImg = toImg
        this.time = time
        this.groupName = groupName
        this.fromName = fromName
    }

    fun getMessage(): String? {
        return message
    }

    fun getFromImg(): String? {
        return fromImg
    }

    fun getToImg(): String? {
        return toImg
    }

    fun getTime(): String? {
        return time
    }

    fun getGroupName(): String? {
        return groupName
    }

    fun getFromName(): String? {
        return fromName
    }

}