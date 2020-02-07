package com.e.mytravel4;

public class DAO {
    // static variable single_instance of type Singleton
    private static DAO single_instance = null;

    private Container container;

    // private constructor restricted to this class itself
    private DAO()
    {
        container = new Container();
    }


    // static method to create instance of Singleton class
    public static DAO getInstance()
    {
        if (single_instance == null)
            single_instance = new DAO();

        return single_instance;
    }
}
