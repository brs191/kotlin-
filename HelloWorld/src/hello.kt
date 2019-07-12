import com.shreks.Person

val pi: Float by lazy {
    3.14f
}

fun main(args: Array<String>) {

    val program = Program()

    program.addTwoNumber(2, 7)
    program.addTwoNumber(2, 7, object: myInterface {
        override fun execute(sum: Int) {
            println(sum)
        }
    })

    val test: String = "Hello"

    //val myLamda: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
    var result = 0;
    program.addTwoNumber(3, 8) { x: Int, y: Int -> result = x + y }
    println(result)

    program.reverseAndDsipaly("HelloG", { it.reversed() })

    val myNumbers: List<Int> = listOf(2, 3, 4, 5, 6, 7, 9)

    val mySqNum = myNumbers.map { it * it }
    for (num in mySqNum) {
        println(num)
    }

    val myPredicate = { num: Int -> num > 5}

    val check1 = myNumbers.all(myPredicate)
    println(check1)

    val check2 = myNumbers.any(myPredicate)
    println(check2)

    var name: String? = null

    name?.let {
        println("The length of name is ${name.length}")
    }

    val len = name?.length ?: -1
    println("Lenght is $len")


    val country = Country()
    country.setup()
    println(country.name)

    println(pi)
}

class Country {
    lateinit var name: String

    fun setup() {
        name = "Korea"
        println("country name is $name")
    }
}

class Program {

    fun addTwoNumber(a: Int, b: Int, action: (Int, Int) -> Unit) {
        action(a, b) // println(s)
    }

    fun addTwoNumber(a: Int, b: Int, action: myInterface) {
        val sum = a + b
        action.execute(sum)
    }

    fun addTwoNumber(a: Int, b: Int) {
        val sum = a + b
        println(sum)
    }

    fun reverseAndDsipaly(str:String, myFunc: (String) -> String) {
        var rev = myFunc(str)
        println(rev)
    }
}

interface myInterface {
    fun execute(sum: Int)
}