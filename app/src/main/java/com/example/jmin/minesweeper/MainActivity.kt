package com.example.jmin.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.example.jmin.minesweeper.model.minesweepermodel.MineSweeperModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val difficultyLevel = arrayOf("EASY", "MEDIUM", "HARD", "HARD++")
        difficultySeekbar.progress = 5
        difficultySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                difficultyTv.text = "DIFFICULTY: " + difficultyLevel[i-4]
                MineSweeperModel.changeBoardsize(i)
                mineSweeperView.difficultyChange()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is started.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Write code to perform some action when touch is stopped.
            }

        })

        digBtn.setOnClickListener {
            mineSweeperView.dig()
        }
        flagBtn.setOnClickListener {
            mineSweeperView.flag()
        }
        restartBtn.setOnClickListener {
            mineSweeperView.restart()
        }
        MineSweeperModel.placeMines()
    }
}
