/*
 * Copyright (C) 2012-2018 The Android Money Manager Ex Project Team
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.money.manager.ex.reports;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.money.manager.ex.R;
import com.money.manager.ex.common.MmxBaseFragmentActivity;

public class PayeesReportActivity
        extends MmxBaseFragmentActivity {

    public boolean mIsDualPanel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.payee);

        setContentView(R.layout.report_chart_fragments_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // set actionbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //check if is dual panel
        mIsDualPanel = findViewById(R.id.fragmentChart) != null;

        PayeeReportFragment fragment = new PayeeReportFragment();
        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentById(R.id.fragmentMain) == null) {
            fm.beginTransaction().add(R.id.fragmentMain, fragment, PayeeReportFragment.class.getSimpleName()).commit();
        }
    }

}
