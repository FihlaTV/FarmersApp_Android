package com.farmers.underground.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.farmers.underground.R;
import com.farmers.underground.config.ProjectConstants;
import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.activities.FragmentViewsCreatedCallback;
import com.farmers.underground.ui.adapters.CropsListAdapter;
import com.farmers.underground.ui.base.BaseFragment;
import com.farmers.underground.ui.custom_views.DividerItemDecoration;
import com.farmers.underground.ui.models.CropsListFragmentModel;
import com.farmers.underground.ui.models.CropsListItemDH;
import com.farmers.underground.ui.utils.ResourceRetriever;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 9/30/15.
 */
public class CropsListFragment
        extends BaseFragment
        implements SearchQueryFragmentCallback {

    @Bind(R.id.rv_FragmentCrops)
    protected RecyclerView recyclerView;

    @Bind(R.id.tv_NoItemsCropsFragment)
    protected TextView tv_NoItems;

    private CropsListFragmentModel thisModel;
    private CropsListAdapter adapter;
    private FragmentViewsCreatedCallback stateCallback;
    private CropsListAdapter.CropsAdapterCallback listCallback;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        stateCallback = (FragmentViewsCreatedCallback)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        stateCallback = null;
        listCallback = null;
    }


    public static BaseFragment getInstance(CropsListFragmentModel.TYPE type, String query) {
        CropsListFragmentModel fragmentModel = new CropsListFragmentModel(type, query);
        Bundle args = new Bundle();
        args.putSerializable(ProjectConstants.KEY_DATA, fragmentModel);
        CropsListFragment fragment = new CropsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_crops_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, v);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(ResourceRetriever.retrievePX(getContext(),R.dimen
                .crops_card_layout_margin)));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stateCallback.onFragmentViewDestroyed();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null)
            thisModel = (CropsListFragmentModel) savedInstanceState.getSerializable(ProjectConstants.KEY_DATA);
        else thisModel = (CropsListFragmentModel) getArguments().getSerializable(ProjectConstants.KEY_DATA);
        //showNoItems();
        adapter = new CropsListAdapter();
        recyclerView.setAdapter(adapter);
        stateCallback.onFragmentViewCreated();
    }

    private void showNoItems() {
        tv_NoItems.setVisibility(View.VISIBLE);
        if (!thisModel.getQuerry().isEmpty())
            tv_NoItems.setText(getActivity().getString(R.string.no_crops_found) + thisModel.getQuerry());
        else
            tv_NoItems.setText(getActivity().getString(R.string.no_crops_found) + getActivity().getString(R.string.all));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ProjectConstants.KEY_DATA, thisModel);
    }


    @Override
    public void setListCallback(CropsListAdapter.CropsAdapterCallback callback) {
        listCallback = callback;
    }

    @Override
    public void onReceiveStringQuerry(String querry) {
        thisModel.setQuerry(querry);
       // showNoItems();
    }

    @Override
    public void onReceiveCrops(List<CropModel> cropsList) {
        adapter.setDataList(generateCropsDataHolders(cropsList));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyFavsRefresh() {
        adapter.notifyDataSetChanged();
    }

    private List<CropsListItemDH> generateCropsDataHolders(List<CropModel> cropsList) {
        List<CropsListItemDH> dhList = new ArrayList<>();
        for (CropModel model : cropsList) {
            dhList.add(new CropsListItemDH(model, thisModel.getType(), listCallback));
        }
        return dhList;
    }

}
