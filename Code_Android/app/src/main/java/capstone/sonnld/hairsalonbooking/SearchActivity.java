package capstone.sonnld.hairsalonbooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.model.Suggesttion;

public class SearchActivity extends AppCompatActivity {
    FloatingSearchView searchView;
    private List<Suggesttion> mSuggestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

                initData();
        searchView = findViewById(R.id.floating_search_view);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    searchView.swapSuggestions(getSuggestion(newQuery));
                    searchView.hideProgress();
                }
            }
        });
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.showProgress();
                searchView.swapSuggestions(getSuggestion(searchView.getQuery()));
                searchView.hideProgress();
            }

            @Override
            public void onFocusCleared() {

            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Suggesttion suggestion = (Suggesttion) searchSuggestion;
                Toast.makeText(getApplicationContext(), "Bạn vừa tìm " + suggestion.getBody(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });
    }

    private void initData() {


        mSuggestions.add(new Suggesttion("4rau Barber "));
        mSuggestions.add(new Suggesttion("Cắt Tóc Sài Gòn"));
        mSuggestions.add(new Suggesttion("SunShine"));
        mSuggestions.add(new Suggesttion("FreeStyle Salon"));
        mSuggestions.add(new Suggesttion("Tony Hair Salon"));


    }

    private List<Suggesttion> getSuggestion(String query) {
        List<Suggesttion> suggestions = new ArrayList<>();
        for (Suggesttion suggestion : mSuggestions) {
            if (suggestion.getBody().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }
}
