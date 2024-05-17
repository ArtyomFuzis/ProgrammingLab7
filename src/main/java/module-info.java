module com.fuzis.proglab {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;
    opens  com.fuzis.proglab to javafx.fxml;
    opens  com.fuzis.proglab.GUI to javafx.fxml;
    exports  com.fuzis.proglab;
    exports  com.fuzis.proglab.Client;
    exports  com.fuzis.proglab.GUI;
}
