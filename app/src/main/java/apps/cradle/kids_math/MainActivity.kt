package apps.cradle.kids_math

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentActivity
import apps.cradle.kids_math.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    private enum class ACTION { ADDITION, SUBTRACTION }

    private val actionList: List<ACTION> = listOf(ACTION.ADDITION, ACTION.SUBTRACTION)

    private var a: Int = 0
    private var b: Int = 0
    private var action: ACTION = ACTION.ADDITION
    private val userInput: StringBuilder = StringBuilder()
    private var tasksCount: Int = DAILY_TASKS_COUNT

    @SuppressLint("InflateParams")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        tasksCount = if (isToday()) 0 else DAILY_TASKS_COUNT
        updateTask()
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        if (!isToday() && tasksCount == 0) {
            tasksCount= DAILY_TASKS_COUNT
            updateTaskUi()
        }
    }

    private fun updateTask() {
        generateTask()
        updateTaskUi()
    }

    private fun setListeners() {
        binding.apply {
            Button0.setOnClickListener(onButtonClick())
            Button1.setOnClickListener(onButtonClick())
            Button2.setOnClickListener(onButtonClick())
            Button3.setOnClickListener(onButtonClick())
            Button4.setOnClickListener(onButtonClick())
            Button5.setOnClickListener(onButtonClick())
            Button6.setOnClickListener(onButtonClick())
            Button7.setOnClickListener(onButtonClick())
            Button8.setOnClickListener(onButtonClick())
            Button9.setOnClickListener(onButtonClick())
            ButtonBackspace.setOnClickListener(onButtonClick())
            ButtonOK.setOnClickListener(onButtonClick())
         }
    }


private fun generateTask() {
    a = Random.nextInt(10)
    b = Random.nextInt(10)
    action = actionList[Random.nextInt(2)]

    if (a < b) {
        val temp = a
        a = b
        b = temp
    }

    userInput.clear()
}

private fun updateTaskUi() {
    if (tasksCount==0) {
        binding.aim.text=getString(R.string.activity_main_aim_Finish)
    } else {
        binding.aim.text = getString(R.string.activity_main_aim, tasksCount.toString())}
        binding.task.text = getString(
            R.string.activityMainTask,
            a.toString(),
            when (action) {
                ACTION.ADDITION -> "+"
                ACTION.SUBTRACTION -> "-"
            },
            b.toString(),
            userInput.toString()
        )

}


private fun onButtonClick(): (View) -> Unit = { button ->
    if (button.id == R.id.ButtonBackspace)
        if (userInput.isNotEmpty()) userInput.deleteAt(userInput.lastIndex)
    if (button.id == R.id.ButtonOK) chekAnswer()
    val onlyZero = userInput.length == 1 && userInput[0] == '0'

    if (userInput.length < 2 && !onlyZero) {
        when (button.id) {
            R.id.Button0 -> userInput.append("0")
            R.id.Button1 -> userInput.append("1")
            R.id.Button2 -> userInput.append("2")
            R.id.Button3 -> userInput.append("3")
            R.id.Button4 -> userInput.append("4")
            R.id.Button5 -> userInput.append("5")
            R.id.Button6 -> userInput.append("6")
            R.id.Button7 -> userInput.append("7")
            R.id.Button8 -> userInput.append("8")
            R.id.Button9 -> userInput.append("9")
        }
    }
    updateTaskUi()
}

private fun chekAnswer() {
    if (userInput.isEmpty()) return
    val correctAnswer=when(action) {
        ACTION.ADDITION -> a + b
        ACTION.SUBTRACTION -> a - b
    }

    val userAnswer = userInput.toString().toInt()
    if (userAnswer==correctAnswer)  {
        if (tasksCount > 0)
            tasksCount--
            if (tasksCount==0) saveToday()
    } else {
        if (!isToday()) tasksCount = DAILY_TASKS_COUNT
        val errorDialog = ErrorDialogFragment()
        val correctAnswerString=getCorrect(correctAnswer)
        val bundle=Bundle()
        bundle.putString(ErrorDialogFragment.EXTRA_MESSAGE, correctAnswerString)
        errorDialog.arguments=bundle
        errorDialog.show(supportFragmentManager, "error_dialog")
    }
    updateTask()
}
    private fun getCorrect(correctAnswer: Int): String {
        val builder=StringBuilder()
        builder.append(a)
        when(action){
            ACTION.ADDITION -> builder.append(" + ")
            ACTION.SUBTRACTION -> builder.append(" - ")
        }
        builder.append(b)
        builder.append(" = ")
        builder.append(correctAnswer)
        return builder.toString()
    }


    companion object{
        const val DAILY_TASKS_COUNT = 10
        const val  PREF_LAST_SUCCESS_DATE="PREF_LAST_SUCCESS_DATE"
    }

    private fun saveToday() {
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
            .edit()
            .putLong(PREF_LAST_SUCCESS_DATE, System.currentTimeMillis())
            .apply()
    }
    private fun isToday(): Boolean {
        val today = System.currentTimeMillis()
        val lastSuccess = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
            .getLong(PREF_LAST_SUCCESS_DATE, 0L)

        val todayCalendar = Calendar.getInstance().apply { timeInMillis=today }
        val lastCalendar = Calendar.getInstance().apply { timeInMillis=lastSuccess }
        val yers = todayCalendar.get(Calendar.YEAR) == lastCalendar.get(Calendar.YEAR)
        val months = todayCalendar.get(Calendar.MONTH) == lastCalendar.get(Calendar.MONTH)
        val days = todayCalendar.get(Calendar.DAY_OF_MONTH) == lastCalendar.get(Calendar.DAY_OF_MONTH)
        return yers && months && days
    }

}

