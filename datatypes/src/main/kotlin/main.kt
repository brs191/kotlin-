open class bakeryGoods(val flavour : String) {
    fun eat(): String {
        return "yummy $flavour bakery good ${name()}"
    }

    open fun name() : String {
        return "bakery goods"
    }
}

class cupCake(flavour: String) : bakeryGoods(flavour) {
    override fun name(): String {
        return "cupCake"
    }
}

class biscuit(flavour: String) : bakeryGoods(flavour) {
    override fun name(): String {
        return "biscuit"
    }
}

fun main()  {
    val blueBerrycupCake = cupCake("blueberry")
    val strawBerrycupCake = biscuit("strawBerry")

    println(blueBerrycupCake.eat())
    println(strawBerrycupCake.eat())

    println("Hello World")
}