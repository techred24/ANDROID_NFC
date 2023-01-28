package com.example.poo

class Person(var name: String = "Anonimo", var passport: String? = null) {
    var alive: Boolean = true;

    fun Person() {
        name = "Juan"
        passport = "NEW NUMBER"
    }

    fun die() {
        alive = false;
    }
}