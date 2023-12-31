import java.util.*

fun main() {
    val gameApp = GameApp()
    val scanner = Scanner(System.`in`)
    gameApp.InitUnitType()
    gameApp.initUnits()
    var check = true
    println()
    var stepCounter: Int = 1

    Thread {
        while (check) {
            println("Шаг: $stepCounter")
            gameApp.printField()
            gameApp.doGameStep(gameApp.humanUnits)
            gameApp.doGameStep(gameApp.orcUnits)
            try {
                Thread.sleep(300)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            stepCounter++
        }
    }.start()

    Thread {
        if (scanner.hasNext()) {
            check = false
        }
    }.start()
}
