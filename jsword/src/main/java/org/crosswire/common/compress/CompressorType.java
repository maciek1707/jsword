/**
 * Distribution License:
 * JSword is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, version 2.1 as published by
 * the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The License is available on the internet at:
 *       http://www.gnu.org/copyleft/lgpl.html
 * or by writing to:
 *      Free Software Foundation, Inc.
 *      59 Temple Place - Suite 330
 *      Boston, MA 02111-1307, USA
 *
 * Copyright: 2007
 *     The copyright to this program is held by it's authors.
 *
 * ID: $Id$
 */
package org.crosswire.common.compress;

import java.io.ByteArrayInputStream;

/**
 * An Enumeration of the possible Compressions.
 * 
 * @see gnu.lgpl.License for license details.<br>
 *      The copyright to this program is held by it's authors.
 * @author DM Smith [dmsmith555 at yahoo dot com]
 */
public enum CompressorType {
    ZIP {
        public Compressor getCompressor(byte[] input) {
            return new Zip(new ByteArrayInputStream(input));
        }
    },

    LZSS {

        public Compressor getCompressor(byte[] input) {
            return new LZSS(new ByteArrayInputStream(input));
        }
    };

    /**
     * Get a compressor.
     * @param input the stream to compress or to uncompress.
     */
    public abstract Compressor getCompressor(byte[] input);

    /**
     * Get a CompressorType from a String
     * 
     * @param name the case insensitive representation of the desired CompressorType
     * @return the desired compressor or null if not found.
     */
    public static CompressorType fromString(String name) {
        for (CompressorType v : values()) {
            if (v.name().equalsIgnoreCase(name)) {
                return v;
            }
        }

        // cannot get here
        assert false;
        return null;
    }
}
