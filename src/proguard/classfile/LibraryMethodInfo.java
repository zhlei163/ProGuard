/* $Id: LibraryMethodInfo.java,v 1.8 2002/07/04 16:16:58 eric Exp $
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
 * Representation of a method from a class-file.
 *
 * @author Mark Welsh
 * @author Eric Lafortune
 */
public class LibraryMethodInfo extends LibraryMemberInfo implements MethodInfo
{


    /**
     * Creates a new LibraryMethodInfo from the file format data in the DataInput stream.
     *
     * @throws IOException if class file is corrupt or incomplete
     */
    public static LibraryMethodInfo create(DataInput din, CpInfo[] constantPool) throws IOException
    {
        LibraryMethodInfo mi = new LibraryMethodInfo();
        mi.read(din, constantPool);
        return mi;
    }

    /**
     * Accepts the given visitor.
     */
    public void accept(LibraryClassFile libraryClassFile, MemberInfoVisitor memberInfoVisitor)
    {
        memberInfoVisitor.visitLibraryMethodInfo(libraryClassFile, this);
    }


    protected LibraryMethodInfo() {}
}