package midi.player.gui.login;

import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import midi.common.security.SecurityService;
import midi.player.gui.main.MainPresenter;
import org.springframework.security.authentication.BadCredentialsException;

import javax.inject.Inject;

public class LoginPresenter {

    @FXML private BorderPane root;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusText;

    @Inject private SecurityService securityService;
    @Inject private MainPresenter mainPresenter;

    public Node getView() {
        return root;
    }

    @FXML
    private void login(ActionEvent event)
    {
        statusText.setText(null);
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final Task loginTask = new Task() {
            protected Void call() throws Exception {
                securityService.login(username, password);
                return null;
            }
        };
        loginTask.stateProperty().addListener((source, oldState, newState) -> {
            if (newState.equals(Worker.State.SUCCEEDED)) {
                usernameField.clear();
                passwordField.clear();
                mainPresenter.showChooser(securityService.getCurrentUser().getMidis());
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

}
