package com.example.translation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // SMMRY APIのAPIキー
    private String API_KEY = "282C0D3A22";
    /**
     * メインアクティビティのログタグ。
     */
    private static final String TAG = "MP7: Summary API";

    /**
     * APIリクエストを管理するためのリクエストキュー。
     */
    private static RequestQueue requestQueue;

    /**
     * アクティビティが作成されたときに呼び出されます。
     * @param savedInstanceState 前のインスタンスから保存されたアプリの状態。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // レイアウトからボタンビューを取得
        ImageButton getSummary = findViewById(R.id.button);

        // ボタンクリックイベントを処理するためのクリックリスナーを設定
        getSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // APIコールを開始
                startAPICall();
            }
        });
    }

    /**
     * APIコールを開始するメソッド。
     */
    void startAPICall() {
        try {
            // JSONオブジェクトリクエストを作成
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.smmry.com/&SM_API_KEY=" + API_KEY
                            + "&SM_URL=https://www.nytimes.com/2018/04/21/us/barbara-bush-funeral" +
                            ".html?rref=collection%2Fsectioncollection%2Fus&action" +
                            "=click&contentCollection=us&region=rank&module=package" +
                            "&version=highlights&contentPlacement=2&pgtype=sectionfront",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                // レスポンスをログに出力
                                Log.d(TAG, response.toString(2));
                            } catch (JSONException ignored) {
                            }
                            /**
                             * この部分は正しく動作していないようです。
                             * 配列を正しく解析する方法がわからない。
                             */
                            try {
                                JSONArray smArray = new JSONArray();
                                String text = "";
                                for (int i = 0; i < smArray.length(); i++) {
                                    JSONObject obj = (JSONObject) smArray.get(i);
                                    text = obj.get("sm_api_content").toString();
                                }
                                // 要約テキストを表示するためのTextViewを取得
                                TextView summary = findViewById(R.id.question);
                                summary.setText(text);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    // エラーレスポンスをログに出力
                    Log.e(TAG, error.toString());
                }
            });
            // リクエストキューにリクエストを追加
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
