/* $Id: ClassCpInfo.java,v 1.8 2002/07/04 16:16:58 eric Exp $
 *
 * ProGuard -- obfuscation and shrinking package for Java class files.
 *
 * Copyright (c) 1999 Mark Welsh (markw@retrologic.com)
 * Copyright (C) 2002 Eric Lafortune (eric@graphics.cornell.edu)
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.classfile;

import proguard.classfile.util.*;
import proguard.classfile.visitor.*;
import java.io.*;
import java.util.*;

/**
 * Representation of a 'class' entry in the ConstantPool.
 *
 * @author Mark Welsh
 * @author Eric Lafortune
 */
public class ClassCpInfo extends CpInfo
{
    public int u2nameIndex;

    /**
     * An extra field pointing to the referenced ClassFile object.
     * This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassFileInitializer ClassFileInitializer}</code>.
     * References to library class files are only filled out if they are
     * superclasses or interfaces of the class of this ClassCpInfo.
     */
    public ClassFile referencedClassFile;


    protected ClassCpInfo()
    {
        super(ClassConstants.CONSTANT_Class);
    }


    /**
     * Returns the name index.
     */
    protected int getNameIndex()
    {
        return u2nameIndex;
    }

    /**
     * Sets the name index.
     */
    protected void setNameIndex(int index)
    {
        u2nameIndex = index;
    }

    /**
     * Returns the name.
     */
    public String getName(ClassFile classFile)
    {
        return classFile.getCpString(u2nameIndex);
    }

    /**
     * Reads the 'info' data following the u1tag byte.
     */
    protected void readInfo(DataInput din) throws IOException
    {
        u2nameIndex = din.readUnsignedShort();
    }

    /**
     * Writes the 'info' data following the u1tag byte.
     */
    protected void writeInfo(DataOutput dout) throws IOException
    {
        dout.writeShort(u2nameIndex);
    }

    /**
     * Accepts the given visitor.
     */
    public void accept(ClassFile classFile, CpInfoVisitor cpInfoVisitor)
    {
        cpInfoVisitor.visitClassCpInfo(classFile, this);
    }


    /**
     * Lets the referenced class file accept the given visitor.
     */
    public void referencedClassAccept(ClassFileVisitor classFileVisitor)
    {
        if (referencedClassFile != null)
        {
            referencedClassFile.accept(classFileVisitor);
        }
    }
}