package com.example.jmin.minesweeper.model.minesweepermodel

import java.util.*

object MineSweeperModel{
    private var boardSize = 5

    public val FLAGGED: Short = 2
    public val UNCOVERED: Short = 1
    public val COVERED: Short = 0

    public val MINE: Short = 1
    public val EMPTY: Short = 0

    private val currFocus = arrayOf(-1, -1)

    var mineLocation = arrayOf(-1, -1)

    public fun placeFocus(column: Int, row: Int){
            currFocus[0] = column
            currFocus[1] = row
    }

    public fun getFocus() = currFocus

    private var minefield = Array(boardSize) {Array(boardSize){(shortArrayOf(EMPTY, COVERED))}}

    fun randomNumGenerator(to: Int) : Int {
        return Random(System.currentTimeMillis()).nextInt(to)
    }

    public fun makeRandomMine(){
        mineLocation[0] = randomNumGenerator(boardSize)
        mineLocation[1] = randomNumGenerator(boardSize)
    }

    public fun placeMines(){
        var minesPlaced = 0
        while(minesPlaced< (boardSize* boardSize/5)){
            makeRandomMine()
            if(minefield[mineLocation[0]][mineLocation[1]][0]==EMPTY){
                minefield[mineLocation[0]][mineLocation[1]][0] = MINE
                minesPlaced++
            }
        }
    }

    public fun mineCountFor(column: Int, row: Int): Int{
        var mineCount =0
        var currentCoordinate: IntArray
        val aroundSelectedSquare = coordinatesAroundSelectedSquare(column, row)
            for (i in 0..7){
                currentCoordinate = aroundSelectedSquare[i]
                if(currentCoordinate[0]!=-1&&currentCoordinate[1]!=-1){
                    if(youAreMine(currentCoordinate[0],currentCoordinate[1])) mineCount++}
                }
        return mineCount
    }

    public fun coordinatesAroundSelectedSquare(column: Int, row: Int): Array<IntArray>{
        var result = Array(8) {IntArray(2){-1}}
        if ((column-1)>=0 && (row-1)>=0) result[0] = intArrayOf((column-1), (row-1))
        if ((column)>=0 && (row-1)>=0) result[1] = intArrayOf(column, (row-1))
        if ((column+1)<=(boardSize-1) && (row-1)>=0) result[2] = intArrayOf(column+1, row-1)
        if ((column-1)>=0 && (row)>=0) result[3] = intArrayOf(column-1, row)
        if ((column+1)<=(boardSize-1) && (row)>=0) result[4] = intArrayOf(column+1, row)
        if ((column-1)>=0 && (row+1)<=(boardSize-1)) result[5] = intArrayOf(column-1, row+1)
        if ((column)>=0 && (row+1)<=(boardSize-1)) result[6] = intArrayOf(column, row+1)
        if ((column+1)<=(boardSize-1) && (row+1)<=(boardSize-1)) result[7] = intArrayOf(column+1, row+1)
        return result
    }

    public fun youAreMine(column:Int, row: Int): Boolean{
        return minefield[column][row][0]== MINE
    }

    public fun getFieldItem(column: Int, row: Int, mineOrCover: Int) = minefield[column][row][mineOrCover]

    public fun setFieldItem(column: Int, row: Int, secondInput: Short){
        minefield[column][row][1] = secondInput
    }

    public fun resetModel(){
        minefield = Array(boardSize) {Array(boardSize){(shortArrayOf(EMPTY, COVERED))}}
        for(i in 0..(boardSize-1)){
            for(j in 0..(boardSize-1)){
                minefield[i][j][0] = EMPTY
                minefield[i][j][1] = COVERED
            }
        }
        placeFocus(-1, -1)
        placeMines()
    }

    public fun getBoardsize() = boardSize

    public fun changeBoardsize(i: Int){
        boardSize = i
    }
}