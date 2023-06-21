package Quiz;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

//class InvalidOperationException extends Exception
//{
//    public InvalidOperationException(String answer) {
//        super(String.format("%s is not allowed option for this question",answer));
//    }
//
//    public InvalidOperationException() {
//        super(String.format("Answers and questions must be of same length!"));
//    }
//}
class Question
{
    String type;
    String text;
    int points;
    String answer;

    public Question(String type, String text, int points, String answer) {
        this.type = type;
        this.text = text;
        this.points = points;
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getPoints() {
        return points;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(type.equals("TF")){
            sb.append("True/False ");
            sb.append(String.format("Question: %s Points: %d Answer: %s",text,points,answer));}
        else{
            sb.append("Multiple Choice ");
            sb.append(String.format("Question: %s Points %d Answer: %s",text,points,answer));}
        return sb.toString();
    }
}

class Quiz
{
    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    void addQuestion(String questionData){
        String [] parts = questionData.split(";");
        if(!(parts[3].equals("A") || parts[3].equals("B") || parts[3].equals("C") || parts[3].equals("D") || parts[3].equals("E") || parts[3].equals("true") || parts[3].equals("false")))
        {
            System.out.printf(String.format("%s is not allowed option for this question\n",parts[3]));
        }
        else {
            Question question = new Question(parts[0],parts[1],Integer.parseInt(parts[2]),parts[3]);
            questions.add(question);
        }
    }

    public void printQuiz(OutputStream os) {
        PrintWriter printWriter = new PrintWriter(os);
        questions.stream()
                .sorted(Comparator.comparing(Question::getPoints).reversed())
                .forEach(q -> printWriter.println(q));
        printWriter.flush();
    }

    public void answerQuiz(List<String> answers, OutputStream os) {
        if(questions.size() != answers.size())
        {
            System.out.printf("Answers and questions must be of same length!");
        }
        else {
            double total = 0.0;
            double pts;
            for (int i = 0; i < questions.size(); i++) {
                pts = 0.0;
                if (questions.get(i).answer.equals(answers.get(i))) {
                    pts = (double) questions.get(i).getPoints();
                    System.out.printf("%d. %.2f\n", i+1, pts);
                } else {
                    if (answers.get(i).equals("true") || answers.get(i).equals("false")) {
                        pts = 0.0;
                        System.out.printf("%d. %.2f\n", i+1, pts);
                    } else {
                        pts = -0.2 * questions.get(i).getPoints();
                        System.out.printf("%d. %.2f\n", i+1, pts);
                    }
                }
                total += pts;
            }
            System.out.printf("Total points: %.2f", total);
        }
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            quiz.answerQuiz(answers, System.out);
        } else {
            System.out.println("Invalid test case");
        }
    }
}

