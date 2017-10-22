package com.ganapathy.cricscorer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@SuppressLint({"HandlerLeak"})
public class CreateMatch extends BaseActivity {
    public static Handler hInstance;
    private ArrayAdapter<String> teamAutoCompleteAdapter;
    private ArrayAdapter<String> teamSpinnerAdapter;

    class C02181 extends Handler {
        C02181() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CreateMatch.this.finish();
                    return;
                default:
                    return;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(C0252R.layout.activity_create_match);
            hInstance = new C02181();
            initializeControls();
        } catch (Exception e) {
            finish();
        }
    }

    public void onResume() {
        super.onResume();
        populateTeamSpinner();
        populateTeamAutoComplete();
    }

    protected void initializeControls() {
        ((CricScorerApp) getApplication()).currentMatch = new dtoMatch();
        EditText dateTimeText = (EditText) findViewById(C0252R.id.editDateTime);
        if (dateTimeText.getText().toString().trim().equalsIgnoreCase("")) {
            dateTimeText.setText(new SimpleDateFormat("dd/MM/yy kk:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()));
        }
        if (VERSION.SDK_INT >= 14) {
            Button btnTeam1 = (Button) findViewById(C0252R.id.buttonTeam1);
            Button btnTeam2 = (Button) findViewById(C0252R.id.buttonTeam2);
            btnTeam1.setPadding(dateTimeText.getPaddingLeft(), btnTeam1.getPaddingTop(), btnTeam1.getPaddingRight(), btnTeam1.getPaddingBottom());
            btnTeam2.setPadding(dateTimeText.getPaddingLeft(), btnTeam2.getPaddingTop(), btnTeam2.getPaddingRight(), btnTeam2.getPaddingBottom());
        }
        registerForContextMenu((Button) findViewById(C0252R.id.noOfInngs));
        registerForContextMenu((Button) findViewById(C0252R.id.noOfDays));
        findViewById(C0252R.id.focusLayout).requestFocus();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if (v.getId() == C0252R.id.noOfInngs) {
            menu.setHeaderTitle(C0252R.string.inngsPerSideText);
            inflater.inflate(C0252R.menu.noinngs_context_menu, menu);
        } else if (v.getId() == C0252R.id.noOfDays) {
            menu.setHeaderTitle(C0252R.string.noOfDaysText);
            inflater.inflate(C0252R.menu.days_context_menu, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0252R.id.miDayOne:
            case C0252R.id.miDayTwo:
            case C0252R.id.miDayThree:
            case C0252R.id.miDayFour:
            case C0252R.id.miDayFive:
            case C0252R.id.miDaySix:
                ((Button) findViewById(C0252R.id.noOfDays)).setText(item.getTitle());
                return true;
            case C0252R.id.miOne:
            case C0252R.id.miTwo:
                ((Button) findViewById(C0252R.id.noOfInngs)).setText(item.getTitle());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void populateTeamSpinner() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> teamList = db.getManageTeamList();
        db.close();
        int i = 0;
        if (!teamList.contains(getString(C0252R.string.team1Text).toUpperCase(Locale.US))) {
            int pos = 0 + 1;
            teamList.add(0, getString(C0252R.string.team1Text).toUpperCase(Locale.US));
            i = pos;
        }
        if (!teamList.contains(getString(C0252R.string.team2Text).toUpperCase(Locale.US))) {
            pos = i + 1;
            teamList.add(i, getString(C0252R.string.team2Text).toUpperCase(Locale.US));
            i = pos;
        }
        if (teamList.size() > 0) {
            this.teamSpinnerAdapter = new ArrayAdapter(this, 17367055, teamList);
        }
    }

    public void populateTeamAutoComplete() {
        DatabaseHandler db = new DatabaseHandler(this);
        List<String> teamList = db.getManageTeamList();
        db.close();
        int i = 0;
        if (!teamList.contains(getString(C0252R.string.team1Text).toUpperCase(Locale.US))) {
            int pos = 0 + 1;
            teamList.add(0, getString(C0252R.string.team1Text).toUpperCase(Locale.US));
            i = pos;
        }
        if (!teamList.contains(getString(C0252R.string.team2Text).toUpperCase(Locale.US))) {
            pos = i + 1;
            teamList.add(i, getString(C0252R.string.team2Text).toUpperCase(Locale.US));
            i = pos;
        }
        if (teamList.size() > 0) {
            this.teamAutoCompleteAdapter = new ArrayAdapter(this, 17367048, teamList);
            this.teamAutoCompleteAdapter.setDropDownViewResource(17367049);
        }
    }

    public void onTeam1(View view) {
        displayPopup((Button) view);
    }

    public void onTeam2(View view) {
        displayPopup((Button) view);
    }

    public void onNoOfInnings(View view) {
        openContextMenu(view);
    }

    public void onNoOfDays(View view) {
        openContextMenu(view);
    }

    private void displayPopup(Button target) {
        Rect displayRectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(C0252R.layout.popup_choose_team_from_list, (ViewGroup) findViewById(C0252R.id.choosePopupLayout));
        layout.setMinimumWidth((int) (((float) displayRectangle.width()) * 0.9f));
        View textView = new TextView(layout.getContext());
        textView.setText(C0252R.string.teamNameText);
        if (VERSION.SDK_INT < 23) {
            Utility.setLargeTitle(textView, layout.getContext());
        } else {
            Utility.setLargeTitle(textView);
        }
        textView.setHeight(60);
        if (VERSION.SDK_INT < 11) {
            TypedValue a = new TypedValue();
            getTheme().resolveAttribute(16842836, a, true);
            if (a.type < 28 || a.type > 31) {
                layout.setBackgroundResource(a.resourceId);
                textView.setBackgroundResource(a.resourceId);
            } else {
                int windowBackground = a.data;
                layout.setBackgroundColor(windowBackground);
                textView.setBackgroundColor(windowBackground);
            }
        }
        Builder builder = new Builder(this);
        builder.setView(layout);
        builder.setCustomTitle(textView);
        final AlertDialog alertDialog = builder.create();
        final AutoCompleteTextView textTeam = (AutoCompleteTextView) layout.findViewById(C0252R.id.editTeamName);
        final ListView spinnerTeam = (ListView) layout.findViewById(C0252R.id.listTeams);
        if (this.teamSpinnerAdapter == null) {
            populateTeamSpinner();
        }
        spinnerTeam.setAdapter(this.teamSpinnerAdapter);
        String value = "" + target.getText().toString().trim();
        int position = this.teamSpinnerAdapter.getPosition(value);
        if (position > 0) {
            spinnerTeam.setSelection(position);
        } else {
            textTeam.setText(value);
        }
        if (this.teamAutoCompleteAdapter == null) {
            populateTeamAutoComplete();
        }
        textTeam.setAdapter(this.teamAutoCompleteAdapter);
        final Button button = target;
        spinnerTeam.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                button.setText("" + spinnerTeam.getItemAtPosition(position));
                ((InputMethodManager) CreateMatch.this.getSystemService("input_method")).hideSoftInputFromWindow(textTeam.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        final Button button2 = target;
        ((Button) layout.findViewById(C0252R.id.btnOK)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String teamName = "";
                if (!textTeam.getText().toString().trim().equals("")) {
                    teamName = textTeam.getText().toString().trim();
                }
                button2.setText(teamName);
                ((InputMethodManager) CreateMatch.this.getSystemService("input_method")).hideSoftInputFromWindow(textTeam.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        ((Button) layout.findViewById(C0252R.id.btnClose)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) CreateMatch.this.getSystemService("input_method")).hideSoftInputFromWindow(textTeam.getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.getWindow().setSoftInputMode(16);
        ((ViewGroup) layout.getParent()).setPadding(0, 0, 0, 0);
    }

    public void onSetTeams(View view) {
        dtoMatch refMatch = ((CricScorerApp) getApplication()).currentMatch;
        String team1Name = "" + ((Button) findViewById(C0252R.id.buttonTeam1)).getText().toString();
        String team2Name = "" + ((Button) findViewById(C0252R.id.buttonTeam2)).getText().toString();
        if (("" + team1Name) == "" && ("" + team2Name) == "") {
            Toast.makeText(getApplicationContext(), "Please choose team(s) to choose squad!", 0).show();
            return;
        }
        team1Name = populateTeam1Players(team1Name, refMatch, false);
        team2Name = populateTeam2Players(team2Name, refMatch, false);
        refMatch.setTeam1Name(team1Name);
        refMatch.setTeam2Name(team2Name);
        startActivity(new Intent(this, SetTeams.class));
    }

    public void onAdditionalSettings(View view) {
        startActivity(new Intent(this, AdditionalSettings.class));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartMatch(android.view.View r18) {
        /*
        r17 = this;
        r10 = new java.text.SimpleDateFormat;
        r14 = "dd/MM/yy kk:mm:ss";
        r15 = java.util.Locale.getDefault();
        r10.<init>(r14, r15);
        r14 = r17.getApplication();
        r14 = (com.ganapathy.cricscorer.CricScorerApp) r14;
        r9 = r14.currentMatch;
        r14 = 2131493054; // 0x7f0c00be float:1.8609577E38 double:1.0530974923E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.EditText) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r14 = r14.trim();
        r15 = "";
        r14 = r14.equalsIgnoreCase(r15);
        if (r14 == 0) goto L_0x004c;
    L_0x0032:
        r14 = 2131493054; // 0x7f0c00be float:1.8609577E38 double:1.0530974923E-314;
        r0 = r17;
        r2 = r0.findViewById(r14);
        r2 = (android.widget.EditText) r2;
        r14 = java.util.Calendar.getInstance();
        r14 = r14.getTime();
        r14 = r10.format(r14);
        r2.setText(r14);
    L_0x004c:
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "";
        r15 = r14.append(r15);
        r14 = 2131493063; // 0x7f0c00c7 float:1.8609596E38 double:1.053097497E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.Button) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r14 = r14.trim();
        r14 = r15.append(r14);
        r11 = r14.toString();
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "";
        r15 = r14.append(r15);
        r14 = 2131493064; // 0x7f0c00c8 float:1.8609598E38 double:1.0530974973E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.Button) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r14 = r14.trim();
        r14 = r15.append(r14);
        r12 = r14.toString();
        r14 = 1;
        r0 = r17;
        r11 = r0.populateTeam1Players(r11, r9, r14);
        r14 = 1;
        r0 = r17;
        r12 = r0.populateTeam2Players(r12, r9, r14);
        r14 = r11.equalsIgnoreCase(r12);
        if (r14 == 0) goto L_0x00c4;
    L_0x00b4:
        r14 = r17.getApplicationContext();
        r15 = "Please choose different teams as opponents!";
        r16 = 1;
        r14 = android.widget.Toast.makeText(r14, r15, r16);
        r14.show();
    L_0x00c3:
        return;
    L_0x00c4:
        r14 = 2131493060; // 0x7f0c00c4 float:1.860959E38 double:1.0530974953E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.EditText) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r14 = r14.trim();
        r15 = "";
        r14 = r14.equalsIgnoreCase(r15);
        if (r14 == 0) goto L_0x00f3;
    L_0x00e3:
        r14 = 2131493060; // 0x7f0c00c4 float:1.860959E38 double:1.0530974953E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.EditText) r14;
        r15 = "12";
        r14.setText(r15);
    L_0x00f3:
        r14 = 2131493054; // 0x7f0c00be float:1.8609577E38 double:1.0530974923E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.EditText) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r9.setDateTime(r14);
        r14 = 2131493056; // 0x7f0c00c0 float:1.8609581E38 double:1.0530974933E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.EditText) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r9.setVenue(r14);
        r14 = 2131493055; // 0x7f0c00bf float:1.860958E38 double:1.053097493E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);
        r14 = (android.widget.EditText) r14;
        r14 = r14.getText();
        r14 = r14.toString();
        r9.setMatchName(r14);
        r9.setTeam1Name(r11);
        r9.setTeam2Name(r12);
        r14 = 2131493066; // 0x7f0c00ca float:1.8609602E38 double:1.0530974983E-314;
        r0 = r17;
        r13 = r0.findViewById(r14);
        r13 = (android.widget.SeekBar) r13;
        r14 = r13.getProgress();
        r15 = 1;
        if (r14 != r15) goto L_0x0151;
    L_0x014d:
        r14 = 0;
        r13.setProgress(r14);
    L_0x0151:
        r14 = r13.getProgress();
        if (r14 != 0) goto L_0x0234;
    L_0x0157:
        r14 = 2131034380; // 0x7f05010c float:1.7679276E38 double:1.0528708773E-314;
        r0 = r17;
        r14 = r0.getString(r14);
    L_0x0160:
        r9.setTossWonBy(r14);
        r14 = 2131493068; // 0x7f0c00cc float:1.8609606E38 double:1.0530974992E-314;
        r0 = r17;
        r7 = r0.findViewById(r14);
        r7 = (android.widget.ToggleButton) r7;
        r14 = r7.isChecked();
        if (r14 == 0) goto L_0x023f;
    L_0x0174:
        r14 = "Bowl";
    L_0x0176:
        r9.setOptedTo(r14);
        r14 = 2131493059; // 0x7f0c00c3 float:1.8609587E38 double:1.053097495E-314;
        r0 = r17;
        r1 = r0.findViewById(r14);
        r1 = (android.widget.CheckBox) r1;
        r14 = r1.isChecked();
        if (r14 != 0) goto L_0x0250;
    L_0x018a:
        r8 = 12;
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14.<init>();	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r15 = "0";
        r15 = r14.append(r15);	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14 = 2131493060; // 0x7f0c00c4 float:1.860959E38 double:1.0530974953E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14 = (android.widget.EditText) r14;	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14 = r14.getText();	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14 = r14.toString();	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14 = r15.append(r14);	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r14 = r14.toString();	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        r8 = java.lang.Integer.parseInt(r14);	 Catch:{ Exception -> 0x0243, all -> 0x024b }
        if (r8 > 0) goto L_0x01ba;
    L_0x01b8:
        r8 = 12;
    L_0x01ba:
        r9.setOvers(r8);
    L_0x01bd:
        r5 = 1;
        r14 = 2131493061; // 0x7f0c00c5 float:1.8609592E38 double:1.053097496E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);	 Catch:{ Exception -> 0x0257, all -> 0x025e }
        r14 = (android.widget.Button) r14;	 Catch:{ Exception -> 0x0257, all -> 0x025e }
        r14 = r14.getText();	 Catch:{ Exception -> 0x0257, all -> 0x025e }
        r14 = r14.toString();	 Catch:{ Exception -> 0x0257, all -> 0x025e }
        r5 = java.lang.Integer.parseInt(r14);	 Catch:{ Exception -> 0x0257, all -> 0x025e }
        if (r5 > 0) goto L_0x01d8;
    L_0x01d7:
        r5 = 1;
    L_0x01d8:
        r9.setNoOfInngs(r5);
    L_0x01db:
        r3 = 1;
        r14 = 2131493062; // 0x7f0c00c6 float:1.8609594E38 double:1.0530974963E-314;
        r0 = r17;
        r14 = r0.findViewById(r14);	 Catch:{ Exception -> 0x0263, all -> 0x0269 }
        r14 = (android.widget.Button) r14;	 Catch:{ Exception -> 0x0263, all -> 0x0269 }
        r14 = r14.getText();	 Catch:{ Exception -> 0x0263, all -> 0x0269 }
        r14 = r14.toString();	 Catch:{ Exception -> 0x0263, all -> 0x0269 }
        r3 = java.lang.Integer.parseInt(r14);	 Catch:{ Exception -> 0x0263, all -> 0x0269 }
        if (r3 > 0) goto L_0x01f6;
    L_0x01f5:
        r3 = 1;
    L_0x01f6:
        r9.setNoOfDays(r3);
    L_0x01f9:
        r14 = java.util.Calendar.getInstance();
        r14 = r14.getTime();
        r14 = r10.format(r14);
        r15 = 47;
        r16 = 95;
        r14 = r14.replace(r15, r16);
        r15 = 58;
        r16 = 95;
        r14 = r14.replace(r15, r16);
        r15 = 32;
        r16 = 95;
        r14 = r14.replace(r15, r16);
        r9.setMatchID(r14);
        r14 = 1;
        r9.setCurrentSession(r14);
        r6 = new android.content.Intent;
        r14 = com.ganapathy.cricscorer.ConfirmStartMatch.class;
        r0 = r17;
        r6.<init>(r0, r14);
        r0 = r17;
        r0.startActivity(r6);
        goto L_0x00c3;
    L_0x0234:
        r14 = 2131034381; // 0x7f05010d float:1.7679278E38 double:1.052870878E-314;
        r0 = r17;
        r14 = r0.getString(r14);
        goto L_0x0160;
    L_0x023f:
        r14 = "Bat";
        goto L_0x0176;
    L_0x0243:
        r4 = move-exception;
        r8 = 12;
        r9.setOvers(r8);
        goto L_0x01bd;
    L_0x024b:
        r14 = move-exception;
        r9.setOvers(r8);
        throw r14;
    L_0x0250:
        r14 = 4479; // 0x117f float:6.276E-42 double:2.213E-320;
        r9.setOvers(r14);
        goto L_0x01bd;
    L_0x0257:
        r4 = move-exception;
        r5 = 1;
        r9.setNoOfInngs(r5);
        goto L_0x01db;
    L_0x025e:
        r14 = move-exception;
        r9.setNoOfInngs(r5);
        throw r14;
    L_0x0263:
        r4 = move-exception;
        r3 = 1;
        r9.setNoOfDays(r3);
        goto L_0x01f9;
    L_0x0269:
        r14 = move-exception;
        r9.setNoOfDays(r3);
        throw r14;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ganapathy.cricscorer.CreateMatch.onStartMatch(android.view.View):void");
    }

    protected String populateTeam1Players(String team1Name, dtoMatch refMatch, boolean autoPopulate) {
        String returnTeamName = team1Name;
        if (team1Name.trim().equalsIgnoreCase("")) {
            if (!autoPopulate) {
                return returnTeamName;
            }
            ((Button) findViewById(C0252R.id.buttonTeam1)).setText(getString(C0252R.string.team1Text).toUpperCase(Locale.US));
            return ("" + ((Button) findViewById(C0252R.id.buttonTeam1)).getText().toString()).trim();
        } else if (refMatch.getTeam1Players().size() > 0) {
            return returnTeamName;
        } else {
            DatabaseHandler db = new DatabaseHandler(this);
            dtoTeamPlayerList team1Players = db.getManageTeamPlayersList(team1Name);
            db.close();
            if (team1Players.size() <= 0) {
                return returnTeamName;
            }
            refMatch.setTeam1Players(team1Players);
            return returnTeamName;
        }
    }

    protected String populateTeam2Players(String team2Name, dtoMatch refMatch, boolean autoPopulate) {
        String returnTeamName = team2Name;
        if (team2Name.trim().equalsIgnoreCase("")) {
            if (!autoPopulate) {
                return returnTeamName;
            }
            ((Button) findViewById(C0252R.id.buttonTeam2)).setText(getString(C0252R.string.team2Text).toUpperCase(Locale.US));
            return ("" + ((Button) findViewById(C0252R.id.buttonTeam2)).getText().toString()).trim();
        } else if (refMatch.getTeam2Players().size() > 0) {
            return returnTeamName;
        } else {
            DatabaseHandler db = new DatabaseHandler(this);
            dtoTeamPlayerList team2Players = db.getManageTeamPlayersList(team2Name);
            db.close();
            if (team2Players.size() <= 0) {
                return returnTeamName;
            }
            refMatch.setTeam2Players(team2Players);
            return returnTeamName;
        }
    }

    public void doCoinToss(View view) {
        startActivity(new Intent(this, CoinToss.class));
    }

    public void setTossWonBy(View view) {
        SeekBar tossWonBy = (SeekBar) findViewById(C0252R.id.seekTossWonBy);
        if (view.getId() == C0252R.id.tossTeam2) {
            tossWonBy.setProgress(2);
        } else {
            tossWonBy.setProgress(0);
        }
    }

    public void changeOversConfig(View view) {
        EditText oversText = (EditText) findViewById(C0252R.id.editOvers);
        if (((CheckBox) findViewById(C0252R.id.checkConfigOvers)).isChecked()) {
            oversText.setVisibility(4);
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(oversText.getWindowToken(), 0);
            findViewById(C0252R.id.focusLayout).requestFocus();
            return;
        }
        oversText.setVisibility(0);
        findViewById(C0252R.id.focusLayout).requestFocus();
    }
}
