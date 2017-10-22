package com.ganapathy.cricscorer;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FAQ extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0252R.layout.activity_faq);
        ((ExpandableListView) findViewById(C0252R.id.faqList)).setAdapter(new SimpleExpandableListAdapter(this, createGroupList(), C0252R.layout.simplerow_pad40, new String[]{"Group Item"}, new int[]{C0252R.id.rowTextView}, createChildList(), C0252R.layout.simplerow_pad40, new String[]{"Sub Item"}, new int[]{C0252R.id.rowTextView}));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private List<Map<String, String>> createGroupList() {
        List<Map<String, String>> result = new ArrayList();
        result.add(getQuestion("Q: Is it possible to change Black background which is very difficult to see in bright sunlight? (or) Can I change default background to White?"));
        result.add(getQuestion("Q: How do I add/insert new players during the game?"));
        result.add(getQuestion("Q: What is \"Restrict Balls per Over\" option in Additional settings?"));
        result.add(getQuestion("Q: What will \"Replace player\" do?"));
        result.add(getQuestion("Q: How can I move matches from FREE to FULL version?"));
        result.add(getQuestion("Q: What is \"Balls per Over\" option in Additional settings?"));
        result.add(getQuestion("Q: What is the use of \"Ball Spots\"?"));
        result.add(getQuestion("Q: How do I export the match summary to Excel or Word?"));
        result.add(getQuestion("Q: What is RAW Archive?"));
        result.add(getQuestion("Q: What is Best Archive File? Why should I convert existing Archive file to Best Archive file?"));
        return result;
    }

    private Map<String, String> getQuestion(String question) {
        Map<String, String> data = new HashMap();
        data.put("Group Item", question);
        return data;
    }

    private List<List<Map<String, String>>> createChildList() {
        List<List<Map<String, String>>> result = new ArrayList();
        result.add(getAnswer("A: Yes, you can. Go to \"Configuration\" option in the main menu which provides the option to select \"Theme\". Choose \"Dark\" for black background and \"Light\" for white background. Remember to choose \"Save\"."));
        result.add(getAnswer("A: Turn OFF \"Preset teams\" option in Match Settings.\nWhile creating the match, you can go to \"Additional Settings\" and turn OFF \"Preset Teams\".\nDuring the match, you can choose \"Options\" button which is next to \"Stats\" button and choose \"Match Settings\". In the available list, you can uncheck \"Preset Teams\" to 'type in' the new player."));
        result.add(getAnswer("A: This option will restrict the balls per over irrespective of \"extras\" bowled. If no extras are bowled, over will end as per \"Balls per over\". Default is 8. For example, if Restrict balls per over is configured with 8 balls, no matter how many wides or noballs are bowled, over will end on 8th ball."));
        result.add(getAnswer("A: \"Replace\" player option will replace the choosen player with the new player in that particular innings. More specifically last innings of the chosen player in the match.\n\nREPLACE will only affect the current innings for batsman or current over for bowler in the match. To RENAME the player like correcting the spellings etc., for the entire match, go to \"Manage Teams\" and do it so.\n\nThere are 3 scenarios to explain REPLACE:\n\nFirst scenario: I have chosen a player by mistake and started scoring runs. If I want to replace that player with original player, I can choose \"Replace\" option.\n\nFor example: I have choosen \"Robin\" and \"Jack\" as batsmen and continued scoring. After scoring 3 balls, I realized a mistake that \"Tom\" is actually playing in the ground instead of \"Robin\". Now I can click on \"Robin\" and select \"Replace\" player with \"Tom\" which will update all the scores to \"Tom\" on that innings\n\nSecond scenario: I have mistakenly chosen already \"out\" batsman and started scoring runs. You can replace the player with original player. BCS is intelligent to \"replace\" only the last innings that player played in the match.\n\nThird scenario: I have mistakenly chosen wrong bowler and continued over. During the over, you can replace the bowler with original bowler.\n\nIMPORTANT NOTE: \"Replace\" option cannot be undone."));
        result.add(getAnswer("A: Two Steps:\n1. In FREE version, go to \"Archive\" and make sure you choose \"Backup\". Then select the matches to be archived (Multi selection is allowed). You can specify the archive file name or you can leave it to the default Archive.bcs. Click \"Backup\" button will archive all the selected matches.\n\n2. In FULL version, go to \"Archive\" and choose \"Restore\" option at the top. Then select the archive filename which contains archived matches (If you left it to default, it is Archive.BCS which is under BCS folder of your SD Card). Then choose \"Get Matches\" which will show all the available matches in the archive file. Select the matches to be restored into FULL version and click \"Restore\" button which will restore the matches. "));
        result.add(getAnswer("A: This is to define number of balls can be bowled in an over. Default value is 6."));
        result.add(getAnswer("A: By keeping track of the ball spots, it will allow particular bowler or team bowlers to analyze their bowling skills. Percentage will clearly show you where the bowler or bowlers are strong in the area."));
        result.add(getAnswer("A: You can open \"Scoardcard.html\" in Excel or Word by choosing \"Open with\" option. This will allow you to edit or modify any contents of the Match summary."));
        result.add(getAnswer("A: RAW Archive is older methodology to backup the matches. From FULL v3.4 and FREE v4.4, archives will be created using Best Archive methodology. It helps in better memory handling and better performance."));
        result.add(getAnswer("A: Best Archive file is newly implement archiving methodology to store matches in a smart way for better performance and for better memory handling. Converting it to Best Archive might help you to restore huge archive files."));
        return result;
    }

    private List<Map<String, String>> getAnswer(String answer) {
        List<Map<String, String>> result = new ArrayList();
        Map<String, String> data = new HashMap();
        data.put("Sub Item", answer);
        result.add(data);
        return result;
    }
}
