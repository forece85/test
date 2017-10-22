package com.ganapathy.cricscorer;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ConvertToZip extends BaseActivity {
    private String _fileName = "";
    private String _storagePath = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0252R.layout.activity_convert_to_zip);
        this._storagePath = getIntent().getExtras().getString("StoragePath");
        this._fileName = getIntent().getExtras().getString("FileName");
    }

    public void onResume() {
        super.onResume();
        ((TextView) findViewById(C0252R.id.introText)).setText("IMPORTANT: \nBest Archive is newly implemented archiving algorithm in Best Cricket Scorer to provide Best Archiving methodology. \n\nThis also eliminates memory crash during restoration of huge archive files. \n\nPlease convert Raw format Archive files to Best Archive for better performance and better memory handling. \n\nHelp & Support:\nFor help and support visit: \nhttp://bestcricketscorer.blogspot.in/\nhttp://www.facebook.com/BestCricketScorer\nFor queries mail to:ganapathy.android@gmail.com \n\n\nSelected File for Conversion:\n" + this._fileName);
    }

    public void onConvert(View v) {
        convertArchive();
    }

    public void convertArchive() {
        final Button startButton = (Button) findViewById(C0252R.id.startButton);
        startButton.setEnabled(false);
        final String newFileName = this._storagePath + File.separator + this._fileName + ".bmz";
        try {
            if (new File(newFileName).exists()) {
                TextView textView = new TextView(getApplicationContext());
                textView.setText("Archive file already exist. Do you want to overwrite?");
                Utility.setMediumTitle(textView, getApplicationContext());
                new Builder(this).setTitle("Archive Match!").setView(textView).setIcon(17301543).setCancelable(false).setPositiveButton("Yes", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.DeleteFileIfExists(newFileName);
                        ConvertToZip.this.doConvert();
                    }
                }).setNegativeButton("No", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startButton.setEnabled(true);
                    }
                }).show();
                return;
            }
            doConvert();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), 1).show();
        }
    }

    public void doConvert() {
        final Button startButton = (Button) findViewById(C0252R.id.startButton);
        new AsyncTask<Boolean, Void, Void>() {
            private ProgressDialog pd;

            protected void onPreExecute() {
                this.pd = new ProgressDialog(ConvertToZip.this);
                this.pd.setTitle("Converting RAW to Best Archive...");
                this.pd.setMessage("Please wait.");
                this.pd.setCancelable(false);
                this.pd.setIndeterminate(true);
                this.pd.show();
            }

            protected Void doInBackground(Boolean... arg0) {
                try {
                    ConvertToZip.this.splitAndSaveMatch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                if (this.pd.isShowing()) {
                    this.pd.dismiss();
                }
                startButton.setEnabled(true);
                Toast.makeText(ConvertToZip.this.getApplicationContext(), "Conversion successful! You can now DELETE source RAW file to save disk space. Thank you!", 1).show();
            }
        }.execute(new Boolean[0]);
    }

    public void splitAndSaveMatch() throws Exception {
        String rawFileName = this._storagePath + File.separator + this._fileName + ".bcs";
        String newFileName = this._storagePath + File.separator + this._fileName + ".bmz";
        String location = getApplicationContext().getFilesDir().getPath();
        List<String> fileNames = new ArrayList();
        String BCS = "<BCS>";
        String BCS_END = "</BCS>";
        String MATCH_STARTS = "<Match ID";
        String MATCH_END = "</Match>";
        FileInputStream fileIS = new FileInputStream(new File(rawFileName));
        BufferedReader buffreader = new BufferedReader(new InputStreamReader(fileIS));
        String readString = "";
        String localFileName = "";
        FileOutputStream fileOS = null;
        while (true) {
            int ch = buffreader.read();
            if (ch == -1) {
                break;
            }
            readString = readString + ((char) ch);
            if (!readString.startsWith("<")) {
                if (fileOS != null) {
                    fileOS.write(readString.getBytes());
                }
                readString = "";
            } else if (readString.endsWith(">")) {
                if (!(readString.equalsIgnoreCase("<BCS>") || readString.equalsIgnoreCase("</BCS>"))) {
                    if (readString.startsWith("<Match ID")) {
                        localFileName = location + File.separator + readString.substring(readString.indexOf("'") + 1, readString.lastIndexOf("'")) + ".bcs";
                        fileNames.add(localFileName);
                        if (!(null == null || fileOS == null)) {
                            fileOS.flush();
                            fileOS.close();
                        }
                        File file = new File(localFileName);
                        file.createNewFile();
                        fileOS = new FileOutputStream(file);
                        fileOS.write("<BCS>".getBytes());
                        fileOS.write(readString.getBytes());
                    } else if (readString.equalsIgnoreCase("</Match>")) {
                        fileOS.write(readString.getBytes());
                        fileOS.write("</BCS>".getBytes());
                    } else {
                        fileOS.write(readString.getBytes());
                    }
                }
                readString = "";
            }
        }
        fileIS.close();
        Utility.zip((String[]) fileNames.toArray(new String[fileNames.size()]), newFileName);
        for (String file2 : fileNames) {
            Utility.DeleteFileIfExists(file2);
        }
        fileNames.clear();
    }
}
