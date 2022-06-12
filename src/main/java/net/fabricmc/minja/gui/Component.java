package net.fabricmc.minja.gui;

import io.github.cottonmc.cotton.gui.widget.WWidget;

public class Component {
    private int[] coords;
    private WWidget widget;

    public Component(WWidget widget, int x, int y, int w, int h){
        this.coords = new int[4];
        this.coords[0] = x;
        this.coords[1] = y;
        this.coords[2] = w;
        this.coords[3] = h;
        this.widget = widget;
    }

    public int[] getCoords(){
        return this.coords;
    }

    public WWidget getWidget(){
        return this.widget;
    }

    public void setWidget(WWidget widget){
        this.widget = widget;
    }
}
