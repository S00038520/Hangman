package hangman.game.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hangman.game.R
import kotlinx.android.synthetic.main.activity_select_region.*

class SelectRegionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_region)

        //europe textView click event
        tv_europe.setOnClickListener {
           val intent: Intent = Intent(this, PlayActivity::class.java) //intent used to navigate to the PlayActivity
            intent.putExtra("region", Europe) // we pass the region as a string when navigating to PlayActivity
            intent.putExtra("current_level", 0) // we pass the current level as a string when navigating to PlayActivity
            startActivity(intent)  //we nnavigate to PlayActivity
            finish() //we destroy this activity
        }

        //nort america textview click event
        tv_north_america.setOnClickListener {
            val intent: Intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("region",   North_America)
            startActivity(intent)
            finish()
        }

        //asia textview click evennt
        tv_asia.setOnClickListener {
            val intent: Intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("region",
                Asia)
            startActivity(intent)
            finish()
        }

    }

    //static objects that are accessible in all parts of the app. it contains static variables, constants and objects
    companion object {
        val Europe = "EUROPE"
        val Asia = "ASIA"
        val North_America = "NORTH AMERICA"
    }
}