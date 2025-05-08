import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignInUI extends AuthenticationView {

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;

    public SignInUI() {
        super();
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();
        VerificationResult result;
        if ((result = controller.verifyCredentials(enteredUsername, enteredPassword)).getSuccess()) {
            dispose();

            SwingUtilities.invokeLater(() -> {
                InstagramProfileUI profileUI = new InstagramProfileUI(result.getNewUser());
                profileUI.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        dispose();

        SwingUtilities.invokeLater(() -> {
            SignUpUI signUpFrame = new SignUpUI();
            signUpFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI();
            frame.setVisible(true);
        });
    }

    @Override
    protected JButton createActionButton() {
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);
        return btnSignIn;
    }

    @Override
    protected JButton createButtonToNavigateToAlternativeAuthenticationView() {
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);
        return btnRegisterNow;
    }

    @Override
    protected Component[] createInfoFormComponents() {
        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        Component[] c = { txtUsername,
                txtPassword };

        return c;
    }
}

