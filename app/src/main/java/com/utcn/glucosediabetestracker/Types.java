package com.utcn.glucosediabetestracker;

import java.util.ArrayList;

public class Types {

    private static ArrayList<Types> typesArrayList = new ArrayList<>();
    private String id, type;

    public Types(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public static void initTypes()
    {
        Types types1 = new Types("0", "Out of Bed");
        typesArrayList.add(types1);
        Types types2 = new Types("1", "Before Bed");
        typesArrayList.add(types2);
        Types types3 = new Types("2", "Before Breakfast");
        typesArrayList.add(types3);
        Types types4 = new Types("3", "After Breakfast");
        typesArrayList.add(types4);
        Types types5 = new Types("4", "Before Lunch");
        typesArrayList.add(types5);
        Types types6 = new Types("5", "After Lunch");
        typesArrayList.add(types6);
        Types types7 = new Types("6", "Before dinner");
        typesArrayList.add(types7);
        Types types8 = new Types("7", "After dinner");
        typesArrayList.add(types8);

    }
    public int getImage()
    {
        switch (getId())
        {
            case "0":
                return R.drawable.v1;
            case "1":
                return R.drawable.v2;
            case "2":
                return R.drawable.v3;
            case "3":
                return R.drawable.v4;
            case "4":
                return R.drawable.v5;
            case "5":
                return R.drawable.v6;
            case "6":
                return R.drawable.v7;
            case "7":
                return R.drawable.v8;

        }
        return R.drawable.v1;
    }

    public static ArrayList<Types> getTypesArrayList() {
        return typesArrayList;
    }
}
