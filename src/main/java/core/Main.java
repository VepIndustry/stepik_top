package core;

public class Main {

    public static void main(String[] args) {
        try {
            int n = Integer.parseInt(args[0]);
            Core core = new Core();
            core.run(n);
        } catch (NumberFormatException e) {
            System.err.println("Couldn't parse integer argument.");
        }
    }

}
