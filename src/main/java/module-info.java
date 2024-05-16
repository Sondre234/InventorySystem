module org.obj.eksamenobj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.slf4j;

    opens org.obj.eksamenobj to javafx.fxml;
    exports org.obj.eksamenobj;
}