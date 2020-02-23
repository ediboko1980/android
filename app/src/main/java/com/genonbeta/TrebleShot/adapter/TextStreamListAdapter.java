/*
 * Copyright (C) 2019 Veli Tasalı
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.genonbeta.TrebleShot.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.genonbeta.TrebleShot.R;
import com.genonbeta.TrebleShot.database.AccessDatabase;
import com.genonbeta.TrebleShot.object.TextStreamObject;
import com.genonbeta.TrebleShot.widget.GroupEditableListAdapter;
import com.genonbeta.android.database.SQLQuery;

/**
 * created by: Veli
 * date: 30.12.2017 13:25
 */

public class TextStreamListAdapter
        extends GroupEditableListAdapter<TextStreamObject, GroupEditableListAdapter.GroupViewHolder>
{
    private AccessDatabase mDatabase;

    public TextStreamListAdapter(Context context, AccessDatabase database)
    {
        super(context, MODE_GROUP_BY_DATE);
        mDatabase = database;
    }

    @Override
    protected void onLoad(GroupLister<TextStreamObject> lister)
    {
        for (TextStreamObject object : mDatabase.castQuery(new SQLQuery.Select(AccessDatabase.TABLE_CLIPBOARD), TextStreamObject.class))
            lister.offerObliged(this, object);
    }

    @Override
    protected TextStreamObject onGenerateRepresentative(String representativeText)
    {
        return new TextStreamObject(representativeText);
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return viewType == VIEW_TYPE_DEFAULT ? new GroupViewHolder(getInflater().inflate(
                R.layout.list_text_stream, parent, false)) : createDefaultViews(parent, viewType, false);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position)
    {
        try {
            TextStreamObject object = getItem(position);

            if (!holder.tryBinding(object)) {
                View parentView = holder.itemView;
                String text = object.text.replace("\n", " ");

                TextView text1 = parentView.findViewById(R.id.text);
                TextView text2 = parentView.findViewById(R.id.text2);
                TextView text3 = parentView.findViewById(R.id.text3);

                parentView.setSelected(object.isSelectableSelected());

                text1.setText(text);
                text2.setText(DateUtils.formatDateTime(getContext(), object.date, DateUtils.FORMAT_SHOW_TIME));
                text3.setVisibility(getGroupBy() != MODE_GROUP_BY_DATE ? View.VISIBLE : View.GONE);

                if (getGroupBy() != MODE_GROUP_BY_DATE)
                    text3.setText(getSectionNameDate(object.date));
            }
        } catch (Exception ignored) {

        }
    }
}