import java.security.MessageDigest

private fun hexStr(b: ByteArray): String {
    return b.joinToString("") {"%02x".format(it)}
}

private fun H(data: String) : String {
    val md = MessageDigest.getInstance("MD5")
    return hexStr(md.digest(data.toByteArray()))
}

private fun KD(secret: String, data: String): String {
    return H("$secret:$data")
}

fun main(args: Array<String>) {

    val username = "+821030943508@mnc008.mcc450.connectrcs.com"
    val domain = "mnc008.mcc450.connectrcs.com"
    val password = "ofKrPmVqoSKe" //"509169B00B940609ACEEACBFD4C3C9D5"
    val uri = "sip:mnc008.mcc450.connectrcs.com"

    val a1 = "$username:$domain:$password"
    val a2 = "REGISTER:$uri"

    val nounce = "16e42a4e-25ce-46c3-9b6e-d11879dd199c"
    val nccount = 1
    val cnounce = "f60783aa41003b7f429a9cfe347d8fd9"
    val qop = "auth"

    val nc = String.format("%08d", nccount)
  //  val nc = 1

    val temp = "$nounce:$nc:$cnounce:$qop:"

    val HA1 = H(a1)
    val HA2 = H(a2)
    println("HA1: $HA1")
    println("HA2: $HA2")
    val res = KD(HA1, temp + HA2)
    println(res)
}

