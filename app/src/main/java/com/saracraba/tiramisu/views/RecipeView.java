package com.saracraba.tiramisu.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saracraba.tiramisu.R;

import java.util.ArrayList;
import java.util.List;


public class RecipeView extends LinearLayout {
    private final static String TAG = RecipeView.class.getName();

    private StepAdapter mStepAdapter;

    private Context mContext;

    private TextView mTitleView;
    private LinearLayout mIngredientsView;
    private RecyclerView mStepsView;

    private List<String> mStepsList;

    public RecipeView(Context context) {
        super(context);
        init(context);
    }

    public RecipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.recipe_layout, this, true);
        setOrientation(LinearLayout.VERTICAL);

        mContext = context;

        mTitleView = (TextView) view.findViewById(R.id.recipe_title);
        mIngredientsView = (LinearLayout) view.findViewById(R.id.recipe_ingredients_list);
        mStepsView = (RecyclerView) view.findViewById(R.id.recipe_steps_list);

        // set steps list
        mStepsList = new ArrayList<>(); // avoid null list
        mStepAdapter = new StepAdapter();
        mStepsView.setAdapter(mStepAdapter);
        mStepsView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    /**
     * Clear view first and populate the view with the passed arguments
     *
     * @param title       title of the recipe
     * @param ingredients ingredient list
     * @param steps       directions list
     */
    public void populateView(String title, List<String> ingredients, List<String> steps) {
        clearView();

        // set title
        mTitleView.setText(title);

        // set ingredient list
        for (String ingredient : ingredients) {
            IngredientView ingredientView = new IngredientView(mContext);
            ingredientView.setText(ingredient);
            mIngredientsView.addView(ingredientView);
        }

        // set steps list
        mStepsList = steps;
        mStepAdapter.notifyDataSetChanged();
    }

    /**
     * Clear all fields of the recipe view
     */
    public void clearView() {
        mTitleView.setText("");
        mIngredientsView.removeAllViews();
        mStepsView.removeAllViews();
    }


    // Recycle view adapter for recipe directions list
    private class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {
        @Override
        public StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_step, parent, false);
            return new StepHolder(view);
        }

        @Override
        public void onBindViewHolder(StepHolder holder, int position) {
            holder.mStepNumber.setText(Integer.toString(position + 1) + ".");
            holder.mStepDescription.setText(mStepsList.get(position));
        }

        @Override
        public int getItemCount() {
            return mStepsList.size();
        }

        class StepHolder extends RecyclerView.ViewHolder {
            private TextView mStepNumber;
            private TextView mStepDescription;

            private StepHolder(View viewItem) {
                super(viewItem);
                mStepNumber = (TextView) viewItem.findViewById(R.id.step_number);
                mStepDescription = (TextView) viewItem.findViewById(R.id.step_description);

            }
        }
    }

    // Item view of the recipe ingredients list
    private class IngredientView extends LinearLayout {
        private TextView mTextView;

        public IngredientView(Context context) {
            super(context);
            init(context);
        }

        public IngredientView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init(context);
        }

        private void init(Context context) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.recipe_ingredient, this, true);

            mTextView = (TextView) view.findViewById(R.id.ingredient_name);
        }

        public void setText(String text) {
            mTextView.setText(text);
        }
    }
}
