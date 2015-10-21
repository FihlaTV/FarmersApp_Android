package com.farmers.underground.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.LastCropPricesModel;
import com.farmers.underground.ui.adapters.AllPricesAdapter;
import com.farmers.underground.ui.adapters.ProjectPagerAdapter;
import com.farmers.underground.ui.base.BaseActivity;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.CustomSearchView;
import com.farmers.underground.ui.dialogs.MorePriecesDialogFragment;
import com.farmers.underground.ui.fragments.AllPricesFragment;
import com.farmers.underground.ui.fragments.MarketeerPricesFragment;
import com.farmers.underground.ui.fragments.PeriodPickerFragment;
import com.farmers.underground.ui.fragments.StatisticsFragment;
import com.farmers.underground.ui.models.DateRange;
import com.farmers.underground.ui.utils.DateFormaterUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by omar
 * on 10/9/15.
 */
public class PricesActivity extends BaseActivity implements AllPricesAdapter.AllPricesCallback {

    public static final int REQUEST_CODE_PERIOD_PICKER  = 5;
    public static final int REQUEST_CODE_DIALOG_WHY     = 2;

    @Bind(R.id.drawer_conainer_MainActivity)
    protected FrameLayout mainContainer;

    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

    @Bind(R.id.tabs_PricesActivity)
    protected TabLayout tabLayout;

    @Bind(R.id.vp_PricesActivity)
    protected ViewPager viewPager;

    @Bind(R.id.searchView)
    protected CustomSearchView searchView;

    @Bind(R.id.action_calendar)
    protected ImageView calendar;

    private Target target;

    private LastCropPricesModel mCropModel;
    private ProjectPagerAdapter<BaseFragment<PricesActivity>> pagerAdapter;

    public static void start(@NonNull Context context, LastCropPricesModel cropModel) {
        Gson gson = new GsonBuilder().create();
        String s = gson.toJson(cropModel);
        Intent intent = new Intent(context, PricesActivity.class);
        intent.putExtra(ProjectConstants.KEY_DATA, s);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataOnStart(getIntent());
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView.setVisibility(View.VISIBLE);
        calendar.setVisibility(View.VISIBLE);

        setViewPager();
        setTabs();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_prices;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    private void getDataOnStart(Intent intent) {
        Gson gson = new GsonBuilder().create();
        mCropModel = gson.fromJson(intent.getStringExtra(ProjectConstants.KEY_DATA), LastCropPricesModel.class);
        if (mCropModel == null)
            throw new IllegalAccessError("Create this activity with start(Context, CropModel) " + "method only!");
    }


    //ViewPager

    public void setViewPager() {

        pagerAdapter = new ProjectPagerAdapter<>(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        pagerAdapter.setFragments(getFragmentList());
        pagerAdapter.setTitles(getTitlesList());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
    }

    private List<String> getTitlesList() {
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.prices_activity_tab1));
        titles.add(getString(R.string.prices_activity_tab2));
        titles.add(getString(R.string.prices_activity_tab3));
        return titles;
    }

    private List<BaseFragment<PricesActivity>> getFragmentList() {
        List<BaseFragment<PricesActivity>> fragmentList = new ArrayList<>();

        fragmentList.add(createStatisticsPricesFragment(mCropModel));
        fragmentList.add(createMarketeerPricesFragment(mCropModel));
        fragmentList.add(createAlLPricesFragment(mCropModel));

        return fragmentList;
    }

    private AllPricesFragment createAlLPricesFragment(LastCropPricesModel cropModel) {
        return AllPricesFragment.getInstance(cropModel);
    }

    private MarketeerPricesFragment createMarketeerPricesFragment(LastCropPricesModel cropModel) {
        return MarketeerPricesFragment.getInstance(cropModel);
    }

    private StatisticsFragment createStatisticsPricesFragment(LastCropPricesModel cropModel) {
        return StatisticsFragment.getInstance(cropModel);
    }

    private void setTabs() {
        tabLayout.setupWithViewPager(viewPager);
    }

    // all prices fragment callbacks
    @Override
    public void onAllPricesItemClicked(LastCropPricesModel cropModel) {
        //hz
    }

    @Override
    public void onAddPricesClicked(LastCropPricesModel cropModel) {
        AddPriceActivity.start(this, mCropModel);
    }

    @Override
    public void onMorePricesClicked(LastCropPricesModel cropModel) {
        TransparentActivity.startWithFragment(this, new MorePriecesDialogFragment());
    }


    public interface DateRangeSetter {
        void setDateRange(DateRange dateRange);
    }


    /** for
     * StatisticsFragment -  has two pages;
     * */
    public interface PageListener {
        void onPageSelected(int page);
    }

    // options menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_back).setVisible(true);
        menu.findItem(R.id.action_burger).setVisible(false);
        menu.findItem(R.id.action_icon).setVisible(true);
        final MenuItem icon = menu.findItem(R.id.action_icon);

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                icon.setIcon(new BitmapDrawable(bitmap));

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(this).load(mCropModel.image).transform(new CropCircleTransformation()).into(target);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
             case R.id.action_back:
               onBackPressed();
                return true;
        }

        return false;
    }

    //clicke events
    @OnClick(R.id.action_calendar)
    protected void onCalendarClick(){

        TransparentActivity.startWithFragmentForResult(this, new PeriodPickerFragment(), REQUEST_CODE_PERIOD_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PERIOD_PICKER:
                    Bundle bundle = data.getExtras();
                    Calendar dateFrom = (Calendar) bundle.getSerializable(PeriodPickerFragment.KEY_DATE_FROM);
                    Calendar dateTo = (Calendar) bundle.getSerializable(PeriodPickerFragment.KEY_DATE_TO);
                    DateRange dateRange = new DateRange();
                    dateRange.setDateFrom(DateFormaterUtil.parseToServerResponse(dateFrom));
                    dateRange.setDateTo(DateFormaterUtil.parseToServerResponse(dateTo));
                    ((PricesActivity.DateRangeSetter) pagerAdapter.getItem(viewPager.getCurrentItem())).setDateRange(dateRange);
                    //todo search request
                    break;
                case REQUEST_CODE_DIALOG_WHY:
                    onAddPricesClicked(mCropModel);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
