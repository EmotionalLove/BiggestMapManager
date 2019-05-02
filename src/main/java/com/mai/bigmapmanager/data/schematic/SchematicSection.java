package com.mai.bigmapmanager.data.schematic;

import com.mai.bigmapmanager.IdentifierTranslator;

import java.io.Serializable;
import java.util.Random;

public class SchematicSection implements Serializable {

    public int x;
    public int z;

    public SchematicSection(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * random schem
     */
    public SchematicSection() {
        Random random = new Random();
    }

    @Override
    public boolean equals(Object section) {
        if (section instanceof SchematicSection) {
            SchematicSection schematicSection = (SchematicSection) section;
            return this.x == schematicSection.x && this.z == schematicSection.z;
        }
        return false;
    }

    @Override
    public String toString() {
        Object[] obj = IdentifierTranslator.getFormatted(this);
        return obj[0] + ", " + obj[1];
    }

}
