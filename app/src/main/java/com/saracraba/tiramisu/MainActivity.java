package com.saracraba.tiramisu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "http://www.misya.info/ricetta/brownies-senza-cottura.htm";
    private static final String INGREDIENTS_BOX = "ul.list-ingr li";
    private static final String RECIPE_TITLE = "article div h1";
    private static final String INSTRUCTIONS = "div[name=istruzioni] p";

    private Button mMainButton;
    private TextView mMainEditText;

    private DownloadHtmlPage.DownloadTaskResponse mDownloadTaskResponse =
            new DownloadHtmlPage.DownloadTaskResponse() {
                @Override
                public void onResult(Document result) {
                    onDownloadResponse(result);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainButton = (Button) findViewById(R.id.main_button);
        mMainEditText = (TextView) findViewById(R.id.main_text_view);

        // TODO check network connection
        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Download the htm text and show in the edit text
                new DownloadHtmlPage(URL, mDownloadTaskResponse);
            }
        });
    }

    private void onDownloadResponse(Document jsoupDocument) {
        Elements ingredients = jsoupDocument.select(INGREDIENTS_BOX);
        Elements title = jsoupDocument.select(RECIPE_TITLE);
        Elements instructions = jsoupDocument.select(INSTRUCTIONS);

        StringBuilder ingredientsList = new StringBuilder();
        for (Element ingredient : ingredients) {
            ingredientsList.append(ingredient.text());
            ingredientsList.append("\n");
        }

        StringBuilder instructionsList = new StringBuilder();
        for (Element instruction : instructions) {
            ingredientsList.append(instruction.text());
            ingredientsList.append("\n");
        }

        mMainEditText.setText(title.text() + "\n" + ingredientsList.toString() + "\n" +
                instructionsList.toString());
    }
}
