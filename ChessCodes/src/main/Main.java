package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    private static JFrame mainFrame;
    private static ChessLearningPlatform platform;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowMainMenu();
            showSplashScreen();
        });
    }

    private static void showSplashScreen() {
        try {
            BufferedImage splashImage = ResourceLoader.loadImage("/res/backgrounds/splash.png.png");
            JWindow splashWindow = new JWindow();
            splashWindow.getContentPane().add(new JLabel(new ImageIcon(splashImage)));
            splashWindow.pack();
            splashWindow.setLocationRelativeTo(null);
            splashWindow.setVisible(true);

            // Hide splash and show main frame after 2 seconds
            Timer timer = new Timer(2000, e -> {
                splashWindow.dispose();
                mainFrame.setVisible(true); // Show the main frame after splash screen
            });
            timer.setRepeats(false);
            timer.start();
        } catch (IOException e) {
            System.err.println("Could not load splash screen: " + e.getMessage());
            mainFrame.setVisible(true); // Show main frame even if splash screen fails
        }
    }

    private static void createAndShowMainMenu() {
        mainFrame = new JFrame("Chess Learning Platform");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        platform = new ChessLearningPlatform(mainFrame);
        mainFrame.setContentPane(platform);
        // Don't make it visible yet - will be made visible after splash screen
    }

    // This method will be called from ChessLearningPlatform to start your existing chess game
    public static void startExistingChessGame(boolean isBot, String color, String level) {
        JFrame gameFrame = new JFrame("Chess Game");
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ChessGame game = new ChessGame();
        boolean whitePerspective = !"Black".equals(color);
        if(color.equals("White")){game.setBotParameters(isBot, "Black", level);}
        else if(color.equals("Black")){game.setBotParameters(isBot, "White", level);}
        else{game.setBotParameters(isBot, color, level);}
        GamePanel gp = new GamePanel(game, whitePerspective);
        gameFrame.add(gp);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null); // Center the game window
        gameFrame.setVisible(true);
        if (mainFrame != null) {
            mainFrame.setVisible(false);
        }

        gp.launchGame();
    }

    public static void returnToMainMenu() {
        if (mainFrame == null) {
            createAndShowMainMenu();
        }
        mainFrame.setVisible(true);
        mainFrame.toFront();
        mainFrame.requestFocus();
    }
}
/*package main;
import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

// vimport main.ChessGame;

public class Main
{
    public static void main(String args[])
    {
        JFrame frame  = new JFrame("Chess Game :");
        //frame.setResizable(false);
        ChessGame game = new ChessGame();
        //MouseClickListener mouse = new MouseClickListener(game);
        GamePanel gp = new GamePanel(game);
        frame.add(gp);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        gp.launchGame();
        //game.play();
    }
}*/

/*public class Main {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Chess Game :");
        ChessGame game = new ChessGame();
        GamePanel gp = new GamePanel(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Use BorderLayout for easier component positioning

        frame.add(gp, BorderLayout.CENTER); // Add the game panel to the center

        // Create the button
        JButton helloButton = new JButton("Click Me");
        helloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello World");
            }
        });

        frame.add(helloButton, BorderLayout.EAST); // Add button at the bottom

        frame.pack();
        frame.setVisible(true);
        gp.launchGame();
    }
}*/

/*public class Main {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Chess Game :");
        ChessGame game = new ChessGame();
        GamePanel gp = new GamePanel(game);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Use BorderLayout for positioning

        // Add the game panel to the center
        frame.add(gp, BorderLayout.CENTER);

        // Create the "undo" button with an icon
        JButton undoButton = new JButton();
        undoButton.setPreferredSize(new Dimension(50, 50)); // Make it small

        // Set icon for the button (replace with your "undo" icon path)
        try {
            ImageIcon undoIcon = new ImageIcon("src/res/icons/undo.png"); // Replace with actual icon path
            Image scaledIcon = undoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            undoButton.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            e.printStackTrace();
            undoButton.setText("Undo"); // Fallback if icon is missing
        }

        // Add action listener to print "undo" when clicked
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Undo");
            }
        });

        // Panel for right-side controls
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(undoButton); // Add the undo button to the panel

        // Add the right panel to the frame
        frame.add(rightPanel, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
        gp.launchGame();
    }
}*/

/*public class Main {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Chess Game :");
        frame.setSize(900, 800); // Set the frame size to ensure space for custom positioning
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null); // Disable layout manager for absolute positioning

        // Initialize and add the game panel (your chessboard)
        ChessGame game = new ChessGame();
        GamePanel gp = new GamePanel(game);
        gp.setBounds(0, 0, 800, 800); // Place the game panel at (0,0) with 800x800 size
        frame.add(gp);

        // Create and position the "undo" button
        JButton undoButton = new JButton();
        undoButton.setBounds(820, 350, 50, 50); // Position at (820, 350) with a size of 50x50

        // Set icon for the button (replace with your "undo" icon path)
        try {
            ImageIcon undoIcon = new ImageIcon("src/res/icons/undo.png"); // Replace with actual icon path
            Image scaledIcon = undoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            undoButton.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            e.printStackTrace();
            undoButton.setText("Undo"); // Fallback if icon is missing
        }

        // Add action listener to print "undo" when clicked
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Undo");
            }
        });

        // Add button to the frame
        frame.add(undoButton);

        frame.setVisible(true);
        gp.launchGame();
    }
}*/
