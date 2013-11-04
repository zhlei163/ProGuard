/* $Id: ProgramClassFile.java,v 1.14 2002/07/04 16:16:58 eric Exp $
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


import proguard.classfile.visitor.*;
import java.io.*;
import java.util.*;

/**
 * This is a complete representation of the data in a Java class file.
 * A ClassFile instance can be generated using the static create(DataInput)
 * method, manipulated using various operators, and persisted back using the
 * write(DataOutput) method.
 *
 * @author Mark Welsh
 * @author Eric Lafortune
 */
public class ProgramClassFile implements ClassFile
{
    public int                 u4magic;
    public int                 u2minorVersion;
    public int                 u2majorVersion;
    public int                 u2constantPoolCount;
    public CpInfo[]            constantPool;
    public int                 u2accessFlags;
    public int                 u2thisClass;
    public int                 u2superClass;
    public int                 u2interfacesCount;
    public int[]               u2interfaces;
    public int                 u2fieldsCount;
    public ProgramFieldInfo[]  fields;
    public int                 u2methodsCount;
    public ProgramMethodInfo[] methods;
    public int                 u2attributesCount;
    public AttrInfo[]          attributes;

    /**
     * An extra field pointing to the subclasses of this class.
     * This field is filled out by the <code>{@link
     * proguard.classfile.util.ClassFileInitializer ClassFileInitializer}</code>.
     */
    public ClassFile[]  subClasses = null;

    /**
     * An extra field in which visitors can store information.
     */
    public Object visitorInfo;


    /**
     * Creates a new ClassFile from the class file format data in the DataInput
     * stream.
     *
     * @throws IOException if class file is corrupt or incomplete
     */
    public static ClassFile create(DataInput din) throws Exception
    {
        ProgramClassFile cf = new ProgramClassFile();
        cf.read(din);
        return cf;
    }


    /**
     * Creates an empty ProgramClassFile.
     */
    private ProgramClassFile() {}


    /**
     * Imports the class data into this ProgramClassFile.
     */
    private void read(DataInput din) throws IOException
    {
        // Read and check the class file magic number.
        u4magic = din.readInt();

        if (u4magic != ClassConstants.MAGIC)
        {
            throw new IOException("Invalid magic number in class file.");
        }

        // Read and check the class file version numbers.
        u2minorVersion = din.readUnsignedShort();
        u2majorVersion = din.readUnsignedShort();

        if (u2majorVersion < ClassConstants.MAJOR_VERSION_MIN ||
            (u2majorVersion == ClassConstants.MAJOR_VERSION_MIN &&
             u2minorVersion <  ClassConstants.MINOR_VERSION_MIN) ||
            (u2majorVersion == ClassConstants.MAJOR_VERSION_MAX &&
             u2minorVersion >  ClassConstants.MINOR_VERSION_MAX) ||
            u2majorVersion > ClassConstants.MAJOR_VERSION_MAX)
        {
            throw new IOException("Unsupported version number ["+u2majorVersion+"."+u2minorVersion+"] for class file format.");
        }

        u2constantPoolCount = din.readUnsignedShort();
        constantPool        = new CpInfo[u2constantPoolCount];
        // Fill the constant pool, recalling the zero entry
        // is not persisted, nor are the entries following a Long or Double
        for (int i = 1; i < u2constantPoolCount; i++)
        {
            constantPool[i] = CpInfo.create(din);
            if ((constantPool[i] instanceof LongCpInfo) ||
                (constantPool[i] instanceof DoubleCpInfo))
            {
                i++;
            }
        }

        u2accessFlags     = din.readUnsignedShort();
        u2thisClass       = din.readUnsignedShort();
        u2superClass      = din.readUnsignedShort();
        u2interfacesCount = din.readUnsignedShort();
        u2interfaces      = new int[u2interfacesCount];
        for (int i = 0; i < u2interfacesCount; i++)
        {
            u2interfaces[i] = din.readUnsignedShort();
        }

        u2fieldsCount = din.readUnsignedShort();
        fields        = new ProgramFieldInfo[u2fieldsCount];
        for (int i = 0; i < u2fieldsCount; i++)
        {
            fields[i] = ProgramFieldInfo.create(din, this);
        }

        u2methodsCount = din.readUnsignedShort();
        methods        = new ProgramMethodInfo[u2methodsCount];
        for (int i = 0; i < u2methodsCount; i++)
        {
            methods[i] = ProgramMethodInfo.create(din, this);
        }

        u2attributesCount = din.readUnsignedShort();
        attributes        = new AttrInfo[u2attributesCount];
        for (int i = 0; i < u2attributesCount; i++)
        {
            attributes[i] = AttrInfo.create(din, this);
        }
    }



    /**
     * Returns the CpInfo at the given index in the constant pool.
     */
    public CpInfo getCpEntry(int cpIndex)
    {
        return constantPool[cpIndex];
    }


    /**
     * Returns the field with the given name and descriptor.
     */
    ProgramFieldInfo findProgramField(String name, String descriptor)
    {
        for (int i = 0; i < u2fieldsCount; i++)
        {
            ProgramFieldInfo field = fields[i];
            if (field.getName(this).equals(name) &&
                field.getDescriptor(this).equals(descriptor))
            {
                return field;
            }
        }

        return null;
    }


    /**
     * Exports the representation to a DataOutput stream.
     */
    public void write(DataOutput dout) throws IOException
    {
        dout.writeInt(u4magic);
        dout.writeShort(u2minorVersion);
        dout.writeShort(u2majorVersion);
        dout.writeShort(u2constantPoolCount);
        for (int i = 1; i < u2constantPoolCount; i++)
        {
            CpInfo cpInfo = constantPool[i];
            if (cpInfo != null)
            {
                cpInfo.write(dout);
            }
        }
        dout.writeShort(u2accessFlags);
        dout.writeShort(u2thisClass);
        dout.writeShort(u2superClass);
        dout.writeShort(u2interfacesCount);
        for (int i = 0; i < u2interfacesCount; i++)
        {
            dout.writeShort(u2interfaces[i]);
        }
        dout.writeShort(u2fieldsCount);
        for (int i = 0; i < u2fieldsCount; i++)
        {
            fields[i].write(dout);
        }
        dout.writeShort(u2methodsCount);
        for (int i = 0; i < u2methodsCount; i++)
        {
            methods[i].write(dout);
        }
        dout.writeShort(u2attributesCount);
        for (int i = 0; i < u2attributesCount; i++)
        {
            attributes[i].write(dout);
        }
    }


    /**
     * Returns the method with the given name and descriptor.
     */
    ProgramMethodInfo findProgramMethod(String name, String descriptor)
    {
        for (int i = 0; i < u2methodsCount; i++)
        {
            ProgramMethodInfo method = methods[i];
            if (method.getName(this).equals(name) &&
                method.getDescriptor(this).equals(descriptor))
            {
                return method;
            }
        }

        return null;
    }


    /**
     * Returns the attribute specified by the given name.
     */
    AttrInfo getAttribute(String name)
    {
        for (int i = 0; i < u2attributesCount; i++)
        {
            AttrInfo attribute = attributes[i];
            if (attribute.getAttributeName(this).equals(name))
            {
                return attribute;
            }
        }

        return null;
    }


    // Implementations for ClassFile

    public int getAccessFlags()
    {
        return u2accessFlags;
    }

    public String getName()
    {
        return getCpClassNameString(u2thisClass);
    }

    public String getSuperName()
    {
        return u2superClass == 0 ? null : getCpClassNameString(u2superClass);
    }

    public int getCpTag(int cpIndex)
    {
        return constantPool[cpIndex].getTag();
    }

    public String getCpString(int cpIndex)
    {
        return ((Utf8CpInfo)constantPool[cpIndex]).getString();
    }

    public String getCpClassNameString(int cpIndex)
    {
        ClassCpInfo classEntry = (ClassCpInfo)constantPool[cpIndex];
        Utf8CpInfo  nameEntry  = (Utf8CpInfo)constantPool[classEntry.getNameIndex()];

        return nameEntry.getString();
    }

    public String getCpNameString(int cpIndex)
    {
        return ((NameAndTypeCpInfo)constantPool[cpIndex]).getName(this);
    }

    public String getCpTypeString(int cpIndex)
    {
        return ((NameAndTypeCpInfo)constantPool[cpIndex]).getType(this);
    }


    public void addSubClass(ClassFile classFile)
    {
        if (subClasses == null)
        {
            subClasses = new ClassFile[1];
        }
        else
        {
            // Copy the old elements into new larger array.
            ClassFile[] temp = new ClassFile[subClasses.length+1];
            System.arraycopy(subClasses, 0, temp, 0, subClasses.length);
            subClasses = temp;
        }

        subClasses[subClasses.length-1] = classFile;
    }


    public ClassFile getSuperClass()
    {
        return u2superClass != 0 ?
            ((ClassCpInfo)constantPool[u2superClass]).referencedClassFile :
            null;
    }


    public FieldInfo findField(String name, String descriptor)
    {
        return findProgramField(name, descriptor);
    }

    public MethodInfo findMethod(String name, String descriptor)
    {
        return findProgramMethod(name, descriptor);
    }


    public void accept(ClassFileVisitor classFileVisitor)
    {
        classFileVisitor.visitProgramClassFile(this);
    }

    public void constantPoolEntriesAccept(CpInfoVisitor cpInfoVisitor)
    {
        for (int i = 1; i < u2constantPoolCount; i++)
        {
            if (constantPool[i] != null)
            {
                constantPool[i].accept(this, cpInfoVisitor);
            }
        }
    }

    public void constantPoolEntryAccept(CpInfoVisitor cpInfoVisitor, int index)
    {
        constantPool[index].accept(this, cpInfoVisitor);
    }

    public void fieldsAccept(MemberInfoVisitor memberInfoVisitor)
    {
        for (int i = 0; i < u2fieldsCount; i++)
        {
            fields[i].accept(this, memberInfoVisitor);
        }
    }

    public void fieldAccept(MemberInfoVisitor memberInfoVisitor, String name, String descriptor)
    {
        ProgramFieldInfo field = findProgramField(name, descriptor);
        if (field != null)
        {
            field.accept(this, memberInfoVisitor);
        }
    }

    public void methodsAccept(MemberInfoVisitor memberInfoVisitor)
    {
        for (int i = 0; i < u2methodsCount; i++)
        {
            methods[i].accept(this, memberInfoVisitor);
        }
    }

    public void methodAccept(MemberInfoVisitor memberInfoVisitor, String name, String descriptor)
    {
        ProgramMethodInfo method = findProgramMethod(name, descriptor);
        if (method != null)
        {
            method.accept(this, memberInfoVisitor);
        }
    }

    public void attributesAccept(AttrInfoVisitor attrInfoVisitor)
    {
        for (int i = 0; i < u2attributesCount; i++)
        {
            attributes[i].accept(this, attrInfoVisitor);
        }
    }


    // Implementations for VisitorAccepter

    public Object getVisitorInfo()
    {
        return visitorInfo;
    }

    public void setVisitorInfo(Object visitorInfo)
    {
        this.visitorInfo = visitorInfo;
    }
}