/*
 * Copyright (C) 2009-2013 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.fbreader.library;

import java.util.Collections;
import java.util.List;

import org.geometerplus.fbreader.book.*;

public class AuthorListTree extends FirstLevelTree {
	AuthorListTree(RootTree root) {
		super(root, ROOT_BY_AUTHOR);
	}

	@Override
	public Status getOpeningStatus() {
		return Status.ALWAYS_RELOAD_BEFORE_OPENING;
	}

	@Override
	public void waitForOpening() {
		clear();
		for (Author a : Collection.authors()) {
			createAuthorSubTree(a);
		}
	}

	@Override
	public boolean onBookEvent(BookEvent event, Book book) {
		switch (event) {
			case Added:
			{
				final List<Author> bookAuthors = book.authors();
				boolean changed = false;
				if (bookAuthors.isEmpty()) {
					changed &= createAuthorSubTree(Author.NULL);
				} else for (Author a : bookAuthors) {
					changed &= createAuthorSubTree(a);
				}
				return changed;
			}
			case Removed:
				// TODO: implement
				return false;
			default:
			case Updated:
				// TODO: implement
				return false;
		}
	}

	private boolean createAuthorSubTree(Author author) {
		final AuthorTree temp = new AuthorTree(Collection, author);
		int position = Collections.binarySearch(subTrees(), temp);
		if (position >= 0) {
			return false;
		} else {
			new AuthorTree(this, author, - position - 1);
			return true;
		}
	}
}