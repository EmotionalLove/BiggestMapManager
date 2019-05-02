package com.mai.bigmapmanager;

import com.mai.bigmapmanager.data.schematic.SchematicSection;

// 00 should be A1 and so on.
public class IdentifierTranslator {

    public static Object[] getFormatted(SchematicSection section) {
        char alpha = (char) (65 + section.x);
        return new Object[]{alpha, section.z + 1};
    }

    public static int[] getRaw(char x, int z) {
        int i = (int) (x) - 65;
        return new int[]{i, z - 1};
    }

}
