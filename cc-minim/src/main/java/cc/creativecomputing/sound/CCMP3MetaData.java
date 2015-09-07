/*
 *  Copyright (c) 2007 - 2008 by Damien Di Fede <ddf@compartmental.net>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package cc.creativecomputing.sound;

import java.nio.file.Path;
import java.util.Map;

class CCMP3MetaData extends CCAudioMetaData {
	private Map<String, Object> _myTagMap;

	CCMP3MetaData(Path thePath, long theLength, Map<String, Object> theTagMap) {
		super(thePath, theLength, -1);
		_myTagMap = theTagMap;
	}

	private String getTag(String tag) {
		if (_myTagMap.containsKey(tag)) {
			return (String) _myTagMap.get(tag);
		}
		return "";
	}

	public String title() {
		return getTag("title");
	}

	public String author() {
		return getTag("author");
	}

	public String album() {
		return getTag("album");
	}

	public String date() {
		return getTag("date");
	}

	public String comment() {
		return getTag("comment");
	}

	public String track() {
		return getTag("mp3.id3tag.track");
	}

	public String genre() {
		return getTag("mp3.id3tag.genre");
	}

	public String copyright() {
		return getTag("copyright");
	}

	public String disc() {
		return getTag("mp3.id3tag.disc");
	}

	public String composer() {
		return getTag("mp3.id3tag.composer");
	}

	public String lyrics() {
		return getTag("mp3.id3tag.lyrics");
	}

	public String orchestra() {
		return getTag("mp3.id3tag.orchestra");
	}

	public String publisher() {
		return getTag("mp3.id3tag.publisher");
	}

	public String encoded() {
		return getTag("mp3.id3tag.encoded");
	}
}
