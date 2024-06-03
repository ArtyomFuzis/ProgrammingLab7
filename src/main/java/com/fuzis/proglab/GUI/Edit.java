package com.fuzis.proglab.GUI;

import com.fuzis.proglab.Client.ClientExecutionModule;
import com.fuzis.proglab.DefaultCartoonPersonCharacter;
import com.fuzis.proglab.Enums.Opinion;
import com.fuzis.proglab.Enums.Popularity;
import com.fuzis.proglab.Enums.Sex;
import com.fuzis.proglab.Exception.NoRootsException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Edit {
    @FXML
    private TextField fld_id;
    @FXML
    private TextField fld_name;
    @FXML
    private ChoiceBox<Sex> ch_sex;
    @FXML
    private TextField fld_quote;
    @FXML
    private TextField fld_height;
    @FXML
    private TextField fld_weight;
    @FXML
    private ChoiceBox<Popularity> ch_popularity;
    @FXML
    private TextField fld_age;
    @FXML
    private TextField fld_description;
    @FXML
    private TextField fld_health;
    @FXML
    private CheckBox chk_anime;
    @FXML
    private TextField fld_names;
    @FXML
    private TextField fld_opinions;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_remove;
    @FXML
    private Button btn_img;
    @FXML
    private void initialize() {
        ch_sex.getItems().addAll(Sex.values());
        ch_popularity.getItems().addAll(Popularity.values());
        fld_id.setDisable(true);
        var listener_name = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]*\\s*")) {
                    fld_name.setText(oldValue);
                }
            }
        };
        var listener_quote = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(?:\\s*[a-zA-Zа-яА-Яё,Ё_.?\"'`!#@$0-9]+\\s*)*")) {
                    fld_quote.setText(oldValue);
                }
            }
        };
        var listener_height = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(([0-9]*\\.[0-9]*)|([0-9]*))")) {
                    fld_height.setText(oldValue);
                }
            }
        };
        var listener_weight = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(([0-9]*\\.[0-9]*)|([0-9]*))")) {
                    fld_weight.setText(oldValue);
                }
            }
        };
        var listener_age = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(([0-9]*\\.[0-9]*)|([0-9]*))")) {
                    fld_age.setText(oldValue);
                }
            }
        };
        var listener_description = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(?:\\s*[\\-a-zA-Zа-яА-ЯёЁ_,.?\"'`!#@$0-9]+\\s*)*")) {
                    fld_description.setText(oldValue);
                }
            }
        };
        var listener_health = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("[0-9]*")) {
                    fld_health.setText(oldValue);
                }
            }
        };
        var listener_names = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*,\\s*)*([a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*)?")) {
                    fld_names.setText(oldValue);
                }
            }
        };
        var listener_opinions = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("(?:\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*(:)?\\s*[a-zA-Z]*\\s*,\\s*)*([a-zA-Zа-яА-ЯёЁ_!#@$0-9]+\\s*(:)?\\s*[a-zA-Z]*\\s*)?")) {
                    fld_opinions.setText(oldValue);
                }
            }
        };
        fld_height.textProperty().addListener(listener_height);
        fld_weight.textProperty().addListener(listener_weight);
        fld_age.textProperty().addListener(listener_age);
        fld_health.textProperty().addListener(listener_health);
        fld_names.textProperty().addListener(listener_names);
        fld_name.textProperty().addListener(listener_name);
        fld_description.textProperty().addListener(listener_description);
        fld_quote.textProperty().addListener(listener_quote);
        fld_opinions.textProperty().addListener(listener_opinions);
        DefaultCartoonPersonCharacter charac;
        synchronized (Home.clicked_obj) {
            charac = Home.clicked_obj;
        }
        if(charac == null)
        {
            System.out.println("In is null!!!");
            return;
        }
        fld_id.setText(charac.getId());
        fld_name.setText(charac.getName());
        fld_description.setText(charac.getDescription());
        fld_quote.setText(charac.getQuote());
        fld_weight.setText(SON(charac.getWeight()));
        fld_age.setText(SON(charac.getAge()));
        fld_height.setText(SON(charac.getHeight()));
        fld_health.setText(SON(charac.getHealth()));
        ch_sex.setValue(charac.getSex());
        ch_popularity.setValue(charac.getPopularity());
        if (charac.getAdditionalNames() != null) fld_names.setText(String.join(", ", charac.getAdditionalNames()));
        else fld_names.setText("");
        StringBuilder sb_opinions = new StringBuilder();
        for (var el : charac.getOpinions().keySet()) {
            sb_opinions.append(el).append(" : ").append(charac.getOpinions().get(el)).append(",");
        }
        if (!sb_opinions.isEmpty())
            sb_opinions.deleteCharAt(sb_opinions.length() - 1);
        fld_opinions.setText(sb_opinions.toString());
        if(Home.imgss.containsKey(charac.getId()) && Home.imgss.get(charac.getId()).getWidth() != 0)
        {
            System.out.println(charac.getId());
            btn_img.setText(resources.getString("edit.img.del"));
        }
    }
    private String SON(Object obj) {
        if (obj == null) return null;
        else return obj.toString();
    }
    @FXML
    private void update_clicked() {
        var charac = getCharacter();
        var res = ClientExecutionModule.request_update(charac);
        if (res == null) {}
        else if (res.getClass() == NoRootsException.class) show_alert(Alert.AlertType.ERROR,resources.getString("auth.problem"));
        else show_alert(Alert.AlertType.ERROR,resources.getString("error.unknown"));
        ((Stage)btn_update.getScene().getWindow()).close();
    }
    private Double DON(String s) {
        if (s == null || s.isEmpty()) return null;
        return Double.parseDouble(s.trim());
    }
    private Integer ION(String s) {
        if (s == null || s.isEmpty()) return null;
        return Integer.parseInt(s.trim());
    }
    @FXML
    private ResourceBundle resources;
    private void show_alert(Alert.AlertType type, String msg)
    {
        Alert alert = new Alert(type);
        alert.setTitle("ERROR");
        alert.setHeaderText(resources.getString(type.toString().toLowerCase()));
        alert.setContentText(msg);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image(GuiApp.class.getResource("icon.png").openStream()));
        }
        catch (IOException e) {}
        alert.show();
    }
    private DefaultCartoonPersonCharacter getCharacter() {
        String id = fld_id.getText().trim();
        String name = fld_name.getText().trim();
        String description = fld_description.getText().trim();
        String quote = fld_quote.getText().trim();
        Double weight = DON(fld_weight.getText());
        Double age = DON(fld_age.getText());
        Double height = DON(fld_height.getText());
        Integer health = ION(fld_health.getText());
        Sex sex = ch_sex.getValue();
        Popularity popularity = ch_popularity.getValue();
        Boolean isanime = chk_anime.isSelected();
        var additionalnames = new ArrayList<>(Arrays.stream(fld_names.getText().trim().split("\\s*,\\s*")).toList());
        if (additionalnames.get(additionalnames.size() - 1).isEmpty())
            additionalnames.remove(additionalnames.get(additionalnames.size() - 1));
        var opinions = new HashMap<String, Opinion>();
        var pre_data = fld_opinions.getText().trim().split("\\s*,\\s*");
        var skipped_values = new ArrayList<String>();
        for (var el : pre_data) {
            var separated = el.split("\\s*:\\s*");
            try {
                opinions.put(separated[0], Opinion.valueOf(separated[1].substring(0, 1).toUpperCase() + separated[1].substring(1).toLowerCase(Locale.ROOT)));
            } catch (Exception e) {
                skipped_values.add(el);
            }
        }
        return new DefaultCartoonPersonCharacter(id, name, sex, quote, opinions, additionalnames, height, weight, isanime, popularity, description, age, health);
    }
    @FXML
    private void remove_clicked() {
        var charac = getCharacter();
        var res = ClientExecutionModule.request_delete(charac);
        if (res == null) {}
        else if (res.getClass() == NoRootsException.class) show_alert(Alert.AlertType.ERROR,resources.getString("auth.problem"));
        else show_alert(Alert.AlertType.ERROR,resources.getString("error.unknown"));
        ((Stage)btn_remove.getScene().getWindow()).close();
    }
    @FXML
    private void img_clicked() {

        var charac = getCharacter();
        if(Home.imgss.containsKey(charac.getId()) && Home.imgss.get(charac.getId()).getWidth() != 0)
        {
            Home.imgss.put(charac.getId(), new Image(new ByteArrayInputStream(new byte[]{})));
            Home.redraw.run();
            ((Stage)btn_img.getScene().getWindow()).close();
        }
        else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(resources.getString("fc.home.img"));
            //fileChooser.setInitialFileName(charac.getId()+".png");
            String s = GuiApp.class.getResource("icon.png").getFile();
            s = s.substring(0, s.length() - 8);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(resources.getString("imgs"), "*.jpg", "*.png", "*.gif", "*.bmp", "*.jpeg"));
            fileChooser.setInitialDirectory(new File(s));
            File f = fileChooser.showOpenDialog((Stage) btn_img.getScene().getWindow());
            if (f == null) return;
            synchronized (Home.imgss) {
                System.out.println(f.getAbsolutePath());
                try {
                    Home.imgss.put(charac.getId(), new Image(new FileInputStream(f), Home.w - 2 * Home.img_inset_x, Home.h - Home.img_y - 2 * Home.img_inset_y, false, true));
                } catch (IOException ex) {
                    System.out.println("Can't open the file");
                }
            }
            Home.redraw.run();
            ((Stage) btn_img.getScene().getWindow()).close();
        }
    }
}
