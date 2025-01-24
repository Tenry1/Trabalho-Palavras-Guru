import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUI {
    private JFrame frame = null;
    //private JPanel main_panel = null;

    public GUI(int width, int height, Color []color_pallete){
        this.frame = new JFrame();
        //this.main_panel = new JPanel();

        this.frame.setSize(width, height);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle("Palavras Guru");

        this.frame.setResizable(false);
        this.frame.setLayout(null);

        this.frame.setVisible(true);

    }

    public void buildLevel(List<String> selectedWords, Boolean[] isShown){
        // Painel superior (1)
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setBounds(0, 0, 640, 100);  // Definir a posição e o tamanho
        topPanel.add(new JLabel("Painel Superior"));
        this.frame.add(topPanel);

        // Painel central grande (2)
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(0, 100, 640, 600);  // Definir a posição e o tamanho
        mainPanel.setLayout(null);  // Definir o layout como nulo para usar setBounds nos componentes internos

        // Adicionar um retângulo ao painel principal
        JPanel rectanglePanel = new JPanel();
        rectanglePanel.setBounds(30, 30, 580, 400);  // Posição e tamanho do retângulo
        rectanglePanel.setBackground(Color.BLUE);

        // Usar GridBagLayout para distribuir as palavras
        rectanglePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Definir os insets para afastar mais as palavras dos cantos
        int inset = 50;  // Aumentar o valor para dar mais espaço

        // Palavras nos cantos
        for (int i = 0; i < 4; i++) {

            JLabel wordLabel = new JLabel("", JLabel.CENTER);
            if (isShown[i]){
                wordLabel.setText(selectedWords.get(i));

            }
            wordLabel.setForeground(Color.WHITE);  // Deixar o texto visível no fundo azul

            // Configurar as restrições do GridBagLayout
            gbc.insets = new Insets(inset, inset, inset, inset); // Afasta mais as palavras das bordas
            gbc.gridx = (i % 2);  // Primeira coluna ou segunda
            gbc.gridy = (i / 2);  // Primeira linha ou segunda

            rectanglePanel.add(wordLabel, gbc);
        }

        mainPanel.add(rectanglePanel);
        this.frame.add(mainPanel);

        // Campo de texto (3)
        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.GRAY);
        textPanel.setBounds(0, 700, 640, 100);  // Posição e tamanho
        JTextField textField = new JTextField(20);
        textPanel.add(new JLabel("Texto:"));
        textPanel.add(textField);
        this.frame.add(textPanel);

        // Painel inferior (4)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.DARK_GRAY);
        bottomPanel.setBounds(0, 800, 640, 324);  // Ajustar o tamanho para 100
        bottomPanel.add(new JLabel("Painel Inferior"));
        this.frame.add(bottomPanel);

        // Tornar visível
        this.frame.setVisible(true);

    }

}
