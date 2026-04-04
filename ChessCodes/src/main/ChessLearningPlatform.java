package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ChessLearningPlatform extends JPanel {
    private JFrame parentFrame;

    public ChessLearningPlatform(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Set background color or image
        setBackground(new Color(232, 232, 232));

        addMenuItems();
    }

    private void addMenuItems() {
        add(Box.createVerticalGlue());

        // Add title
        JLabel titleLabel = new JLabel("Chess Learning Platform");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        // Add buttons
        addButton("Play with Friend", e -> Main.startExistingChessGame(false,"null","null"));
        addButton("Play with Bot", e -> showBotOptions());
        addButton("Load Game", e -> loadGame());
        add(Box.createVerticalGlue());
    }

    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        styleButton(button);
        button.addActionListener(listener);
        add(button);
        add(Box.createRigidArea(new Dimension(0, 20)));
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setPreferredSize(new Dimension(250, 50));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void showBotOptions() {
        String[] levels = {"Easy", "Medium", "Hard"};
        String[] colors = {"White", "Black"};

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JComboBox<String> levelBox = new JComboBox<>(levels);
        JComboBox<String> colorBox = new JComboBox<>(colors);

        panel.add(new JLabel("Select difficulty:"));
        panel.add(levelBox);
        panel.add(new JLabel("Select your color:"));
        panel.add(colorBox);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel,
                "Bot Game Settings", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String level = (String)levelBox.getSelectedItem();
            String color = (String)colorBox.getSelectedItem();
            // Implement bot game launch here
            Main.startExistingChessGame(true,color,level);
            JOptionPane.showMessageDialog(parentFrame,
                    "Starting bot game with " + level + " difficulty as " + color);
        }
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        // Set file filter to only show .pgn files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PGN Files (*.pgn)", "pgn");
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Create new chess game with the loaded PGN
                JFrame gameFrame = new JFrame("Chess Game - " + selectedFile.getName());
                gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                ChessGame game = new ChessGame();
                game.loadPGNFile(selectedFile); // You'll need to implement this method

                GamePanel gp = new GamePanel(game, true);
                gameFrame.add(gp);
                gameFrame.pack();
                gameFrame.setVisible(true);

                gp.launchGame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Error loading PGN file: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
