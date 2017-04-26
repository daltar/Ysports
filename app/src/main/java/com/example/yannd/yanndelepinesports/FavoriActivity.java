package com.example.yannd.yanndelepinesports;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yannd on 25/04/2017.
 */

public class FavoriActivity extends AppCompatActivity {


    DataBaseHandler myDb;
    private TextView tvData;
    private ListView lvArticles;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DataBaseHandler(this);


        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait..."); // showing a dialog for loading the data
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start

        lvArticles = (ListView)findViewById(R.id.lvArticles);


        Cursor res = myDb.getAllData();

        StringBuffer buffer = new StringBuffer();
        JSONArray favorilist = new JSONArray();
        while (res.moveToNext()) {
            try {
                String json = res.getString(0);
                JSONObject favori = new JSONObject(json);
                favorilist.put(favori);

            } catch (JSONException e){
                e.printStackTrace();
            }
        }






    }


    public class JSONTask extends AsyncTask<String,String, List<ArticleModel> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<ArticleModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("articles");

                List<ArticleModel> ArticleModelList = new ArrayList<>();

                Gson gson = new Gson();
                for(int i=0; i<parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    /**
                     * below single line of code from Gson saves you from writing the json parsing yourself
                     * which is commented below
                     */
                    ArticleModel ArticleModel = gson.fromJson(finalObject.toString(), ArticleModel.class); // a single line json parsing using Gson

                    // adding the final object in the list
                    ArticleModelList.add(ArticleModel);
                }
                return ArticleModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(final List<ArticleModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(result != null) {
                FavoriActivity.JSONTask.ArticleAdapter adapter = new FavoriActivity.JSONTask.ArticleAdapter(getApplicationContext(), R.layout.row, result);
                lvArticles.setAdapter(adapter);
              /*   lvArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // list item click opens a new detailed activity
                   @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ArticleModel articleModel = result.get(position); // getting the model
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("movieModel", new Gson().toJson(articleModel)); // converting model json into string type and sending it via intent
                        startActivity(intent);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }*/
            }
        }



        public class ArticleAdapter extends ArrayAdapter {

            private List<ArticleModel> articleModelList;
            private int resource;
            private LayoutInflater inflater;
            public ArticleAdapter(Context context, int resource, List<ArticleModel> objects) {
                super(context, resource, objects);
                articleModelList = objects;
                this.resource = resource;
                inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                FavoriActivity.JSONTask.ArticleAdapter.ViewHolder holder = null;

                if(convertView == null){
                    holder = new FavoriActivity.JSONTask.ArticleAdapter.ViewHolder();
                    convertView = inflater.inflate(resource, null);
                    holder.ivArticleIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
                    holder.tvArticle = (TextView)convertView.findViewById(R.id.tvArticle);
                    holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);


                    convertView.setTag(holder);
                } else {
                    holder = (FavoriActivity.JSONTask.ArticleAdapter.ViewHolder) convertView.getTag();
                }

                final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar);

                // Then later, when you want to display image
                final FavoriActivity.JSONTask.ArticleAdapter.ViewHolder finalHolder = holder;
                ImageLoader.getInstance().displayImage(articleModelList.get(position).getUrlToImage(), holder.ivArticleIcon, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setVisibility(View.VISIBLE);
                        finalHolder.ivArticleIcon.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                        finalHolder.ivArticleIcon.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                        finalHolder.ivArticleIcon.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        progressBar.setVisibility(View.GONE);
                        finalHolder.ivArticleIcon.setVisibility(View.INVISIBLE);
                    }
                });

                holder.tvArticle.setText(articleModelList.get(position).getTitle());
                holder.tvDate.setText(articleModelList.get(position).getPublisheDate() + " " + articleModelList.get(position).getPublisheTime());



                return convertView;
            }


            class ViewHolder{
                private ImageView ivArticleIcon;
                private TextView tvArticle;
                private TextView tvDate;

            }

        }



    }


}
