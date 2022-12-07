package wordle.project.bot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
//import org.json.*;

class Triple {
    public String word;
    public double frequency;
    public double entropy;

    Triple(String word, double frequency,double entropy) {
        this.word=word;
        this.frequency=frequency;
        this.entropy=entropy;
    }
}
enum Colors {
    GREEN {
        @Override
        public String toString() {
            return "GREEN";
        }
    },
    YELLOW {
        @Override
        public String toString() {
            return "YELLOW";
        }
    },
    GRAY {
        @Override
        public String toString() {
            return "GRAY";
        }
    };
}
class Result {
    String guess;
    Colors[] output = new Colors[5];
    Result(String guess, Colors[] output) {
        this.guess=guess;
        this.output=output;
    }
}
class WordAndFrequency {
    public String word;
    public int frequency;



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Word: "+word);
        sb.append("Frequency: "+frequency);
        return sb.toString();
    }

    WordAndFrequency(String word, int frequency) {
        this.word=word;
        this.frequency=frequency;
    }
}

public class Bot {
    public static List<String> words = new ArrayList<>();
    public static List<Double> frequencies = new ArrayList<>();
    public static List<Double> entropy = new ArrayList<>();
    public static List<Triple> list = new ArrayList<>();
    public static Colors[] returnColors(String solution, String guess) {
        //List<String> wordleWordsList = Arrays.asList(wordleString.split(""));
        String[] userWordsArray = guess.split("");
        List<Boolean> wordMatchesList = new ArrayList<>();
        String checked_chars = "";// letters checked until that instant
        Colors[] output = new Colors[5];

        for (int i = 0; i < 5; i++) // checking for each letter in the word inputted and then updating the color of the panel text for each letter
        {
            checked_chars = checked_chars + userWordsArray[i];
//			System.out.println("count of " + userWordsArray[i] + " is " + count_check(guess, userWordsArray[i]) );
            if (solution.charAt(i)==guess.charAt(i)) {
                output[i]=Colors.GREEN;
            } else if (count_check(solution, userWordsArray[i]) >= count_check(checked_chars, userWordsArray[i])) {
                output[i]=Colors.YELLOW;
            } else {
                output[i]=Colors.GRAY;
            }
        }
        return output;
    }
   // public static String guess(List<Result> pastGuesses, Result[] guesses) {
//        return "";
//    }
//    public static boolean isAnswer(String answer, String guess) {
//        return (answer.compareTo(guess)==0);
//    }
//    public static void play(String solution,Result[] guesses) {
//        List<Result> pastResults = new ArrayList<>();
//        for(int i=0;i<6;i++) {
//            String currentGuess = guess(pastResults,guesses);
//            pastResults.add(new Result(currentGuess, returnColors(solution,currentGuess)));
//            if(isAnswer(solution,currentGuess))
//                break;
//        }
//    }
    public static int count_check(String s, String c) {
        int count = 0;
        String[] s_arr = s.split("");
        for (int i = 0; i < s.length(); i++) {
            if (c.equals(s_arr[i])) {
                count++;
            }
        }
        return count;
    }
    public static int count_check(String s, char c) {
        int count = 0;
        String[] s_arr = s.split("");
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)==c) {
                count++;
            }
        }
        return count;
    }
    public static int correctGuess(String answer,String guess, char ch) {
        int count=0;
        for(int i=0;i<answer.length();i++) {
            if(answer.charAt(i)==guess.charAt(i) && answer.charAt(i)==ch)
                count++;
        }
        return count;
    }
    public static String test(String answer, String guess)
    {
        String color = "";
        for (int i = 0; i < guess.length(); i++) {
            if (answer.charAt(i) == guess.charAt(i))
                color += "g";
            else if (count_check(answer, guess.charAt(i)) == 0)
                color += "b";
            else if ((count_check(answer, guess.charAt(i)) < count_check(guess, guess.charAt(i)))
                    && count_check(guess, guess.charAt(i)) > correctGuess(answer, guess, guess.charAt(i)))
                color += "b";
            else
                color += "y";
        }
        System.out.println(color);
        System.out.println(color.length());
        return color;
    }
    private static List<WordAndFrequency> readJson(String path) throws IOException {
        List<WordAndFrequency> list = new ArrayList<>();
        String lines = "";
        try {
            lines+=(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
            lines+="\n";
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return list;
    }
    public static List<Double> calculateEntropy() {
        int count=0;
        int size=words.size();
        for (int i = 0; i < size; i++) {
            double value = frequencies.get(i);
            count++;
        }
        for (int i = 0; i < words.size(); i++) {
            double freq = frequencies.get(i);
            double p = (1.0 * freq)/ size;
            if (freq > 0) {
                double calc = -1 * p * Math.log(p) / Math.log(2);
                entropy.add(calc);
                list.add(new Triple(words.get(i),frequencies.get(i),calc));
            }

        }
        return entropy;
    }
    public static double sigmoid(double x) {
        return 1/(1+ (Math.pow(Math.E,-1*x)));
    }


        //public static String nextGuess()
    public static void main(String[] args) throws IOException {
        String solution="chair";
        Result[] guesses = new Result[6];

        int count=0;



        try
        {
            File file = new File("./assets/word_list.txt");
        //    File file=new File("C:\\Users\\Kalyan\\IdeaProjects\\demo\\src\\main\\java\\com\\example\\demo\\word_list.txt");    //creates a new file instance
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String line;

            while((line=br.readLine())!=null)
            {
                String[] line_arr = line.split(":");
                words.add(line_arr[0]);
                frequencies.add(Double.parseDouble(line_arr[1]));
            }
            fr.close();
          //  System.out.println("Contents of File: ");
//            for (int i=0; i < words.size(); i++)
//            {
//                System.out.println(words.get(i) +" --- "+frequencies.get(i));
//            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        List<Double> entropy = calculateEntropy();
        int size=words.size();
        System.out.println(entropy.size());
        System.out.println(words.size());
        for(int i=0;i<list.size();i++) {
            System.out.println("Word: "+list.get(i).word+"\t Entropy: "+list.get(i).entropy);
        }
       }
    }