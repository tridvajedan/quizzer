import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;

public class Question {
    public String category,type,difficulty,question,correctAnswer;
    public String[] wrongAnswers;

    public Question(String category, String type, String difficulty, String question, String correctAnswer, String[] wrongAnswers) {
        this.category = category;
        this.type = type.substring(0,1).toUpperCase(Locale.ROOT) + type.substring(1);
        this.difficulty = difficulty.substring(0,1).toUpperCase(Locale.ROOT) + difficulty.substring(1);
        this.question = question.replaceAll("&.*?;","");
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
        initWrongAnswers();
    }

    public void initWrongAnswers() {
        for(int i = 0; i < wrongAnswers.length;i++) {
            wrongAnswers[i] = wrongAnswers[i].replaceAll("&.*?;","");
            String word = wrongAnswers[i];
            if (i == 0) {
                if (wrongAnswers[i + 1] != null)
                {
                    wrongAnswers[i] = word.substring(2, word.length() - 1);
                }
                else
                {
                    wrongAnswers[i] = word.substring(2, word.length() - 2);
                }
            } else if (i == wrongAnswers.length-1) {
                wrongAnswers[i] = word.substring(1, word.length() - 2);
            } else
            {
                wrongAnswers[i] = word.substring(1, word.length() - 1);
            }
        }
    }


    @Override
    public String toString() {
        return "Category - " + category + '\n' +
                " Type - " + type + '\n' +
                " Difficulty - " + difficulty + '\n' +
                " Question - " + question + '\n' +
                " CorrectAnswer - " + correctAnswer + '\n' +
                " WrongAnswers - " + Arrays.toString(wrongAnswers);
    }
}
