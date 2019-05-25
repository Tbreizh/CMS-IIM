package guillo.tristan.cms.services

import guillo.tristan.cms.Model
import guillo.tristan.cms.model.User

class LoginService(private val model: Model) {
    fun getUserByUsername(username: String): User? = model.getUserByUsername(username)
}