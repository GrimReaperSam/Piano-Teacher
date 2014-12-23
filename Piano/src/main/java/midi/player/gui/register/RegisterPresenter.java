package midi.player.gui.register;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import midi.common.security.SecurityService;
import midi.common.security.User;
import midi.common.security.UserRole;
import midi.common.service.MidiService;
import midi.player.gui.main.MainPresenter;
import org.springframework.security.authentication.BadCredentialsException;

import javax.inject.Inject;

public class RegisterPresenter {

    @FXML private BorderPane root;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button submitButton;
    @FXML private Label statusText;

    @Inject private SecurityService securityService;
    @Inject private MidiService midiService;
    @Inject private MainPresenter mainPresenter;

    public Parent getView() {
        return root;
    }

    @FXML
    private void login(ActionEvent event) {
        statusText.setText(null);
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final Task loginTask = new Task() {
            protected Void call() throws Exception {
                User user = new User(username, password, true);
                UserRole role = new UserRole(user, "ROLE_USER");
                user.getUserRole().add(role);
                securityService.registerNewUserAccount(user);
                return null;
            }
        };
        loginTask.stateProperty().addListener((source, oldState, newState) -> {
            if (newState.equals(Worker.State.SUCCEEDED)) {
                usernameField.clear();
                passwordField.clear();
                mainPresenter.showChooser(midiService.getCurrentUserMidis());
            } else if (newState.equals(Worker.State.FAILED)) {
                Throwable exception = loginTask.getException();
                if (exception instanceof BadCredentialsException) {
                    statusText.setText("Invalid username or password");
                } else {
                    statusText.setText("Login error: " + exception);
                }
            }
        });
        new Thread(loginTask).start();
    }

    @FXML
    private void initialize(){
        usernameField.textProperty().addListener(observable -> {
            if (securityService.existsByName(usernameField.getText())) {
                statusText.setText("Username exists");
                submitButton.setDisable(true);
            } else {
                statusText.setText("");
                submitButton.setDisable(false);
            }
        });
    }
}
