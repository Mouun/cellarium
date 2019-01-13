package classes;

import javafx.scene.paint.Paint;
import util.CellariumUtil;

public enum Couleurs {
    Rouge(Paint.valueOf("#c00000")),
    Blanc(Paint.valueOf("#fdeadb")),
    Rose(Paint.valueOf("#ff9b9b")),
    Autres(Paint.valueOf("#cccccc"));

    private Paint color;

    Couleurs(Paint c)
    {
        color = c;
    }

    public static final int size = values().length;
    private static final String[] names = new String[size];

    static
    {
        for (Couleurs c : values()) names[c.ordinal()] = c.toString();
    }

    public static final String[] SORTED_STRING_ARRAY = CellariumUtil.sortClearBOM(toStringArray());

    public static String[] toStringArray() {
        Couleurs[] coul = values();
        String[] str = new String[size];

        for (int i = 0; i < size; i++) str[i] = coul[i].toString();

        return str;
    }

    public Paint getColor()
    {
        return color;
    }

    public static Couleurs fromString(String name)
    {
        for (int i=0;i<size;i++)
        {
            if (names[i].equalsIgnoreCase(name)) return Couleurs.values()[i];
        }

        return Autres;
    }
}
