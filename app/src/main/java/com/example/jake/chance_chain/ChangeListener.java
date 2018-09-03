package com.example.jake.chance_chain;

public class ChangeListener {
    private boolean boo = false;
    private ChangeListener listener;

    public boolean isBoo() {
        return boo;
    }

    public void setBoo(boolean boo) {
        this.boo = boo;
        if (listener != null) listener.onChange();
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void onChange(){

    }


}
