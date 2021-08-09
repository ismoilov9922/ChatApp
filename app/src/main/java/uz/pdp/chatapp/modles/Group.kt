package uz.pdp.chatapp.modles

import java.io.Serializable

class Group : Serializable {
    private var name: String? = null
    private var info: String? = null
    private var id: String? = null

    constructor(name: String?, info: String?, id: String?) {
        this.name = name
        this.info = info
        this.id = id
    }

    constructor()

    fun getName(): String? {
        return name
    }

    fun getInfo(): String? {
        return info
    }

    fun getId(): String? {
        return id
    }
}