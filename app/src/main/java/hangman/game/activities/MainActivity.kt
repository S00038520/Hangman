package hangman.game.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import hangman.game.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //play game button click event
        btn_play_game.setOnClickListener {
            startActivity(Intent(this, SelectRegionActivity::class.java))
            finish()
        }

        //quit game button quit evennt
        btn_quit_game.setOnClickListener {
            showClosingDialog() //Display Confirmation Dialog to user
        }

    }


    /**
     * This functions shows the closing dialog to warn the user of their decision to close the app
     */
    fun showClosingDialog() {
        val builder = AlertDialog.Builder(this) //create an alert dialog buider object
        builder.setTitle("") //set title for the alert dialog
        builder.setMessage(R.string.quitGameDialogMessage)   //set message for alert dialog

        //performing positive action (YES Button)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            finish() //Close the Game
        }

        //performing negative / cancel action (NO Button)
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss() //dismiss the dialog
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    /**
     * When the user press the android back button, show the closing dialog
     */
    override fun onBackPressed() {
        showClosingDialog()

    }
}