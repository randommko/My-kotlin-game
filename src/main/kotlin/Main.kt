import java.util.*

fun main() {
    val gameApp = GameApp()
    val scanner = Scanner(System.`in`)
    gameApp.initUnitType()
    gameApp.initUnits()
    var check = true
    println()
    var stepCounter: Int = 1

    Thread {
        while (check) {
            println("Шаг: $stepCounter")
            gameApp.printField()
            gameApp.doGameStep(UnitType.Race.HUMAN, gameApp.humanUnits)
            gameApp.doGameStep(UnitType.Race.ORC, gameApp.orcUnits)
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
