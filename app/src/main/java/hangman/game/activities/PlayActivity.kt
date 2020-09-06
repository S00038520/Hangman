package hangman.game.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import hangman.game.R
import hangman.game.models.Question
import kotlinx.android.synthetic.main.activity_play.*


class PlayActivity : AppCompatActivity() {
    lateinit var text: TextView
    lateinit var gridView: GridView
    val mMediaPlayer = MediaPlayer();
    var player = MediaPlayer();
    var successSound: Int = -1
    var failedSound: Int = -1

    //This is an array of all letters, it is used to populate letters for our keyboard
    val letters =
        arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    //we convert the array of letters to a mutable list (an editable list)
    var lettersList = letters.toMutableList()

    //we declare our various objects and variables
    lateinit var region: String
    lateinit var answer: String
    lateinit var hint: String
    var currentLevel: Int = -1
    var wrongLevel: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        gridView = findViewById(R.id.gridView1) as GridView

        initMusic()  //we initialise our sound/music player
        initObjects() //we initialize all objects and variables in this function
        initializeAlphabets()  //we initialize our keyboard


    }

    private fun initObjects() {
        currentLevel = intent.getIntExtra("current_level", 0) //we get the current level from the previous activity
        region = intent.getStringExtra("region")!! //we get the selected region from the previous activity


        var questionArray: ArrayList<Question> =
            ArrayList()  //we create a question array that holds questions

        //This is used to pick the correct questions based on the region selected by the user
        when (region) {
            SelectRegionActivity.Europe -> questionArray = ArrayList(europeQuestionArray)
            SelectRegionActivity.North_America -> questionArray =
                ArrayList(northAmericaQuestionArray)
            SelectRegionActivity.Asia -> questionArray = ArrayList(asiaQuestionArray)
        }



        answer = questionArray.get(currentLevel).answer    //we initialise our answer object/variable
        hint = questionArray.get(currentLevel).hint     //we initialise our hint/question

        tvHint.text = hint   //we show our hint/question using the hint TextView
        tvGuessedAnswer.setFilters(arrayOf<InputFilter>(LengthFilter(answer.length))) //we set the length of our textView as the length of our answer

        //we mask our answer textView with "_"
        var blankString: String = ""
        answer.forEach {
            blankString += "_"
        }
        tvGuessedAnswer.text = blankString

        
        wrongLevel = 1 //we initialize the wrong level as 1
        setStatusImageView(wrongLevel)  //we display the current wrong level image
    }

    /**
     * We initialize our keypad sounnds
     */
    private fun initMusic() {
        successSound = getResources().getIdentifier("success", "raw", getPackageName())
        failedSound = getResources().getIdentifier("fail", "raw", getPackageName())
    }


    /**
     * We populate our keyboard on a gridview
     */
    private fun initializeAlphabets() {
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lettersList)
        gridView.adapter = adapter
        gridView.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            checkGuess((v as TextView).text, position, adapter)
            // lettersList.removeAt(position)


        }
        addSpaceToAnswer() //if there is a space in the answer, we add it
    }

    /**
     * Check if the user's guessed letter is correct and performs the necessary operations
     * @param guessedChar = the user guessed letter
     * @param position = position of the gueessed letter on the keyboard
     * @param adapter = the letter adapter object
     */
    private fun checkGuess( guessedChar: CharSequence?, position: Int, adapter: ArrayAdapter<String> ) {
        if (guessedChar!!.length == 1) { //confirm its just 1 character
            if (answer.contains(guessedChar, true)) {
                playSound(successSound)
                lettersList.set(position, "") //set the selected letter as blank on the keyboard
                adapter.notifyDataSetChanged()  //re populate the keyboard

                var guessedWord: String = tvGuessedAnswer.text.toString()

                for (i in 0 until guessedWord.length) { //we iterate through the guessed words to get empty characters (_)
                    if (guessedWord.get(i).toString() .equals("_") ) { //if the current character is empty (_)
                        if (guessedChar.toString()
                                .equals(answer.get(i).toString(), true)
                        ) { //if guessed char exists in the answer

                            val guessedWordBuilder = StringBuilder(guessedWord)
                            guessedWordBuilder.setCharAt(i, guessedChar.toString()
                                .toCharArray()[0]) //replace the guessedWord character

                            guessedWord =
                                guessedWordBuilder.toString() //update our guessed word string
                            tvGuessedAnswer.setText(guessedWordBuilder.toString()) //update our textView

                            if (tvGuessedAnswer.text.toString()
                                    .equals(answer, true)
                            ) { //check if guessed word equals answer to congratulate user
                                showCongratulationPage()
                            }
                        }
                    }
                }


            } else {
                playSound(failedSound)
                if (wrongLevel < 10) {
                    wrongLevel += 1
                    setStatusImageView(wrongLevel)
                    if (wrongLevel == 8) {
                        showTwoMoreTriesDialog()
                    }

                } else {

                    startActivity(Intent(this, GameOverActivity::class.java))
                    stopMediaPlayer()
                    finish()
                }
            }
        }

    }

    /*
    * This is used for playing the keyboard sounds. it accepts the sound as
    * a parameters annd plays it useing the mediaPlayer object
    * */

    private fun playSound(sound: Int) {
        if (player.isPlaying) {
            player.pause()
            player.release()
        }

        player = MediaPlayer.create(this, sound)
        player.start()
    }

    /**
     * This functions automatically checks the answer to find it it contains space and it adds it
     */
    private fun addSpaceToAnswer() {
        val space = " "
        if (answer.contains(space, true)) {

            var guessedWord: String = tvGuessedAnswer.text.toString()

            for (i in 0 until guessedWord.length) { //we iterate through the guessed words to get empty characters (_)
                if (guessedWord.get(i).toString()
                        .equals("_")
                ) { //if the current character is empty (_)
                    if (space.toString()
                            .equals(answer.get(i).toString(), true)
                    ) { //if guessed char exists in the answer

                        val guessedWordBuilder = StringBuilder(guessedWord)
                        guessedWordBuilder.setCharAt(i, space.toString()
                            .toCharArray()[0]) //replace the guessedWord character

                        guessedWord = guessedWordBuilder.toString() //update our guessed word string
                        tvGuessedAnswer.setText(guessedWordBuilder.toString()) //update our textView

                        if (tvGuessedAnswer.text.toString()
                                .equals(answer, true)
                        ) { //check if guessed word equals answer to congratulate user
                            showCongratulationPage()
                        }
                    }
                }
            }


        }


    }

    /**
     * This functions loads the congratulation page/activity
     */
    private fun showCongratulationPage() {
        val intent = Intent(this, CongratulationActivity::class.java).apply {
            putExtra("current_level", currentLevel)
            putExtra("region", region)
        }
        stopMediaPlayer()
        startActivity(intent)
        finish()
    }

    /**
     * stop media player
     */
    private fun stopMediaPlayer() {
        player.stop()
    }


    /**
     * This function is used to display the hanging stick picture
     */
    private fun setStatusImageView(wrongLevel: Int) {
        when (wrongLevel) {
            1 -> img_status.setImageResource(R.drawable.hangman1)
            2 -> img_status.setImageResource(R.drawable.hangman2)
            3 -> img_status.setImageResource(R.drawable.hangman3)
            4 -> img_status.setImageResource(R.drawable.hangman4)
            5 -> img_status.setImageResource(R.drawable.hangman5)
            6 -> img_status.setImageResource(R.drawable.hangman6)
            7 -> img_status.setImageResource(R.drawable.hangman7)
            8 -> img_status.setImageResource(R.drawable.hangman8)
            9 -> img_status.setImageResource(R.drawable.hangman9)
            10 -> img_status.setImageResource(R.drawable.hangman10)
        }

    }


    /**
     * This funnction shows a dialog saying "YOU HAVE 2 MORE TRIES"
     */
    private fun showTwoMoreTriesDialog() {
        val builder = AlertDialog.Builder(this@PlayActivity)

        // Set the alert dialog title
        builder.setTitle("")

        // Display a message on alert dialog
        builder.setMessage("YOU HAVE 2 MORE TRIES!")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("CONTINUE") { dialog, which ->
            dialog.dismiss()

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


    /**
     * When the user presses the back button, this method takes the user to the main page
     */
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }


    /**
     * These are global objects, variables and constants that are available through out the app and cann be accessed from any activity.
     * They are also  known as static objects
     */
    companion object {
        val europeQuestionArray =
            listOf<Question>(Question(1, "regarded as the greatest writer in the English language and the world's greatest dramatist", "William Shakespeare"), Question(2, "theoretical physicist who developed the theory of relativity, one of the two pillars of modern physics", "Albert Einstein"))


        val northAmericaQuestionArray =
            listOf<Question>(Question(1, "first president of the USA", "George Washington"), Question(2, "inventor of the light-bulb", "Thomas Edison"))

        val asiaQuestionArray =
            listOf<Question>(Question(1, "was the founder and first Great Khan and Emperor of the Mongol Empire", "Genghis Khan"), Question(2, "employed nonviolent resistance to lead the successful campaign for India's independence from British rule", "Mahatma Gandhi"))

        private const val TAG = "PlayActivity"


    }

}