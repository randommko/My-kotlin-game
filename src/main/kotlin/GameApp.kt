import java.util.*

class GameApp {
    //TODO: удалить все ячейки у которых тип FREE и всю связанную с ними логику
    private val gameField = HashMap<Coords, Cell>()
    private lateinit var humanUnitType: UnitType
    private lateinit var orcUnitType: UnitType

    val humanUnits = HashMap<Coords, Unit>()
    val orcUnits = HashMap<Coords, Unit>()

    fun InitUnitType() {
        humanUnitType = UnitType(UnitType.Race.HUMAN)
        orcUnitType = UnitType(UnitType.Race.ORC)
    }

    fun initUnits() {
        for (i in 0 until GameSettings.globalParams.HUMAN_MAX_QTY) {
            var coords: Coords
            do {        //TODO: возможен бесконечный цикл если все поле занято
                coords = Coords(
                    Random().nextInt(GameSettings.globalParams.FIELD_X - 1),
                    Random().nextInt(GameSettings.globalParams.FIELD_Y - 1)
                )
            } while (gameField.containsKey(coords))
            gameField[coords] = Cell(Cell.CellTypes.UNIT)
            humanUnits[coords] = Unit(humanUnitType, coords)
        }
        for (i in 0 until GameSettings.globalParams.ORC_MAX_QTY) {
            var coords: Coords
            do {        //TODO: возможен бесконечный цикл если все поле занято
                coords = Coords(
                    Random().nextInt(GameSettings.globalParams.FIELD_X - 1),
                    Random().nextInt(GameSettings.globalParams.FIELD_Y - 1)
                )
            } while (gameField.containsKey(coords))
            gameField[coords] = Cell(Cell.CellTypes.UNIT)
            orcUnits[coords] = Unit(orcUnitType, coords)
        }
    }

    fun printField() {
        print("   ")
        for (i in 0 until GameSettings.globalParams.FIELD_X) {
            print("$i  ")
        }
        println()
        for (i in 0 until GameSettings.globalParams.FIELD_X) {
            print("$i  ")
            for (j in 0 until GameSettings.globalParams.FIELD_Y) {
                if (!gameField.containsKey(Coords(i, j))) print("*  ")
                //TODO: переписать вывод поля с учетом чот существуют объекты только у юнитов скал
                if (gameField.containsKey(Coords(i, j))) {
                    if (gameField[Coords(i, j)]!!.getType() == Cell.CellTypes.ROCK) print("R  ")
                    if (gameField[Coords(i, j)]!!.getType() == Cell.CellTypes.UNIT) {
                        if (humanUnits.containsKey(Coords(i, j))) print("H  ")
                        if (orcUnits.containsKey(Coords(i, j))) print("O  ")
                    }
                }
            }
            println()
        }
        println("--- --- --- ---")
    }

    fun doGameStep(unitMap: HashMap<Coords, Unit>) {

        //Передвинуть юнитов
        //Выбрать в какую сторону двигаться - getMoveDestination(Coords actualCoords)
        //Получить координаты передвижения - getMoveCroords(Coords unitCoords, int delta_x, int delta_y)
        //Проверить занятость клетки - if (...)
        //При необходимости атаковать если юнит в клетке ЖИВ
        //Если победа, то передвинуть юнита
        val movedUnits = HashMap<Coords, Coords>()
        for ((actualCoords, unit) in unitMap) {
            val destination: Coords = getMoveDestination(actualCoords)
            val newCoords: Coords = getMoveCroords(
                actualCoords,
                destination.X * unit.getSpeed(),
                destination.Y * unit.getSpeed()
            )
            if (!gameField.containsKey(newCoords)) movedUnits[actualCoords] = newCoords
            //moveUnit(unitMap, newCoords, actualCoords);
        }
        for ((key, value) in movedUnits) {
            moveUnit(unitMap, key, value)
            //unitMap.remove(coords);
        }
    }

    private fun getMoveDestination(actualCoords: Coords): Coords {
        val destination = Coords(0, 0)

        //если рандомное число больше 49 то движемся в положительном направлении
        //иначе в отрицаительном
        val rnd = Random()
        var temp = rnd.nextInt(100)
        if (temp > 66) destination.X = 1
        if ((temp >= 33) and (temp <= 66)) destination.X = 0
        if (temp < 39) destination.X = -1
        temp = rnd.nextInt(100)
        if (temp > 66) destination.Y = 1
        if ((temp >= 33) and (temp <= 66)) destination.Y = 0
        if (temp < 33) destination.Y = -1

        //прверка на границы поля
        if (actualCoords.X == 0) destination.X = 1
        if (actualCoords.X == GameSettings.globalParams.FIELD_X - 1) destination.X = -1
        if (actualCoords.Y == 0) destination.Y = 1
        if (actualCoords.Y == GameSettings.globalParams.FIELD_X - 1) destination.Y = -1
        return destination
    }

    private fun getMoveCroords(unitCoords: Coords, delta_x: Int, delta_y: Int): Coords {
        val newCoords = Coords(0, 0)
        if (unitCoords.X + delta_x > GameSettings.globalParams.FIELD_X - 1) newCoords.X =
            GameSettings.globalParams.FIELD_X - 1 else if (unitCoords.X + delta_x < 0) newCoords.X = 0 else newCoords.X =
            unitCoords.X + delta_x
        if (unitCoords.Y + delta_y > GameSettings.globalParams.FIELD_Y - 1) newCoords.Y =
            GameSettings.globalParams.FIELD_Y - 1 else if (unitCoords.Y + delta_y < 0) newCoords.Y = 0 else newCoords.Y =
            unitCoords.Y + delta_y
        return newCoords
    }

    private fun moveUnit(unitMap: HashMap<Coords, Unit>, newCoords: Coords, oldCoors: Coords) {
        //TODO: нельзя удалять объект из мапы пока ты итерируешься по мапе
        println(
            unitMap[oldCoors]!!.getUnitType()!!.getMyRace()
                .toString() + "#" + unitMap[oldCoors]!!.getID() + " переместился: " +
                    oldCoors.X + " ," + oldCoors.Y + " -> " + newCoords.X + " ," + newCoords.Y
        )
        unitMap[newCoords] = unitMap[oldCoors]!!
        gameField.remove(oldCoors)
        gameField[newCoords] = Cell(Cell.CellTypes.UNIT)
    }

    fun removeUnit(unitMap: HashMap<Coords, kotlin.Unit>, unitCoords: Coords) {
        unitMap.remove(unitCoords)
        gameField.remove(unitCoords)
    }
}