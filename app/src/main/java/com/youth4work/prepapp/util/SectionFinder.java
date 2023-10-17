/*
 * Copyright 2012 Kevin Sawicki <kevinsawicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youth4work.prepapp.util;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.widget.SectionIndexer;

import java.util.LinkedHashSet;
import java.util.Set;

public class SectionFinder implements SectionIndexer {

    private final Set<Object> sections = new LinkedHashSet<Object>();

    private final SparseIntArray sectionPositions = new SparseIntArray();

    private final SparseIntArray itemSections = new SparseIntArray();

    private int index = 0;

    @NonNull
    public SectionFinder clear() {
        sections.clear();
        sectionPositions.clear();
        itemSections.clear();
        index = 0;
        return this;
    }

    protected Object getSection(@NonNull final Object item) {
        final String string = item.toString();
        if (!TextUtils.isEmpty(string))
            return Character.toUpperCase(string.charAt(0));
        else
            return '?';
    }

    private void addSection(Object section) {
        int count = sections.size();
        if (sections.add(section))
            sectionPositions.put(count, index);
    }

    private void addItem(Object item) {
        itemSections.put(index, sections.size());
        index++;
    }

    @NonNull
    public SectionFinder index(@NonNull Object... items) {
        for (Object item : items) {
            addSection(getSection(item));
            addItem(item);
        }
        return this;
    }

    @NonNull
    public SectionFinder add(final Object section, @NonNull final Object... items) {
        addSection(section);
        for (Object item : items)
            addItem(item);
        return this;
    }

    public int getPositionForSection(final int section) {
        return sectionPositions.get(section);
    }

    public int getSectionForPosition(final int position) {
        return itemSections.get(position);
    }

    @NonNull
    public Object[] getSections() {
        return sections.toArray();
    }
}
