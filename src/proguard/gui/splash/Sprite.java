/* $Id: Sprite.java,v 1.3 2003/12/06 22:12:42 eric Exp $
 *
 * ProGuard -- obfuscation and shrinking package for Java class files.
 *
 * Copyright (c) 2002-2004 Eric Lafortune (eric@graphics.cornell.edu)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package proguard.gui.splash;

import java.awt.Graphics;

/**
 * This interface describes objects that can paint themselves, possibly varying
 * as a function of time.
 *
 * @author Eric Lafortune
 */
public interface Sprite
{
    /**
     * Paints the object.
     *
     * @param graphics the Graphics to paint on.
     * @param time     the time since the start of the animation, expressed in
     *                  milliseconds.
     */
    public void paint(Graphics graphics, long time);
}
