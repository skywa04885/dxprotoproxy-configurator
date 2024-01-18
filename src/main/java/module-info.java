module com.github.skywa04885.dxprotoproxy.configurator {
    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;
    requires org.jetbrains.annotations;
    requires java.logging;
    requires common;

    requires net.synedra.validatorfx;
    requires java.xml;

    exports com.github.skywa04885.dxprotoproxy.configurator;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.primary to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.primary.tree to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor to javafx.fxml;
    opens com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor to javafx.fxml;
}
