/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2022 czlucius
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.callbacks.ManualResetPreferenceClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        // Instantiate ads if on play flavour.
        View v = super.onCreateView(inflater, container, savedInstanceState);

        Preference oss_link = findPreference("open_source");
        if (oss_link != null) {
            oss_link.setOnPreferenceClickListener(preference -> {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                Uri url = Uri.parse(getString(R.string.oss_link));
                urlIntent.setData(url);
                boolean appAvailable = Utils.launchIntentCheckAvailable(urlIntent, requireContext());
                if (!appAvailable) {
                    Toast.makeText(getContext(), R.string.no_browsers, Toast.LENGTH_SHORT).show();
                }
                return true;
            });
        }

        Preference oss_licenses = findPreference("oss_licenses");
        if (oss_licenses != null) {
            setupOSSLicensesDialog(oss_licenses);
        }


//        if (findPreference("watch_ads_prefbtn") != null) {
//
//            findPreference("watch_ads_prefbtn").setOnPreferenceClickListener(new ManualResetPreferenceClickListener() {
//                @Override
//                public boolean onSingleClick(Preference p) {
//                    new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_App_AlertDialogTheme)
//                            .setBackground(new ColorDrawable(Color.YELLOW))
//                            .setTitle(R.string.rewarded_admob)
//                            .setMessage(R.string.rewarded_dialog_prompt)
//                            .setNegativeButton(R.string.cancel, (dialog, which) -> resetListener())
//                            .setPositiveButton(R.string.next, (dialog, which) -> {
//                                Toast.makeText(getActivity(), R.string.not_app_content, Toast.LENGTH_LONG).show();
//                            })
//                            .setOnCancelListener(dialog -> resetListener())
//                            .show();
//                    return true;
//                }
//            });
//        }

        if (findPreference("privacy_policy") != null) {
            findPreference("privacy_policy").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Utils.launchWebPageExternally(
                            getContext(),
                            Uri.parse(getString(R.string.privacy_policy_url))
                    );
                    return true;
                }
            });
        }

        if (findPreference("share_app") != null) {
            findPreference("share_app").setOnPreferenceClickListener(preference -> {
                // TODO this only links to Google Play, link to other stores soon!
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.promo_share_description, getString(R.string.store_url)));
                sendIntent.setType("text/plain");

                startActivity(sendIntent);

                return true;
            });
        }

        return v;
    }

    private void setupOSSLicensesDialog(Preference preferenceToBeClicked) {
        // WebView for displaying open-source licenses
        WebView webView = new WebView(requireContext());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return shouldOverrideUrlLoading(Uri.parse(url));
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri url = request.getUrl();
                return shouldOverrideUrlLoading(url);
            }

            private boolean shouldOverrideUrlLoading(Uri url) {
                Utils.launchWebPageExternally(getContext(), url);
                return true;
            }
        });
        webView.zoomBy(0.5F);

        MaterialAlertDialogBuilder ossDialog = new MaterialAlertDialogBuilder(requireContext());
        ossDialog.setView(webView);
        AlertDialog finalDialog = ossDialog.create();

        preferenceToBeClicked.setOnPreferenceClickListener(preference -> {
            webView.loadUrl("file:///android_asset/licenses.html"); // Load from app's asset folder
            finalDialog.show();
            return true;
        });
    }
}