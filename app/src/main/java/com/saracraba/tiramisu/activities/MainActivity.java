package com.saracraba.tiramisu.activities;

import android.os.Bundle;

import com.saracraba.tiramisu.network.DownloadHtmlPage;
import com.saracraba.tiramisu.R;
import com.saracraba.tiramisu.views.RecipeView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends TiramisuBaseActivity {
    private static final String TAG = MainActivity.class.getName();

    private static final String URL = "http://www.misya.info/ricetta/brownies-senza-cottura.htm";
    private static final String INGREDIENTS_BOX = "ul.list-ingr li";
    private static final String RECIPE_TITLE = "article div h1";
    private static final String INSTRUCTIONS = "div[name=istruzioni] p";

    @BindView(R.id.main_recipe_view)
    RecipeView mRecipeView;

    @OnClick(R.id.main_button)
    public void onDownload() {
        // TODO check network connection

        // Download the htm document
        new DownloadHtmlPage(URL, mDownloadTaskResponse);
    }

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

        //mRecipeView = (RecipeView) findViewById(R.id.main_recipe_view);
        ButterKnife.bind(this);
    }

    private void onDownloadResponse(Document jsoupDocument) {
        Elements title = jsoupDocument.select(RECIPE_TITLE);
        Elements ingredients = jsoupDocument.select(INGREDIENTS_BOX);
        Elements instructions = jsoupDocument.select(INSTRUCTIONS);

        List<String> ingredientsList = new ArrayList<>();
        for (Element ingredient : ingredients) {
            ingredientsList.add(ingredient.text());
        }

        List<String> stepsList = new ArrayList<>();
        for (Element instruction : instructions) {
            stepsList.add(instruction.text());
        }

        mRecipeView.populateView(title.text(), ingredientsList, stepsList);
    }
}
