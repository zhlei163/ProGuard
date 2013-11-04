/* $Id: VisitorAccepter.java,v 1.6 2002/05/16 19:04:27 eric Exp $
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


/**
 * This interface is a base interface for visitor accepters. It allows
 * visitors to set and get any temporary information they desire on the
 * objects they are visiting. Note that every visitor accepter has only one
 * such property, so visitors will have to take care not to overwrite each
 * other's information, if it is still required.
 *
 * @author Eric Lafortune
 */
public interface VisitorAccepter
{
    /**
     * Gets the visitor information of the visitor accepter.
     */
    public Object getVisitorInfo();


    /**
     * Sets the visitor information of the visitor accepter.
     */
    public void setVisitorInfo(Object visitorInfo);
}