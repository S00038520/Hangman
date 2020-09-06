package hangman.game.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import hangman.game.R
import kotlinx.android.synthetic.main.activity_game_over.*


class GameOverActivity : AppCompatActivity() {

    var currentLevel: Int = 0
    var region = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        region = intent.getStringExtra("region").toString()

        //try Againn textView Click event
        tv_try_again.setOnClickListener {
            tryAgain()
        }

        //Main menu textView Click event
        tv_main_menu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        //Quit textView Click event
        tv_quit.setOnClickListener {
            showClosingDialog()
        }
    }


    /**
     * This method allows the user tom start the game again
     */
    private fun tryAgain() {
        val intent = Intent(this, PlayActivity::class.java).apply {
            putExtra("current_level", 0)
            putExtra("region", region)
        }
        startActivity(intent)
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

    /**
     * If the user presses the back buttonn, we navigate to the main activity
     */
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java)) //navigate to main activity
        finish() //we destroy this activity
    }
}