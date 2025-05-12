import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SignUpUI extends AuthenticationView{

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtBio;
    private JButton btnRegister;
    private JButton btnUploadPhoto;
    private JButton btnSignIn;


    public SignUpUI() {
        super();
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();
        if (controller.doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (username.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }else if(username.length() > 255 || password.length() > 255)
        {
            // this literally handles all cases of sql input being too long
            JOptionPane.showMessageDialog(this, "Username and password cannot be longer than 255 characters.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            controller.saveCredentials(username, password, bio);
            handleProfilePictureUpload();
            dispose();
    
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI();
            signInFrame.setVisible(true);
        });
        }
    }

     private void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            controller.saveProfilePicture(selectedFile, txtUsername.getText());
        }
    }
        
    private void openSignInUI() {
        dispose();

        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI();
            signInFrame.setVisible(true);
        });
    }

    @Override
    protected Component[] createInfoFormComponents() {
        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtBio = new JTextField("Bio");
        txtBio.setForeground(Color.GRAY);
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        btnUploadPhoto = new JButton("Upload Photo");
        
        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });
        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(btnUploadPhoto);
        
        Component[] c = {txtUsername, txtPassword, txtBio, photoUploadPanel};

        return c;
    }

    @Override
    protected JButton createActionButton() {
        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);
        return btnRegister;
    }

    @Override
    protected JButton createButtonToNavigateToAlternativeAuthenticationView() {
        btnSignIn = new JButton("Already have an account? Sign In");
        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignInUI();
            }
        });

        return btnSignIn;
    }

}