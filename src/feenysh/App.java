package feenysh;

import java.util.Scanner; // Import the Scanner class to read user input

public class App {
    public static void main(String[] args) {
        String name = "Kevin Marville";
        String role = "Developer, Author, and Gamer";
        String project = "Currently writing: Merci Ruth 1 (Cyberpunk Novel)";
        String message = "Building a resilient world through tech, storytelling, and survival guides.";

        // Drawing design output
        public class FullLion {
            public static void main(String[] args) {
                System.out.println("             **********  ******");
                System.out.println("        **************************");
                System.out.println("      ******************************");
                System.out.println("    ****    ****            ****    **");
                System.out.println("    ****  ****                ****  **");
                System.out.println("  **********                    ********");
                System.out.println("  **********  ****        ****  ********");
                System.out.println("************      **    **      ********");
                System.out.println("************      **    **      ********");
                System.out.println("************      **    **    **********");
                System.out.println("**************    ********    **********");
                System.out.println("  ************        **      ********");
                System.out.println("    ************    ******  **********");
                System.out.println("    ****************      ************");
                System.out.println("      **************      **********");
                System.out.println("        **************************");
                System.out.println("            ********************");
                System.out.println("                **************");
                System.out.println("                  **********");
                System.out.println("                      ****");
                System.out.println();
                System.out.println("                         _.----~~~~~~-----..__");
                System.out.println("                  __..------~~~~-     .._     ~~-.");
                System.out.println("     ___.--.--~~~~     --~~~~---~ __  ~~----.__   ~~~~~~~---...._____");
                System.out.println("(~~~~_..----~       ~~--=< O >- .----. -< O >=--~~             ..   .)");
                System.out.println(" ~-..__..--         ..  ___-----_...__-----___        _.  ~-=___..-~");
                System.out.println("         `   _    ..   (     \" :_.}{._; \" \"   )      _-     '");
                System.out.println("          \\   ~~-      `   \" \" __###__  \"\"    '    -~     .'");
                System.out.println("           `-._  ~-.    _`--~~~VvvvvVV~~---'_     ~..    _.");
                System.out.println("               -.     `~##\\(            )/###~' .     _.~");
                System.out.println("                 -    `.###\\#    {     #/####.'    _-~");
                System.out.println("                  -_    -####    !     #####-  ..");
                System.out.println("                    -._  ~.###   }     ###-~ ___.-~");
                System.out.println("                       ~-  \\##  / \"   ##.~ /~");
                System.out.println("                         \\ |###  \"   ###' /");
                System.out.println("                          \\`/\\#######/\\' ;");
                System.out.println("                           ~-.^^^^^^^ .-~");
                System.out.println("                              ~~~~~~~~");
            }
        }

        System.out.println("Welcome, " + name + "!");
        System.out.println("Role: " + role);
        System.out.println("Project: " + project);
        System.out.println(message);
        System.out.println("\n---"); // Separator for better readability

        // --- Your Website and Blog Links (as text) ---
        System.out.println("Connect with Kevin Online:");
        System.out.println("My Website: https://kvnbbg-creations.io");
        System.out.println("My Blog:    https://kvnbbg.fr");
        System.out.println("---\n"); // Separator

        // --- Interactive Elements (User Input) ---
        // Create a Scanner object to read input from the console
        Scanner scanner = new Scanner(System.in);

        System.out.println("What's your favorite type of story (e.g., Sci-Fi, Fantasy, Thriller)?");
        String favoriteStoryType = scanner.nextLine(); // Read user's input

        System.out.println("That's great! You enjoy " + favoriteStoryType + " stories.");

        System.out.println("What's one question you have for Kevin?");
        String userQuestion = scanner.nextLine(); // Read user's input

        System.out.println("Thanks for your question: \"" + userQuestion + "\"");
        System.out.println("Kevin will get back to you... (in this console demo, just kidding!)\n");

        System.out.println("To interact further, copy the links above into your browser!");

        // Close the scanner to release system resources
        scanner.close();
    }
}
