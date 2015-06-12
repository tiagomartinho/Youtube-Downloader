package hackerspace.invento.youtubedownloader;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by invento on 28/5/15.
 */
public class YoutubeHandler {


    private YouTube youtube;
    private YouTube.Search.List query;
    Long MaxResults = 25L;

    public static final String KEY = "AIzaSyDgWzLARD5rt5gfud0lylXOQxKkNIOe_eQ";

    public YoutubeHandler(Context context) {

        youtube = new YouTube.Builder(new NetHttpTransport(),new JacksonFactory(),new HttpRequestInitializer(){
             @Override
             public void initialize(HttpRequest hr) throws IOException{}
         }).setApplicationName(context.getString(R.string.app_name)).build();

        try{
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setMaxResults(MaxResults);
            query.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
        }catch(IOException e){
            Log.d("YH", "Could not initialize: " + e);
        }
    }

    public ArrayList<Video> Search(String word){
        query.setQ(word);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            ArrayList<Video> vids = new ArrayList<Video>();

            Log.d("Title",results.get(0).getSnippet().getTitle());

            for(SearchResult result:results){
                Video vid = new Video();
                Log.d("Title",result.getSnippet().getTitle());
                vid.setTitle(result.getSnippet().getTitle());

                vid.setimgurl(result.getSnippet().getThumbnails().getDefault().getUrl());
                vid.setURL(result.getId().getVideoId());
                vids.add(vid);
            }
            return vids;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }

}
