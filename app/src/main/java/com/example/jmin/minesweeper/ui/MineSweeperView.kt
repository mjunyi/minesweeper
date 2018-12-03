package com.example.jmin.minesweeper.ui

import android.content.Context
import android.graphics.*
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.jmin.minesweeper.R
import com.example.jmin.minesweeper.model.minesweepermodel.MineSweeperModel
import kotlinx.android.synthetic.main.activity_main.view.*

class MineSweeperView constructor(context: Context?, attrs: AttributeSet?) : View(context, attrs){
    private var boardSize = MineSweeperModel.getBoardsize()
    private var gameOver = false
    private var firstDig = true
    private var win = false

    private val paintBackground = Paint()
    private val paintLine = Paint()
    private val paintFocus = Paint()
    private var paintNumber = Paint()

    private var bitmapFlag = BitmapFactory.decodeResource(resources, R.drawable.flag)
    private var bitmapMine = BitmapFactory.decodeResource(resources, R.drawable.mine)


    init{
        paintBackground.color = Color.BLACK
        paintBackground.strokeWidth = 10F
        paintBackground.style = Paint.Style.FILL

        paintLine.color = Color.GRAY
        paintLine.strokeWidth = 10F
        paintLine.style = Paint.Style.STROKE

        paintFocus.color = Color.GREEN
        paintFocus.strokeWidth = 10F
        paintFocus.style = Paint.Style.STROKE

        paintNumber.color = Color.LTGRAY
        paintNumber.textSize = 100F
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintNumber.textSize = height.toFloat()/boardSize
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, width/boardSize, height/boardSize, false)
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, width/boardSize, height/boardSize, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0F,0F,width.toFloat(), height.toFloat(),paintBackground)

        drawGameArea(canvas)
        drawFocus(canvas)
        drawFlags(canvas)
        drawAllCorrectlyDug(canvas)
        if(gameOver) drawMines(canvas)
    }

    fun drawGameArea(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
        for(i in 1..(boardSize-1)){
        canvas.drawLine(0f, (i * height / boardSize).toFloat(), width.toFloat(),
                                   (i * height / boardSize).toFloat(),
                                   paintLine)}
        for(i in 1..(boardSize-1)){
            canvas.drawLine((i * width / boardSize).toFloat(), 0f,
                            (i * width / boardSize).toFloat(), height.toFloat(),
                            paintLine)
        }
    }

    fun drawFocus(canvas: Canvas){
        val focus = MineSweeperModel.getFocus()
        if(focus[0]!=-1 && focus[1]!=-1){
            canvas.drawRect((focus[0]*(width/boardSize).toFloat()),
                            (focus[1]*(height/boardSize).toFloat()),
                            ((focus[0]+1)*(width/boardSize).toFloat()),
                            ((focus[1]+1)*(height/boardSize).toFloat()), paintFocus)
        }
    }

    fun flag(){
        if(!gameOver&&!win){
            val focus = MineSweeperModel.getFocus()
            if(focus[0]!=-1 && focus[1]!=-1){
                if (MineSweeperModel.getFieldItem(focus[0], focus[1],1)==MineSweeperModel.UNCOVERED)
                else if(MineSweeperModel.getFieldItem(focus[0], focus[1],0)==MineSweeperModel.MINE){
                    MineSweeperModel.setFieldItem(focus[0], focus[1], MineSweeperModel.FLAGGED)
                }
                else{
                    gameOver()
                }
            }
            invalidate()
            checkWin()
        }
    }

    fun drawFlags(canvas: Canvas){
        for (i in 0..(boardSize-1)){
            for (j in 0..(boardSize-1)){
                if(MineSweeperModel.getFieldItem(i,j,1)==MineSweeperModel.FLAGGED){
                    canvas.drawBitmap(bitmapFlag, (i*width/boardSize).toFloat(),
                                                  (j*height/boardSize).toFloat(), null)
                }
            }
        }
    }

    fun drawMines(canvas: Canvas){
        for (i in 0..(boardSize-1)){
            for (j in 0..(boardSize-1)){
                if(MineSweeperModel.getFieldItem(i,j,0)==MineSweeperModel.MINE){
                    canvas.drawBitmap(bitmapMine, (i*width/boardSize).toFloat(),
                            (j*height/boardSize).toFloat(), null)
                }
            }
        }
    }

    fun drawAllCorrectlyDug(canvas: Canvas){
        var numMines: Int
        for (i in 0..(boardSize-1)){
            for (j in 0..(boardSize-1)){
                if(MineSweeperModel.getFieldItem(i,j,1)==MineSweeperModel.UNCOVERED){
                    numMines = MineSweeperModel.mineCountFor(i,j)
                    canvas.drawText(numMines.toString(), (i+1)*width/boardSize.toFloat()-width/boardSize*0.78F,
                                                         (j+1)*height/boardSize.toFloat()-height/boardSize*0.15F,
                                                             paintNumber)
                }
            }
        }
    }

    fun gameOver(){
        gameOver = true
        showMessage("You Lost! Click \"RESTART GAME\" to play again!")
        invalidate()
    }

    fun showMessage(msg: String) {
        Snackbar.make(mineSweeperView, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean { //Remove the "?"
        val tX = event.x.toInt() / (width / boardSize)
        val tY = event.y.toInt() / (height / boardSize)

        if (!gameOver&&!win){
            if (tX < boardSize && tY < boardSize)
            {
                MineSweeperModel.placeFocus(tX, tY)
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    fun dig(){
        if(!gameOver&&!win){
            val focus = MineSweeperModel.getFocus()
            if(focus[0]!=-1 && focus[1]!=-1){
                if (MineSweeperModel.getFieldItem(focus[0], focus[1],1)==MineSweeperModel.FLAGGED)
                else if (MineSweeperModel.getFieldItem(focus[0], focus[1],0)==MineSweeperModel.MINE) {
                    gameOver()
                }
                else{
                    MineSweeperModel.setFieldItem(focus[0], focus[1], MineSweeperModel.UNCOVERED)
                    if(firstDig){
                        firstDig = false
                        digOtherNonMines(focus[0],focus[1])
                    }
                }
            }
            invalidate()
            checkWin()
        }
    }

    fun digOtherNonMines(column: Int, row: Int){
        var currentCoordinate: IntArray
        val aroundSelectedSquare = MineSweeperModel.coordinatesAroundSelectedSquare(column, row)
        for (i in 0..7){
            currentCoordinate = aroundSelectedSquare[i]
            if(currentCoordinate[0]!=-1&&currentCoordinate[1]!=-1){
                if(!MineSweeperModel.youAreMine(currentCoordinate[0], currentCoordinate[1])){
                    MineSweeperModel.setFieldItem(currentCoordinate[0],
                                                  currentCoordinate[1],
                                                  MineSweeperModel.UNCOVERED)
                }
            }
        }
    }

    fun checkWin(){
        var numUncoveredOrFlagged = 0
        for(i in 0..(boardSize-1)){
            for(j in 0..(boardSize-1)){
                if(MineSweeperModel.getFieldItem(i,j,1)!=MineSweeperModel.COVERED)
                    numUncoveredOrFlagged++
            }
        }
        if(numUncoveredOrFlagged==boardSize*boardSize){
            playerWon()
        }
    }

    fun playerWon(){
        win = true
        showMessage("You did it! Click \"RESTART GAME\" to play again!")
    }

    fun restart(){
        win = false
        gameOver = false
        firstDig = true
        MineSweeperModel.resetModel()
        invalidate()
    }

    fun difficultyChange(){
        win = false
        gameOver = false
        firstDig = true
        boardSize = MineSweeperModel.getBoardsize()
        paintNumber.textSize = height.toFloat()/boardSize
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, width/boardSize, height/boardSize, false)
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, width/boardSize, height/boardSize, false)
        MineSweeperModel.resetModel()
        invalidate()
    }
}