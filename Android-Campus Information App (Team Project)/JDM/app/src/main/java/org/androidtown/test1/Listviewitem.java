package org.androidtown.test1;

import android.util.Log;

public class Listviewitem {
    private int icon;
    private String name;
    private boolean changable;
    public int getIcon(){
        Log.d("errorlistviewitem10",String.valueOf(icon));
        return icon;}
    public String getName(){return name;}
    public boolean getChangable(){return changable;}
    public Listviewitem(int icon,String name,boolean changable){
        this.icon=icon;
        this.name=name;
        this.changable = changable;
    }
}
