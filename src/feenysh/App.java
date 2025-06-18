package feenysh;

import java.util.Scanner;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class App {
    // ANSI Color Codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    private static final String UNDERLINE = "\u001B[4m";

    // Configuration
    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_ATTEMPTS = 3;
    private static final int CAPTCHA_TIMEOUT_SECONDS = 30;
    private static final String LOG_FILE = "feenysh_log.txt";

    public static void main(String[] args) {
        try {
            initializeLogFile();
            log("Application started");
            
            displayWelcome();
            displayLionArt();
            displayAuthorInfo();
            
            boolean running = true;
            while (running) {
                displayMainMenu();
                String choice = scanner.nextLine();
                log("User selected option: " + choice);
                
                switch (choice) {
                    case "1":
                        interactWithStoryQuestions();
                        break;
                    case "2":
                        if (mathCaptchaChallenge()) {
                            accessGrantedContent();
                        } else {
                            System.out.println(RED + "\nToo many failed attempts. Please try again later." + RESET);
                        }
                        break;
                    case "3":
                        displayContactInfo();
                        break;
                    case "4":
                        System.out.println(GREEN + "\nExiting Feenysh. Thank you for visiting!" + RESET);
                        running = false;
                        break;
                    default:
                        System.out.println(RED + "\nInvalid choice. Please try again." + RESET);
                }
            }
        } catch (Exception e) {
            System.err.println(RED + "An unexpected error occurred: " + e.getMessage() + RESET);
            logError("Fatal error: " + e.getMessage());
        } finally {
            scanner.close();
            log("Application shutdown");
        }
    }

    private static void initializeLogFile() throws IOException {
        Path path = Paths.get(LOG_FILE);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        String header = "\n\n=== Feenysh Session - " + currentTimestamp() + " ===\n";
        Files.write(path, header.getBytes(), StandardOpenOption.APPEND);
    }

    private static void log(String message) {
        try {
            String logEntry = "[" + currentTimestamp() + "] " + message + "\n";
            Files.write(Paths.get(LOG_FILE), logEntry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    private static void logError(String error) {
        try {
            String logEntry = "[" + currentTimestamp() + "] ERROR: " + error + "\n";
            Files.write(Paths.get(LOG_FILE), logEntry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write error to log file: " + e.getMessage());
        }
    }

    private static String currentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static void displayWelcome() {
        System.out.println(PURPLE + BOLD + "\n--- WELCOME TO FEENYSH ---\n" + RESET);
    }

    private static void displayLionArt() {
        System.out.println(YELLOW + """
             **********  ******
        **************************
      ******************************
    ****    ****            ****    **
    ****  ****                ****  **
  **********                    ********
  **********  ****        ****  ********
************      **    **      ********
************      **    **      ********
************      **    **    **********
**************    ********    **********
  ************        **      ********
    ************    ******  **********
    ****************      ************
      **************      **********
        **************************
            ********************
                **************
                  **********
                      ****
                         _.----~~~~~~-----..__
                  __..------~~~~-     .._     ~~-.
     ___.--.--~~~~     --~~~~---~ __  ~~----.__   ~~~~~~~---...._____
(~~~~_..----~       ~~--=< O >- .----. -< O >=--~~             ..   .)
 ~-..__..--         ..  ___-----_...__-----___        _.  ~-=___..-~
         `   _    ..   (     \" :_.}{._; \" \"   )      _-     '
          \\   ~~-      `   \" \" __###__  \"\"    '    -~     .'
           `-._  ~-.    _`--~~~VvvvvVV~~---'_     ~..    _.
               -.     `~##\\(            )/###~' .     _.~
                 -    `.###\\#    {     #/####.'    _-~
                  -_    -####    !     #####-  ..
                    -._  ~.###   }     ###-~ ___.-~
                       ~-  \\##  / \"   ##.~ /~
                         \\ |###  \"   ###' /
                          \\`/\\#######/\\' ;
                           ~-.^^^^^^^ .-~
                              ~~~~~~~~
        """ + RESET);
    }

    private static void displayAuthorInfo() {
        System.out.println(CYAN + BOLD + "\nFeenysh - A Console Demo by Kevin Marville" + RESET);
        System.out.println(CYAN + "=========================================" + RESET);
        System.out.println(BLUE + """
            This is a console-based application that showcases my work 
            and allows you to interact with me.
            """ + RESET);
        
        System.out.println(BOLD + "Author: " + RESET + "Kevin Marville");
        System.out.println(BOLD + "Role: " + RESET + "Developer, Author, and Gamer");
        System.out.println(BOLD + "Projects: " + RESET + "Currently writing: Merci Ruth 1 (Cyberpunk Novel)");
        System.out.println(BOLD + "Message: " + RESET + "Building a resilient world through tech, storytelling, and survival guides.");
    }

    private static void displayMainMenu() {
        System.out.println(PURPLE + BOLD + "\n--- MAIN MENU ---" + RESET);
        System.out.println(YELLOW + "1. Story Discussion" + RESET);
        System.out.println(YELLOW + "2. CAPTCHA Verification Challenge" + RESET);
        System.out.println(YELLOW + "3. Contact Information" + RESET);
        System.out.println(YELLOW + "4. Exit" + RESET);
        System.out.print(BLUE + "Enter your choice (1-4): " + RESET);
    }

    private static void interactWithStoryQuestions() {
        System.out.println(PURPLE + BOLD + "\n--- STORY DISCUSSION ---" + RESET);
        System.out.print(BLUE + "What's your favorite type of story (e.g., Sci-Fi, Fantasy, Thriller)? " + RESET);
        String favoriteStoryType = scanner.nextLine();
        log("User favorite story type: " + favoriteStoryType);
        System.out.println(GREEN + "That's great! You enjoy " + favoriteStoryType + " stories." + RESET);

        System.out.print(BLUE + "\nWhat's one question you have for Kevin about writing or stories? " + RESET);
        String userQuestion = scanner.nextLine();
        log("User question: " + userQuestion);
        System.out.println(CYAN + "\nInteresting question: \"" + userQuestion + "\"" + RESET);
        System.out.println(YELLOW + "Kevin would say: Writing is about exploring the human condition through imagined worlds!" + RESET);
    }

    private static boolean mathCaptchaChallenge() {
        System.out.println(PURPLE + BOLD + "\n--- CAPTCHA VERIFICATION ---" + RESET);
        System.out.println(BLUE + "Prove you're human by solving these math problems:" + RESET);
        
        Instant startTime = Instant.now();
        int attempts = 0;
        int correctAnswersNeeded = 2;
        int correctAnswers = 0;
        
        while (attempts < MAX_ATTEMPTS && correctAnswers < correctAnswersNeeded) {
            String problem = generateCaptchaProblem();
            int correctAnswer = solveCaptchaProblem(problem);
            
            System.out.printf(YELLOW + "\nProblem %d/%d: " + RESET + "What is " + CYAN + "%s" + RESET + "? ", 
                attempts + 1, MAX_ATTEMPTS, problem);
            
            try {
                int userAnswer = Integer.parseInt(scanner.nextLine());
                log(String.format("CAPTCHA attempt: problem=%s, answer=%d, correct=%d", 
                    problem, userAnswer, correctAnswer));
                
                if (Instant.now().isAfter(startTime.plusSeconds(CAPTCHA_TIMEOUT_SECONDS))) {
                    System.out.println(RED + "Verification timeout. Too slow!" + RESET);
                    return false;
                }
                
                if (userAnswer == correctAnswer) {
                    System.out.println(GREEN + "✓ Correct!" + RESET);
                    correctAnswers++;
                } else {
                    System.out.println(RED + "✗ Incorrect. Try again." + RESET);
                    attempts++;
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input. Numbers only!" + RESET);
                attempts++;
            }
        }
        
        return correctAnswers >= correctAnswersNeeded;
    }

    private static String generateCaptchaProblem() {
        int a = (int)(Math.random() * 15) + 1;
        int b = (int)(Math.random() * 15) + 1;
        int c = (int)(Math.random() * 10) + 1;
        int operation1 = (int)(Math.random() * 3);
        int operation2 = (int)(Math.random() * 3);
        
        String[] operators = {"+", "-", "*"};
        String op1 = operators[operation1];
        String op2 = operators[operation2];
        
        if (Math.random() > 0.5) {
            return String.format("(%d %s %d) %s %d", a, op1, b, op2, c);
        } else {
            return String.format("%d %s (%d %s %d)", a, op1, b, op2, c);
        }
    }

    private static int solveCaptchaProblem(String problem) {
        try {
            return (int) new Object() {
                int pos = -1, ch;
                
                void nextChar() {
                    ch = (++pos < problem.length()) ? problem.charAt(pos) : -1;
                }
                
                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }
                
                int parse() {
                    nextChar();
                    int x = parseExpression();
                    if (pos < problem.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                    return x;
                }
                
                int parseExpression() {
                    int x = parseTerm();
                    for (;;) {
                        if (eat('+')) x += parseTerm();
                        else if (eat('-')) x -= parseTerm();
                        else return x;
                    }
                }
                
                int parseTerm() {
                    int x = parseFactor();
                    for (;;) {
                        if (eat('*')) x *= parseFactor();
                        else return x;
                    }
                }
                
                int parseFactor() {
                    if (eat('(')) {
                        int x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')'");
                        return x;
                    }
                    StringBuilder sb = new StringBuilder();
                    while ((ch >= '0' && ch <= '9') || ch == ' ') {
                        if (ch != ' ') sb.append((char)ch);
                        nextChar();
                    }
                    return Integer.parseInt(sb.toString());
                }
            }.parse();
        } catch (Exception e) {
            logError("Error solving CAPTCHA problem: " + problem + " - " + e.getMessage());
            throw new RuntimeException("Error in captcha problem", e);
        }
    }

    private static void accessGrantedContent() {
        System.out.println(GREEN + BOLD + "\n=== ACCESS GRANTED ===" + RESET);
        System.out.println(CYAN + "Welcome to the exclusive content area!" + RESET);
        System.out.println(YELLOW + "\"The future of storytelling combines technology with human creativity.\"" + RESET);
        System.out.println(BLUE + "\nUpcoming projects:" + RESET);
        System.out.println("- " + PURPLE + "Merci Ruth 1 (Cyberpunk Novel)" + RESET);
        System.out.println("- " + PURPLE + "Feenysh Interactive Story Platform" + RESET);
        System.out.println("- " + PURPLE + "AI-Assisted Writing Tools" + RESET);
        log("User accessed granted content");
    }

    private static void displayContactInfo() {
        System.out.println(PURPLE + BOLD + "\n--- CONNECT WITH KEVIN ---" + RESET);
        System.out.println(BLUE + "Website: " + CYAN + UNDERLINE + "https://kvnbbg-creations.io" + RESET);
        System.out.println(BLUE + "Blog:    " + CYAN + UNDERLINE + "https://kvnbbg.fr" + RESET);
        System.out.println(YELLOW + "\nFor collaborations or questions, use the website contact form." + RESET);
    }
}