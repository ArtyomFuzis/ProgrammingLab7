module com.fuzis.proglab {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    requires java.desktop;
    opens  com.fuzis.proglab to javafx.fxml;
    opens  com.fuzis.proglab.GUI to javafx.fxml;
    exports  com.fuzis.proglab;
    exports  com.fuzis.proglab.Client;
    exports  com.fuzis.proglab.GUI;
    exports com.fuzis.proglab.Localization;
    opens com.fuzis.proglab.Localization to javafx.fxml;
}
