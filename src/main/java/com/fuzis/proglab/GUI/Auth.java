package com.fuzis.proglab.GUI;

import com.fuzis.proglab.Client.ClientExecutionModule;
import com.fuzis.proglab.Client.ClientLogger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;
import java.util.zip.CheckedInputStream;

public class Auth
{
    final ClientLogger log = ClientLogger.getInstance();
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label lbl_feedback_head_auth;
    @FXML
    private Label lbl_feedback_body_auth;
    @FXML
    private AnchorPane anchor_msg_auth;
    @FXML
    private PasswordField fld_password;
    @FXML
    private TextField fld_username;
    @FXML
    private ImageView img_res_auth;

    @FXML
    private void submit()
    {
        if(!ClientExecutionModule.request_auth(fld_username.getText(), fld_password.getText())) {
            var img = Auth.class.getResourceAsStream("error.png");
            if(img != null) {
                img_res_auth.setImage(new Image(img));
                lbl_feedback_head_auth.setText(resources.getString("error"));
                lbl_feedback_body_auth.setText(resources.getString("auth.error.body"));
                anchor_msg_auth.setVisible(true);
            }
            else
            {
                log.error("No image error.png");
                System.exit(1);
            }

        }
        else
        {
            var img = Auth.class.getResourceAsStream("OK.png");
            if(img != null) {
                img_res_auth.setImage(new Image(img));
                lbl_feedback_head_auth.setText(resources.getString("success"));
                lbl_feedback_body_auth.setText(resources.getString("auth.success.body"));
                anchor_msg_auth.setVisible(true);
                MainProperties.lbl_auth_as.setText(resources.getString("auth.as")+" "+fld_username.getText());
            }
            else
            {
                log.error("No image OK.png");
                System.exit(1);
            }

        }
    }
}
