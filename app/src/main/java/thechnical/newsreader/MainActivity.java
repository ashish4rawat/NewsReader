package thechnical.newsreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

        String con = "";
        DownloadContent downloadHTML;
        DownloadContent2 downloadID;
        ArrayList<String> al;
        ArrayList<String> au;
        ListView listView;

        public class DownloadContent extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... strings) {


                        try {
                                String result = "";
                                URL url = new URL(strings[0]);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                InputStream is = connection.getInputStream();
                                InputStreamReader reader = new InputStreamReader(is);

                                int data = reader.read();

                                while (data != -1) {

                                        char ch = (char) data;
                                        result += ch;

                                        data = reader.read();
                                }

                                return result;


                        } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("Info", "Exception");
                        }


                        return "Failed";
                }
        }


        public class DownloadContent2 extends AsyncTask<String, Void, String> {

                @Override
                protected String doInBackground(String... strings) {


                        try {
                                String results = "";
                                URL url = new URL(strings[0]);
                                HttpURLConnection connections = (HttpURLConnection) url.openConnection();
                                InputStream is = connections.getInputStream();
                                InputStreamReader reader = new InputStreamReader(is);

                                int data = reader.read();

                                while (data != -1) {

                                        char ch = (char) data;
                                        results += ch;

                                        data = reader.read();
                                }

                                return results;


                        } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("Info", "Exception");
                        }


                        return "Failed";
                }
        }




        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                al = new ArrayList<>();
                au = new ArrayList<>();
                listView = (ListView)findViewById(R.id.listView);
                ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,al);
                listView.setAdapter(adapter);






                downloadHTML = new DownloadContent();


                // Downloadingg conttent
                try {

                        con = downloadHTML.execute("https://news.ycombinator.com/").get();

                } catch (Exception e) {

                        Log.i("Info", "Exception in HTML");
                }

                //extracting id from the news items
                Pattern p = Pattern.compile("id='up_(.*?)'");
                Matcher m = p.matcher(con);

                int try_num=0,catch_num=0,total;

                while (m.find()) {

                        try{
                                String id = m.group(1);
                                Log.i("Id = ", id);
                                String data = new DownloadContent().execute("https://hacker-news.firebaseio.com/v0/item/"+ id + ".json?print=pretty").get() ;

                                JSONObject jsonObject = new JSONObject(data);
                                String theURL = jsonObject.getString("url");
                                String theTitle = jsonObject.getString("title");


                                Log.i("Info",theURL+ theTitle);

                                al.add(try_num,theTitle);
                                au.add(try_num,theURL);
                                adapter.notifyDataSetChanged();

                                ++try_num;

                        }
                        catch (Exception e)
                        {
                                e.printStackTrace();
                                Log.i("Info","Exception in downloading data of Id");

                        }

                }

                listView.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        Intent intent = new Intent(MainActivity.this , browser.class);
                                        intent.putExtra("url_name",au.get(i));
                                        startActivity(intent);

                                }
                        }

                );

                // https://hacker-news.firebaseio.com/v0/item/14980512.json?print=pretty

                //download details

                /*
                try {

                        for (int i = 0; i < 30; i++) {

                                alDetails.set(i, dc.execute("https://hacker-news.firebaseio.com/v0/item/" + alContent.get(i) + ".json?print=pretty").get());
                               Log.i("Aldetails ", alDetails.get(i));

                        }
                } catch (Exception e) {

                        Log.i("Info", "Exception in download details");


                }
                */

/*
                for(int j=0 ;j<30;j++){
                        try{

                                JSONObject jsonObject = new JSONObject(alDetails.get(j));
                                String url = jsonObject.getString("url");
                                Log.i("URL :",url);

                        }
                        catch (Exception e){

                                e.printStackTrace();
                        }



                }

*/



        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                        return true;
                }

                return super.onOptionsItemSelected(item);
        }
}
