package uz.pdp.chatapp.modles

import java.io.Serializable

class GroupData : Serializable {
    private var id: String? = null
    private var group_name: String? = null
    private var about_group: String? = null


    constructor(id: String?, group_name: String?, about_group: String?) {
        this.id = id
        this.group_name = group_name
        this.about_group = about_group
    }

    constructor()

    fun getId(): String? {
        return id
    }

    fun getName(): String? {
        return group_name
    }

    fun getAbout(): String? {
        return about_group
    }

}