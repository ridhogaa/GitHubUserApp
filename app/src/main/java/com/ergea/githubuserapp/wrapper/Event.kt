package com.ergea.githubuserapp.wrapper

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

open class Event<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}