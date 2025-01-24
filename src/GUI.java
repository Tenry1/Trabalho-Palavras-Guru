import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUI {
    private JFrame frame = null;

    public GUI(int width, int height, Color []color_pallete){
        this.frame = new JFrame();

        this.frame.setSize(width, height);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Palavras Guru");

        this.frame.setResizable(false);
        this.frame.setLayout(null);

        this.frame.setVisible(true);

    }

    public void buildLevel(List<String> selectedWords, Boolean[] isShown, Color[] color_pallete){
        JPanel topPanel = new JPanel();
        topPanel.setBackground(color_pallete[3]);
        topPanel.setBounds(0, 0, 640, 100);
        this.frame.add(topPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(color_pallete[1]);
        mainPanel.setBounds(0, 100, 640, 600);
        mainPanel.setLayout(null);

        JPanel rectanglePanel = new JPanel();
        rectanglePanel.setBounds(30, 30, 580, 400);
        rectanglePanel.setBackground(color_pallete[2]);

        rectanglePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int inset = 50;

        for (int i = 0; i < 4; i++) {

            JLabel wordLabel = new JLabel("", JLabel.CENTER);
            if (isShown[i]){
                wordLabel.setText(selectedWords.get(i));

            }
            wordLabel.setForeground(color_pallete[0]);

            gbc.insets = new Insets(inset, inset, inset, inset);
            gbc.gridx = (i % 2);
            gbc.gridy = (i / 2);

            rectanglePanel.add(wordLabel, gbc);
        }

        mainPanel.add(rectanglePanel);
        this.frame.add(mainPanel);

        JPanel textPanel = new JPanel();
        textPanel.setBackground(color_pallete[1]);
        textPanel.setBounds(0, 700, 640, 100);
        JTextField textField = new JTextField(20);
        textPanel.add(textField);
        this.frame.add(textPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(color_pallete[2]);
        bottomPanel.setBounds(0, 800, 640, 324);
        this.frame.add(bottomPanel);

        this.frame.setVisible(true);

    }

}
