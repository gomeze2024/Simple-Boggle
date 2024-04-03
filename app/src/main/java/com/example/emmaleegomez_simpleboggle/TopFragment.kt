package com.example.emmaleegomez_simpleboggle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class TopFragment : Fragment(), ButtonRecyclerViewAdapter.ItemClickListener {

    private lateinit var adapter: ButtonRecyclerViewAdapter
    private lateinit var wordState : TextView
    private lateinit var clearButton : Button
    private lateinit var submitButton : Button
    private lateinit var wordBank : List<String>

    private val vowels : List<Char> = listOf('A', 'E', 'I', 'O', 'U')
    private val doubleValue : List<Char> = listOf('S', 'Z', 'P', 'X', 'Q')
    private val alphabet : List<Char> = ('A'..'Z').toList()
    private val wordSet = HashSet<CharSequence>()

    private val viewModel: BoggleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.clearGame.observe(viewLifecycleOwner) { item ->
            if (item) {
                resetGame()
                viewModel.clearGame(false)
            }
        }

        val view = inflater.inflate(R.layout.top_fragment, container, false)
        val boardData = generateBoard()
        wordState = view.findViewById(R.id.wordState)
        clearButton = view.findViewById(R.id.clear)
        submitButton = view.findViewById(R.id.submit)

        lifecycleScope.launch {
            wordBank = fetchWordBank()
            wordBank = wordBank.map { it.uppercase() }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val numberOfColumns = 4
        recyclerView.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        adapter = ButtonRecyclerViewAdapter(requireContext(), boardData)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter

        clearButton.setOnClickListener {
            adapter.enableAllButtons()
            wordState.text = ""
        }

        submitButton.setOnClickListener {
            val word = wordState.text.toString()
            val vowelCount = word.count { it in vowels}
            val context = submitButton.context
            var score = 0

            if (word.length < 4 || vowelCount < 2
                || wordSet.contains(word)
                || !wordBank.contains(word)) {
                score -= 10
                Toast.makeText(context, "That's incorrect, $score", Toast.LENGTH_SHORT).show()
            } else {
                val doubleScore = word.any { it in doubleValue }
                for (char in word) {
                    score += if (char in vowels) 5 else 1
                }
                if (doubleScore) {
                    score *= 2
                }

                Toast.makeText(context, "That's correct, +$score", Toast.LENGTH_SHORT).show()
            }
            wordSet.add(word)
            adapter.enableAllButtons()
            wordState.text = ""
            viewModel.changeScore(score)
        }

        return view
    }

    private fun generateBoard() : List<Char> {
        val randomVowels = List(2) { vowels.random() }
        val randomLetters = List(14) { alphabet.random()}

        return (randomVowels + randomLetters).shuffled()
    }

    private suspend fun fetchWordBank(): List<String> {
        return withContext(Dispatchers.IO) {
            URL("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt").openStream()
                .bufferedReader().readLines()
        }
    }

    private fun resetGame() {
        adapter.enableAllButtons()
        adapter.updateData(generateBoard())
        wordState.text = ""
        wordSet.clear()
    }

    override fun onItemClick(view: View?, position: Int) {
        val letter = adapter.getItem(position)
        wordState.append(letter.toString())
    }
}