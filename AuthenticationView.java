import javax.swing.*;
import java.awt.*;

public abstract class AuthenticationView extends JFrame {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;
    protected AuthenticationController controller;
    AuthenticationView()
    {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        this.controller = new AuthenticationController();
        initializeUI();
    }

    final private void initializeUI()
    {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51));
        JLabel lblRegister = new JLabel("Quackstagram 🐥");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE);
        headerPanel.add(lblRegister);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 40));

        JLabel lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel();
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        for (Component c: createInfoFormComponents()) {
            fieldsPanel.add(Box.createVerticalStrut(10));
            fieldsPanel.add(c);
        }

        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        JButton actionButton = createActionButton();
        actionButton.setBackground(new Color(255, 90, 95));
        actionButton.setForeground(Color.BLACK); 
        actionButton.setFocusPainted(false);
        actionButton.setBorderPainted(false);
        actionButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(Color.WHITE);
        registerPanel.add(actionButton, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);

        // New button for navigating to other authentication flow (Sign In -> Sign Up and vice versa)
        JButton altViewButton = createButtonToNavigateToAlternativeAuthenticationView();
        altViewButton.setBackground(Color.WHITE);
        altViewButton.setForeground(Color.BLACK);
        altViewButton.setFocusPainted(false);
        altViewButton.setBorderPainted(false);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(actionButton);
        buttonPanel.add(altViewButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    abstract protected Component[] createInfoFormComponents();
    abstract protected JButton createActionButton();
    abstract protected JButton createButtonToNavigateToAlternativeAuthenticationView();
}
