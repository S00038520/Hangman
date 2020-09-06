package hangman.game.activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import hangman.game.R
import kotlinx.android.synthetic.main.activity_congratulation.*


class CongratulationActivity : AppCompatActivity() {

    var currentLevel: Int = 0
    var region = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulation)

        currentLevel = intent.getIntExtra("current_level", 0) // get the current level
        currentLevel += 1 //increment the current level by 1
        region = intent.getStringExtra("region").toString()

        if (currentLevel >1) { //if current level is 2, meaning nno more questions, make next_level_textview invicible
            tv_next_level.visibility = View.GONE
        }

        tv_next_level.setOnClickListener {
            if (currentLevel > 1) { //we check if there is no more question
                showNoMoreQuestionsDialog()  //we show the user a dialog
            } else {

                goToNextLevel()  //we go to the next level
            }
        }


        tv_main_menu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java)) //we navigate to the main menu activity
            finish()
        }

        tv_quit.setOnClickListener {
            showClosingDialog() //show the closing dialog
            finish()
        }


    }

    /**
     *  go to the next level
     */

    private fun goToNextLevel() {
        val intent = Intent(this, PlayActivity::class.java).apply {
            putExtra("current_level", currentLevel)
            putExtra("region", region)
        }
        startActivity(intent)
        finish()
    }


    /**
     * Show the user a dialog sayinng "NO MORE QUESTIONS"
     */
    private fun showNoMoreQuestionsDialog() {

        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        builder.setTitle("")

        // Display a message on alert dialog
        builder.setMessage("NO MORE QUESTIONS")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }


    //show dialog to alert user before closing
    fun showClosingDialog() {
        val builder = AlertDialog.Builder(this) //create an alert dialog buider object
        builder.setTitle("") //set title for the alert dialog
        builder.setMessage(R.string.quitGameDialogMessage)   //set message for alert dialog

        //performing positive action (YES Button)
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            finish() //Close the Game
        }

        //performing negative / cancel action (NO Button)
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() //dismiss the dialog
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}