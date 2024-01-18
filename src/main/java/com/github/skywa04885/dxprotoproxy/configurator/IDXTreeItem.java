package com.github.skywa04885.dxprotoproxy.configurator;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

public interface IDXTreeItem {
    Node treeItemGraphic();

    ObservableValue<String> treeItemText();

    Object value();
}
