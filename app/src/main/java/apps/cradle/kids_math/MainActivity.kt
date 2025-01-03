package apps.cradle.kids_math

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater= LayoutInflater.from(this)
        val contentView = inflater.inflate(R.layout.activity_main,null)
        setContentView(contentView)
        
    }
}

