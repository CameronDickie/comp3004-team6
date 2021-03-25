package com.comp3004.educationmanager.decorator;

import com.comp3004.educationmanager.composite.Component;

public abstract class Decorator implements Component {
    protected Component wrappee;

    /*
    Constructor
     */
    public Decorator(Component c) {
        wrappee = c;
    }

    /*
    Getters
     */
    public Component getWrappee() {
        return wrappee;
    }
}
