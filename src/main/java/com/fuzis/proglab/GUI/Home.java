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
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.text.StyledEditorKit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Home {

    @FXML
    private TableView<DefaultCartoonPersonCharacter> tbl;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, String> col_name;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Double> col_age;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, String> col_quote;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, String> col_id;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Sex> col_sex;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Double> col_height;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Double> col_weight;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Popularity> col_popularity;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, String> col_description;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Integer> col_health;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Boolean> col_anime;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, List<String>> col_names;
    @FXML
    private TableColumn<DefaultCartoonPersonCharacter, Map<String, Opinion>> col_opinions;
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
    private Button btn_add;
    @FXML
    private Button btn_update;
    @FXML
    private Button btn_remove;
    @FXML
    private Button btn_filter;

    private void change_tbl_btns(boolean flag) {
        if (flag) {
            btn_add.setDisable(false);
            btn_update.setDisable(true);
            btn_remove.setDisable(true);
        } else {
            btn_add.setDisable(true);
            btn_update.setDisable(false);
            btn_remove.setDisable(false);
        }
    }
    @FXML
    private void initialize() {
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_quote.setCellValueFactory(new PropertyValueFactory<>("quote"));
        col_sex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        col_height.setCellValueFactory(new PropertyValueFactory<>("height"));
        col_weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        col_popularity.setCellValueFactory(new PropertyValueFactory<>("popularity"));
        col_description.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_health.setCellValueFactory(new PropertyValueFactory<>("health"));
        col_anime.setCellValueFactory(new PropertyValueFactory<>("animeCharacter"));
        col_names.setCellValueFactory(new PropertyValueFactory<>("additionalNames"));
        col_opinions.setCellValueFactory(new PropertyValueFactory<>("opinions"));
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        ch_sex.getItems().addAll(Sex.values());
        ch_popularity.getItems().addAll(Popularity.values());
        var listener_id = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null) return;
                if (!newValue.matches("\\s*[a-zA-Zа-яА-ЯёЁ_!#@$0-9]*\\s*")) {
                    fld_id.setText(oldValue);
                    newValue = oldValue;
                }
                if (!ids.contains(newValue)) change_tbl_btns(true);
                else change_tbl_btns(false);
            }
        };
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
        fld_id.textProperty().addListener(listener_id);
        fld_name.textProperty().addListener(listener_name);
        fld_description.textProperty().addListener(listener_description);
        fld_quote.textProperty().addListener(listener_quote);
        fld_opinions.textProperty().addListener(listener_opinions);
        fill();
        ClientExecutionModule.setUpdateHandle(0,this::update_table);
        draw_canvas();
    }

    private void update_table(Object[] args) {
        System.out.println(args[0]);
        System.out.println(args[1]);
        fill();
    }

    private String SON(Object obj) {
        if (obj == null) return null;
        else return obj.toString();
    }

    @FXML
    private void row_clicked() {
        if(tbl.getSelectionModel().getSelectedItem() == null)
        {
            return;
        }
        if(!isFilterMode) {
            DefaultCartoonPersonCharacter charac = tbl.getSelectionModel().getSelectedItem();
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
        }
        else
        {
            var column = tbl.getSelectionModel().getSelectedCells().get(0).getTableColumn();
            if(column == null) return;
            switch (column.getText())
            {
                case "id":
                    filters.put("id",(x)-> Objects.equals(x.getId(), tbl.getSelectionModel().getSelectedItem().getId()));
                    break;
                case "name":
                    filters.put("name",(x)-> Objects.equals(x.getName(), tbl.getSelectionModel().getSelectedItem().getName()));
                    break;
                case "description":
                    filters.put("description",(x)-> Objects.equals(x.getDescription(), tbl.getSelectionModel().getSelectedItem().getDescription()));
                    break;
                case "quote":
                    filters.put("quote",(x)-> Objects.equals(x.getQuote(), tbl.getSelectionModel().getSelectedItem().getQuote()));
                    break;
                case "weight":
                    filters.put("weight",(x)-> Objects.equals(x.getWeight(), tbl.getSelectionModel().getSelectedItem().getWeight()));
                    break;
                case "age":
                    filters.put("age",(x)-> Objects.equals(x.getAge(), tbl.getSelectionModel().getSelectedItem().getAge()));
                    break;
                case "height":
                    filters.put("height",(x)-> Objects.equals(x.getHeight(), tbl.getSelectionModel().getSelectedItem().getHeight()));
                    break;
                case "sex":
                    filters.put("sex",(x)-> Objects.equals(x.getSex(), tbl.getSelectionModel().getSelectedItem().getSex()));
                    break;
                case "popularity":
                    filters.put("popularity",(x)-> Objects.equals(x.getPopularity(), tbl.getSelectionModel().getSelectedItem().getPopularity()));
                    break;
                case "health":
                    filters.put("health",(x)-> Objects.equals(x.getHealth(), tbl.getSelectionModel().getSelectedItem().getHealth()));
                    break;
                case "anime":
                    filters.put("anime",(x)-> Objects.equals(x.getAnimeCharacter(), tbl.getSelectionModel().getSelectedItem().getAnimeCharacter()));
                    break;
                case "names":
                    filters.put("names",(x)-> Objects.equals(x.getAdditionalNames(), tbl.getSelectionModel().getSelectedItem().getAdditionalNames()));
                    break;
                case "opinions":
                    filters.put("opinions",(x)-> Objects.equals(x.getOpinions(), tbl.getSelectionModel().getSelectedItem().getOpinions()));
                    break;
                default:
                    System.out.println("111");
            }
            fill();
        }
    }
    private HashMap<String,Predicate<DefaultCartoonPersonCharacter>> filters = new HashMap<>();
    private HashSet<String> ids = new HashSet<>();
    private void fill() {
        try {
            var col = ClientExecutionModule.request_collection_all().stream();
            for (var el : filters.values())
            {
                col = col.filter(el);
            }
            var col_collected = col.toList();
            tbl.getItems().setAll(col_collected);
            ids.clear();
            if (col != null) ids.addAll(col_collected.stream().map(DefaultCartoonPersonCharacter::getId).toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private Double DON(String s) {
        if (s == null || s.isEmpty()) return null;
        return Double.parseDouble(s.trim());
    }

    private Integer ION(String s) {
        if (s == null || s.isEmpty()) return null;
        return Integer.parseInt(s.trim());
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
    private ResourceBundle resources;
    private void show_alert(Alert.AlertType type, String msg)
    {
        Alert alert = new Alert(type);
        alert.setTitle("ERROR");
        alert.setHeaderText(resources.getString(type.toString().toLowerCase()));
        alert.setContentText(msg);
        alert.show();
    }
    private boolean show_conformation( String msg)
    {
        ButtonType yes_btn = new ButtonType(resources.getString("YES"), ButtonBar.ButtonData.YES);
        ButtonType no_btn = new ButtonType(resources.getString("NO"), ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, yes_btn,no_btn);
        Button noButton = (Button) alert.getDialogPane().lookupButton(no_btn );
        noButton.setDefaultButton( true );
        noButton.requestFocus();
        Button yesButton = (Button) alert.getDialogPane().lookupButton(yes_btn );
        yesButton.setDefaultButton( false );
        alert.setTitle("CONFORMATION");
        alert.setHeaderText(resources.getString("CONFORMATION"));
        alert.showAndWait();
        return (alert.getResult() == yes_btn);
    }
    @FXML
    private void add_clicked() {
        var charac = getCharacter();
        if (charac.getId() == null || charac.getId().isEmpty())
        {
            show_alert(Alert.AlertType.ERROR,resources.getString("id.empty"));
            return;
        }
        var res = ClientExecutionModule.request_add(charac);
        if (res == null) {
            change_tbl_btns(false);
        } else if (res.getClass() == NoRootsException.class) show_alert(Alert.AlertType.ERROR,resources.getString("auth.no"));
        else show_alert(Alert.AlertType.ERROR,resources.getString("error.unknown"));

    }

    @FXML
    private void update_clicked() {
        var charac = getCharacter();
        var res = ClientExecutionModule.request_update(charac);
        if (res == null) {}
        else if (res.getClass() == NoRootsException.class) show_alert(Alert.AlertType.ERROR,resources.getString("auth.problem"));
        else show_alert(Alert.AlertType.ERROR,resources.getString("error.unknown"));
    }
    @FXML
    private void clear_clicked() {
        if(show_conformation(resources.getString("clear.conform"))) {
            var res = ClientExecutionModule.request_clear();
            if (res == null) {
            } else if (res.getClass() == NoRootsException.class)
                show_alert(Alert.AlertType.ERROR, resources.getString("auth.problem"));
            else show_alert(Alert.AlertType.ERROR, resources.getString("error.unknown"));
        }
    }
    @FXML
    private void clfld_clicked() {;
        fld_id.clear();
        fld_name.clear();
        fld_names.clear();
        fld_description.clear();
        fld_opinions.clear();
        fld_quote.clear();
        ch_popularity.setValue(null);
        ch_sex.setValue(null);
        fld_weight.clear();
        fld_height.clear();
        fld_health.clear();
        fld_age.clear();
        chk_anime.setSelected(false);
    }

    @FXML
    private void remove_clicked() {
        var charac = getCharacter();
        var res = ClientExecutionModule.request_delete(charac);
        if (res == null) {
            change_tbl_btns(true);
        } else if (res.getClass() == NoRootsException.class) show_alert(Alert.AlertType.ERROR,resources.getString("auth.problem"));
        else show_alert(Alert.AlertType.ERROR,resources.getString("error.unknown"));
    }
    boolean isFilterMode = false;
    @FXML
    private void filter_clicked() {
        if(isFilterMode)
        {
            isFilterMode = false;
            btn_filter.setText(resources.getString("home.filter.btn"));
        }
        else
        {
            isFilterMode = true;
            btn_filter.setText(resources.getString("home.filter2.btn"));
        }
    }
    @FXML
    private void clfil_clicked() {
        filters.clear();
        fill();
    }

    @FXML
    private Canvas cnvs;
    private void draw_canvas() {
        /*var context = cnvs.getGraphicsContext2D();
        context.setFill(Color.AQUA);
        try {
            context.drawImage(new Image("resources/com/fuzis/proglab/GUI/1.jpg"), 0, 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }*/
    }
}
